package dev.elysia.elysiaclient.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.theme.ElysiaAccent
import dev.elysia.elysiaclient.theme.ElysiaBackground
import dev.elysia.elysiaclient.theme.ElysiaMuted
import dev.elysia.elysiaclient.theme.ElysiaSurface
import dev.elysia.elysiaclient.theme.ElysiaText
import dev.elysia.elysiaclient.theme.ElysiaTextSecondary
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen

@Composable
fun AuthLoadingPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ElysiaBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = ElysiaAccent,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Проверяем авторизацию",
                color = ElysiaText,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Подождите немного...",
                color = ElysiaMuted,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AuthPage(
    onAuthorize: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ElysiaBackground),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(420.dp)
                .background(
                    ElysiaAccent.copy(alpha = 0.035f),
                    shape = RoundedCornerShape(210.dp)
                )
        )

        Surface(
            modifier = Modifier
                .padding(24.dp),
            shape = RoundedCornerShape(18.dp),
            color = ElysiaSurface,
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .width(390.dp)
                    .padding(
                        horizontal = 42.dp,
                        vertical = 40.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Elysia",
                            color = ElysiaText,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.8).sp
                        )

                        Text(
                            text = "Client",
                            color = ElysiaAccent,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.8).sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(3.dp)
                            .background(
                                ElysiaAccent,
                                RoundedCornerShape(2.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Добро пожаловать",
                    color = ElysiaText,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Авторизуйтесь, чтобы продолжить",
                    color = ElysiaTextSecondary,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Используйте свой аккаунт ElysiaID для доступа к серверам, профилю и функциям клиента.",
                    color = ElysiaMuted,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = onAuthorize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElysiaAccent,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(9.dp))

                    Text(
                        text = "Авторизоваться",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Откроется браузер для безопасной авторизации",
                    color = ElysiaMuted,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}