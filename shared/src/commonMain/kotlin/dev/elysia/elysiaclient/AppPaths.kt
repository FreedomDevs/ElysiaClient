package dev.elysia.elysiaclient

import java.nio.file.Path

object AppPaths {
    val executableDirectory: Path
        get() = when {
            System.getProperty("os.name").startsWith("Windows") -> {
                Path.of(
                    System.getenv("APPDATA"),
                    "ElysiaClient"
                )
            }

            System.getProperty("os.name").startsWith("Mac") -> {
                Path.of(
                    System.getProperty("user.home"),
                    "Library",
                    "Application Support",
                    "ElysiaClient"
                )
            }

            else -> {
                Path.of(
                    System.getenv("XDG_DATA_HOME")
                        ?: Path.of(System.getProperty("user.home"), ".local", "share").toString(),
                    "ElysiaClient"
                )
            }
        }

    val pluginsDirectory: Path
        get() = executableDirectory.resolve("plugins")
}