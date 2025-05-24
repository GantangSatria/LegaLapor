package com.example.legalapor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}

@Composable
fun LoginScreenContent() {
    val primaryPurpleColor = Color(0xff31469f) // Main interactive elements purple
    val logoBrandBlue = Color(0xFF3D5AFE)     // Blue part of the logo graphic
    val logoBrandTeal = Color(0xFF00BFA5)     // Teal part of the logo graphic
    val logoBrandTextBlue = Color(0xFF4FC3F7) // "LEGALAPOR" text under logo graphic

    val lightGrayBackground = Color(0xFFF5F5F5)
    val footerColor = primaryPurpleColor

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGrayBackground) // This background is for the content inside the rounded box
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            AppLogo(logoBrandBlue, logoBrandTeal, logoBrandTextBlue)
            Spacer(modifier = Modifier.height(14.dp))
            WelcomeMessage(primaryPurpleColor)
            Spacer(modifier = Modifier.height(24.dp))
            LoginRegisterTabs(primaryPurpleColor)
            Spacer(modifier = Modifier.height(24.dp))
            EmailInput(primaryPurpleColor)
            Spacer(modifier = Modifier.height(16.dp))
            PasswordInput(primaryPurpleColor)
            Spacer(modifier = Modifier.height(10.dp))
            ForgotPasswordLink(primaryPurpleColor)
            Spacer(modifier = Modifier.height(24.dp))
            SignInButton(primaryPurpleColor)
            Spacer(modifier = Modifier.height(24.dp))
            OrContinueDivider()
            Spacer(modifier = Modifier.height(20.dp))
            SocialLoginOptions()
            Spacer(modifier = Modifier.height(15.dp))
            TermsAndPolicyText()
            Spacer(modifier = Modifier.height(24.dp)) // Ensures content is pushed up from footer
        }
        Footer(footerColor)
    }
}

@Composable
fun AppLogo(
    figureBlue: Color,
    figureTeal: Color,
    textBlue: Color
) {
    Image(
        painter = painterResource(R.drawable.lega_lapor_logo),
        contentDescription = null,
        modifier = Modifier.size(150.dp),
    )
}

@Composable
fun WelcomeMessage(highlightColor: Color) {
    Text(
        text = buildAnnotatedString {
            append("Selamat Datang di ")
            withStyle(style = SpanStyle(color = Color(49,70,159), fontWeight = FontWeight.Bold)) {
                append("Legalapor")
            }
        },
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Daftar atau masuk untuk memulai aksi\ndalam hukum",
        fontSize = 15.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp
    )
}

@Composable
fun LoginRegisterTabs(selectedColor: Color) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Masuk", "Daftar Akun")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Transparent,
            contentColor = selectedColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = selectedColor,
                    height = 2.5.dp // Slightly thicker indicator
                )
            },
            divider = {} // Remove default TabRow divider
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            title,
                            color = if (selectedTabIndex == index) selectedColor else Color.Gray,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                )
            }
        }
        Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp) // Full width divider
    }
}

@Composable
fun EmailInput(focusColor: Color) {
    var email by remember { mutableStateOf("") }
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon", tint = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = focusColor,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = focusColor,
            focusedLabelColor = focusColor,
            unfocusedLabelColor = Color.Gray
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun PasswordInput(focusColor: Color) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Kata Sandi") },
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon", tint = Color.Gray) },
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else {
                Icons.Filled.VisibilityOff
            }
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description, tint = Color.Gray)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = focusColor,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = focusColor,
            focusedLabelColor = focusColor,
            unfocusedLabelColor = Color.Gray
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

    @Composable
    fun ForgotPasswordLink(linkColor: Color) {
        Text(
            text = "Lupa kata sandi?",
            fontSize = 14.sp,
            color = linkColor,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Handle forgot password */ }
                .padding(vertical = 4.dp), // Add padding for easier click
            textAlign = TextAlign.Start
        )
}

@Composable
fun SignInButton(buttonColor: Color) {
    Button(
        onClick = { /* Handle login */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Masuk", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OrContinueDivider() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
        Text(
            text = "atau lanjutkan dengan email", // As per image
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
    }
}

@Composable
fun SocialLoginOptions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Google Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White) // Google button often has white background
                .border(BorderStroke(1.dp, Color.LightGray), CircleShape)
                .clickable { /* Google login */ }
        ) {
            // In a real app: Image(painter = painterResource(id = R.drawable.ic_google_logo), ...)
            // Using a simple text "G" as placeholder.
           Image(
               painter = painterResource(R.drawable.google_logo_graphic),
               contentDescription = null,
               modifier = Modifier.size(33.dp),
           )
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Apple Button - Fixed to use Material Icons instead of non-existent resource
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { /* Apple login */ }
        ) {
            // Using a simple "A" as placeholder since we don't have Apple logo resource
            Image(
                painter = painterResource(R.drawable.apple_logo_graphic),
                contentDescription = null,
                modifier = Modifier.size(60.dp))
        }
    }
}

@Composable
fun TermsAndPolicyText() {
    Text(
        text = buildAnnotatedString {
            append("By continuing, you agree to ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.DarkGray)) {
                append("Terms of Use")
            }
            append(" and ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.DarkGray)) {
                append("Privacy Policy")
            }
        },
        fontSize = 12.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp),
        lineHeight = 16.sp
    )
}

@Composable
fun Footer(backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 16.dp), // Slightly more padding
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "© All Rights Reserved", // Fixed typo from "Right" to "Rights"
            color = Color.White.copy(alpha = 0.9f), // Slightly transparent white
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, backgroundColor = 0xFFCCCCCC)
@Composable
fun LoginScreenPreview() {
    MaterialTheme { // Apply MaterialTheme for consistent styling
        // This Box simulates the device screen or a card with rounded corners.
        Box(
            modifier = Modifier
                .fillMaxSize() // Takes full preview space
                .padding(0.dp) // Example padding if it were a card on a different background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // Content fills this inner box
                    .clip(RoundedCornerShape(20.dp)) // Simplified rounded corners
            ) {
                LoginScreenContent()
            }
        }
    }
}