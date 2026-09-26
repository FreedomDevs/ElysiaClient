package dev.elysia.elysiaclient

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.json.Json
import java.net.InetSocketAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID

object AuthManager {
    @Serializable
    data class EData(
        var currentSession: String?,
        var sessions: List<String>
    )

    @Serializable
    data class User(
        val id: String,
        val name: String,
        val permissions: Map<String, List<String>>,
        val groups: List<Group>,
        val createdAt: String,
        val updatedAt: String
    )

    @Serializable
    data class Group(
        val id: String,
        val name: String,
        val permissions: List<String>,
        val createdAt: String,
        val updatedAt: String
    )

    @Serializable
    data class Meta(
        val code: String,
        val traceId: String,
        val timestamp: String
    )

    @Serializable
    data class ApiResponse<T>(
        val data: T,
        val message: String,
        val meta: Meta
    )

    @Serializable
    data class AccessToken(
        @SerialName("token")
        val accessToken: String
    )

    private const val EDATA_FOLDER = "EData"
    private const val EDATA_MANIFEST = "edata.json"
    private lateinit var eData: EData

    private var currentUser: User? = null

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val executableDirectory = AppPaths.executableDirectory
    private val eDataFolder = executableDirectory.resolve(EDATA_FOLDER)

    private var onAuthStateChanged: (() -> Unit)? = null

    fun setOnAuthStateChanged(listener: () -> Unit) {
        onAuthStateChanged = listener
    }

    fun init(): Boolean {
        ELogger.info("Initializing AuthManager")

        if (!createOrExistEData()) {
            ELogger.error("Failed to create or initialize EData")
            return false
        }

        if (!loadEData()) {
            ELogger.error("Failed to load EData")
            return false
        }

        if (!LocalServer.start()) {
            ELogger.error("Failed to start LocalServer")
            return false
        }

        ELogger.info("AuthManager initialized successfully")
        return true
    }

    fun getCurRefreshToken(): String? {
        if (!::eData.isInitialized) {
            ELogger.warn("Attempted to get refresh token before EData initialization")
            return null
        }

        val curSession = eData.currentSession

        try {
            val file = eDataFolder.resolve("$curSession.session")

            if (!Files.exists(file)) {
                ELogger.warn("Current session file does not exist")
                return null
            }

            return Files.readString(file)
        } catch (e: Exception) {
            ELogger.error("Failed to read current session: ${e.message}")
            return null
        }
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun getAccessToken(): String? {
        if (!::eData.isInitialized) {
            ELogger.warn("Attempted to get access token before EData initialization")
            return null
        }

        if (eData.currentSession == null) {
            ELogger.warn("Attempted to get access token without active session")
            return null
        }

        return try {
            val refreshToken = getCurRefreshToken()

            if (refreshToken == null) {
                ELogger.warn("Failed to get current refresh token")
                return null
            }

            ELogger.info("Refreshing access token")

            val request = HttpRequest.newBuilder()
                .uri(URI.create(Config.API_URL + "/auth/refresh"))
                .header("Content-Type", "application/json")
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        """{"refresh_token":"$refreshToken"}"""
                    )
                )
                .build()

            val response = HttpClient.newHttpClient().send(
                request,
                HttpResponse.BodyHandlers.ofString()
            )

            if (response.statusCode() !in 200..299) {
                ELogger.warn(
                    "Access token refresh failed with status ${response.statusCode()}"
                )
                return null
            }

            val apiResponse =
                json.decodeFromString<ApiResponse<AccessToken>>(response.body())

            ELogger.info(
                "Access token refreshed successfully (${apiResponse.meta.code})"
            )

            apiResponse.data.accessToken
        } catch (e: Exception) {
            ELogger.error("Failed to refresh access token: ${e.message}")
            null
        }
    }

    fun getAndUpdateCurrentUser(): User? {
        if (!::eData.isInitialized) {
            ELogger.warn("Attempted to fetch current user before EData initialization")
            return null
        }

        if (eData.currentSession == null) {
            ELogger.warn("Attempted to fetch current user without active session")
            return null
        }

        val accessToken = getAccessToken() ?: return null

        return try {
            val client = HttpClient.newHttpClient()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(Config.API_URL + "/users/me"))
                .header("Authorization", "Bearer $accessToken")
                .GET()
                .build()

            ELogger.info("Fetching current user")

            val response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            )

            if (response.statusCode() !in 200..299) {
                ELogger.warn(
                    "Failed to fetch current user: ${response.statusCode()}"
                )
                return null
            }

            val apiResponse = Json.decodeFromString<ApiResponse<User>>(response.body())

            currentUser = apiResponse.data

            ELogger.info("Current user updated: ${currentUser?.name}")

            currentUser
        } catch (e: Exception) {
            ELogger.error("Failed to fetch current user: ${e.message}")
            null
        }
    }

    fun isAuth(): Boolean {
        ELogger.info("Checking authentication state")

        if (!::eData.isInitialized) {
            ELogger.warn("EData is not initialized")
            return false
        }

        if (eData.currentSession == null) {
            ELogger.info("No active authentication session")
            currentUser = null
            return false
        }

        if (getCurRefreshToken() == null) {
            ELogger.warn("Current refresh token not found")
            currentUser = null
            return false
        }

        val user = getAndUpdateCurrentUser()

        if (user == null) {
            ELogger.warn("Authentication check failed")
            currentUser = null
            return false
        }

        ELogger.info("Authentication successful: ${user.name}")

        return true
    }

    fun logOut(): Boolean {
        if (!::eData.isInitialized) {
            ELogger.warn("Attempted to logout before EData initialization")
            return false
        }

        try {
            eData.currentSession = null
            currentUser = null

            if (!writeEDataManifest()) {
                ELogger.error("Failed to save EData after logout")
                return false
            }

            ELogger.info("User logged out successfully")

            onAuthStateChanged?.invoke()

            return true
        } catch (e: Exception) {
            ELogger.error("Logout failed: ${e.message}")
            return false
        }
    }

    fun writeEDataRefreshTokenAndSetCurrentSession(token: String): Boolean {
        if (!::eData.isInitialized) {
            ELogger.warn("Attempted to save refresh token before EData initialization")
            return false
        }

        try {
            val randUuid = UUID.randomUUID()
            val file = eDataFolder.resolve("$randUuid.session")

            Files.createFile(file)

            if (!Files.exists(file)) {
                ELogger.error("Failed to create session file")
                return false
            }

            Files.writeString(file, token)

            if (Files.size(file) == 0L) {
                ELogger.error("Session file was created but is empty")
                return false
            }

            if (Files.readString(file) != token) {
                ELogger.error("Session token verification failed")
                return false
            }

            val newSessions = eData.sessions.toMutableList()
            newSessions.add(randUuid.toString())

            eData.sessions = newSessions
            eData.currentSession = randUuid.toString()

            ELogger.info("Current session set to: ${eData.currentSession}")

            if (!writeEDataManifest()) {
                ELogger.error("Failed to save EData manifest after creating session")
                return false
            }

            ELogger.info("New authentication session created")
            return true
        } catch (e: Exception) {
            ELogger.error("Failed to create authentication session: ${e.message}")
            return false
        }
    }

    private fun writeEDataManifest(): Boolean {
        if (!::eData.isInitialized) {
            ELogger.warn("Attempted to write EData before initialization")
            return false
        }

        try {
            val fileManifest = eDataFolder.resolve(EDATA_MANIFEST)
            val data = Json.encodeToString(eData)

            ELogger.info("Writing EData: $data")

            Files.writeString(fileManifest, data)

            if (!Files.exists(fileManifest)) {
                ELogger.error("EData manifest does not exist after writing")
                return false
            }

            if (Files.size(fileManifest) == 0L) {
                ELogger.error("EData manifest is empty after writing")
                return false
            }

            if (Files.readString(fileManifest) != data) {
                ELogger.error("EData manifest verification failed")
                return false
            }

            return true
        } catch (e: Exception) {
            ELogger.error("Failed to write EData manifest: ${e.message}")
            return false
        }
    }

    fun getEDataStruct(): EData? {
        if (!::eData.isInitialized) {
            return null
        }
        return eData
    }

    @Suppress("unused")
    fun getAllSessions(): List<UUID>? {
        return getEDataStruct()?.sessions?.map {
            UUID.fromString(it)
        }
    }

    @Suppress("unused")
    fun getCurrentSessions(): UUID? {
        return getEDataStruct()?.currentSession?.let {
            UUID.fromString(it)
        }
    }

    private fun createOrExistEData(): Boolean {
        try {
            val file = eDataFolder.resolve(EDATA_MANIFEST)

            if (!Files.exists(eDataFolder)) {
                ELogger.info("Creating EData directory")

                Files.createDirectories(eDataFolder)

                if (!Files.exists(eDataFolder)) {
                    ELogger.error("Failed to create EData directory")
                    return false
                }
            }

            val json = """
            {
                "currentSession": null,
                "sessions": []
            }
            """.trimIndent()

            fun abc(): Boolean {
                if (Files.size(file) == 0L) {
                    ELogger.info("Initializing EData manifest")

                    Files.writeString(file, json)

                    if (!Files.exists(file)) {
                        ELogger.error("Failed to create EData manifest")
                        return false
                    } else {
                        if (Files.size(file) == 0L) {
                            ELogger.error("EData manifest is empty")
                            return false
                        } else {
                            if (Files.readString(file) != json) {
                                ELogger.error("EData manifest verification failed")
                                return false
                            }
                        }
                    }
                }

                return true
            }

            return if (!Files.exists(file)) {
                ELogger.info("Creating EData manifest")

                Files.createFile(file)

                if (!Files.exists(file)) {
                    ELogger.error("Failed to create EData manifest")
                    false
                } else {
                    abc()
                }
            } else {
                abc()
            }
        } catch (e: Exception) {
            ELogger.error("Failed to initialize EData: ${e.message}")
            return false
        }
    }

    private fun loadEData(): Boolean {
        try {
            val file = eDataFolder.resolve(EDATA_MANIFEST)

            if (!Files.exists(file)) {
                ELogger.error("EData manifest does not exist")
                return false
            }

            val json = Files.readString(file)

            if (json.isEmpty()) {
                ELogger.error("EData manifest is empty")
                return false
            }

            eData = Json.decodeFromString<EData>(json)

            ELogger.info("EData loaded successfully")
            return true
        } catch (e: Exception) {
            ELogger.error("Failed to load EData: ${e.message}")
            return false
        }
    }

    private object LocalServer {
        private var server: HttpServer? = null
        private const val PORT = 48173

        fun start(): Boolean {
            return try {
                server = HttpServer.create(
                    InetSocketAddress("127.0.0.1", PORT),
                    0
                )

                server!!.createContext("/callback") { exchange ->
                    handleCallback(exchange)
                }

                server!!.start()

                ELogger.info("LocalServer started on port $PORT")
                true
            } catch (e: Exception) {
                ELogger.error("Failed to start LocalServer: ${e.message}")
                false
            }
        }

        private fun handleCallback(exchange: HttpExchange) {
            try {
                val query = exchange.requestURI.query

                if (query == null) {
                    ELogger.warn("Authentication callback received without query")
                    sendResponse(exchange, errorHtml())
                    return
                }

                val token = query
                    .split("&")
                    .map { it.split("=", limit = 2) }
                    .firstOrNull { it[0] == "token" }
                    ?.getOrNull(1)

                if (token.isNullOrEmpty()) {
                    ELogger.warn("Authentication callback received without token")
                    sendResponse(exchange, errorHtml())
                    return
                }

                ELogger.info("Authentication callback received")

                if (!writeEDataRefreshTokenAndSetCurrentSession(token)) {
                    ELogger.error("Failed to save authentication token")
                    sendResponse(exchange, errorHtml())
                    return
                }

                if (!isAuth()) {
                    ELogger.warn("Received authentication token is invalid")

                    logOut()

                    sendResponse(exchange, errorHtml())
                    return
                }

                ELogger.info("Authentication completed successfully")

                onAuthStateChanged?.invoke()

                sendResponse(exchange, successHtml())
            } catch (e: Exception) {
                ELogger.error("Failed to handle authentication callback: ${e.message}")
                sendResponse(exchange, errorHtml())
            }
        }

        private fun sendResponse(exchange: HttpExchange, html: String) {
            val response = html.toByteArray(Charsets.UTF_8)

            exchange.responseHeaders.set("Content-Type", "text/html; charset=UTF-8")
            exchange.sendResponseHeaders(200, response.size.toLong())

            exchange.responseBody.use {
                it.write(response)
            }
        }

        @Suppress("unused")
        fun getPort(): Int {
            return PORT
        }

        @Suppress("unused")
        fun stop() {
            server?.stop(0)
            server = null
        }

//        Статика

        private fun successHtml(): String {
            return """
        <!DOCTYPE html>
        <html lang="ru">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>ElysiaClient</title>
            <style>
                * {
                    box-sizing: border-box;
                }

                body {
                    margin: 0;
                    min-height: 100vh;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    background: #000b14;
                    color: #d6dce5;
                    font-family: Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
                }

                .container {
                    width: min(420px, calc(100% - 32px));
                    padding: 40px 32px;
                    text-align: center;
                    background: #071520;
                    border: 1px solid #172633;
                    border-radius: 14px;
                    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.35);
                }

                .icon {
                    width: 64px;
                    height: 64px;
                    margin: 0 auto 24px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    border-radius: 50%;
                    background: rgba(255, 51, 102, 0.12);
                    color: #ff3366;
                    font-size: 30px;
                }

                h1 {
                    margin: 0 0 12px;
                    font-size: 24px;
                    font-weight: 700;
                }

                p {
                    margin: 0;
                    color: #8f889e;
                    font-size: 15px;
                    line-height: 1.6;
                }

                .accent {
                    margin-top: 24px;
                    color: #ff3366;
                    font-size: 13px;
                    font-weight: 600;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="icon">✓</div>
                <h1>Авторизация успешна</h1>
                <p>
                    Вы успешно вошли в ElysiaClient.
                    Можете закрыть эту страницу и вернуться в приложение.
                </p>
                <div class="accent">ElysiaClient</div>
            </div>
        </body>
        </html>
    """.trimIndent()
        }

        private fun errorHtml(): String {
            return """
        <!DOCTYPE html>
        <html lang="ru">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>ElysiaClient</title>
            <style>
                * {
                    box-sizing: border-box;
                }

                body {
                    margin: 0;
                    min-height: 100vh;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    background: #000b14;
                    color: #d6dce5;
                    font-family: Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
                }

                .container {
                    width: min(420px, calc(100% - 32px));
                    padding: 40px 32px;
                    text-align: center;
                    background: #071520;
                    border: 1px solid #172633;
                    border-radius: 14px;
                    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.35);
                }

                .icon {
                    width: 64px;
                    height: 64px;
                    margin: 0 auto 24px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    border-radius: 50%;
                    background: rgba(255, 51, 102, 0.12);
                    color: #ff3366;
                    font-size: 30px;
                    font-weight: 700;
                }

                h1 {
                    margin: 0 0 12px;
                    font-size: 24px;
                    font-weight: 700;
                }

                p {
                    margin: 0;
                    color: #8f889e;
                    font-size: 15px;
                    line-height: 1.6;
                }

                .accent {
                    margin-top: 24px;
                    color: #ff3366;
                    font-size: 13px;
                    font-weight: 600;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="icon">!</div>
                <h1>Ошибка авторизации</h1>
                <p>
                    Не удалось выполнить авторизацию в ElysiaClient.
                    Вернитесь в приложение и попробуйте снова.
                </p>
                <div class="accent">ElysiaClient</div>
            </div>
        </body>
        </html>
    """.trimIndent()
        }
    }
}