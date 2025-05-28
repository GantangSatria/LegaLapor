package com.example.legalapor

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.legalapor.models.UserModel
import com.example.legalapor.navigation.NavRoutes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase



// Data class untuk menyimpan state form
data class AuthFormState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val fullNameError: String? = null,
    val phoneNumberError: String? = null
)

@Composable
fun AuthScreen(navController: NavHostController) {
    val primaryPurpleColor = Color(0xff31469f)
    val logoBrandBlue = Color(0xFF3D5AFE)
    val logoBrandTeal = Color(0xFF00BFA5)
    val logoBrandTextBlue = Color(0xFF4FC3F7)
    val lightGrayBackground = Color(0xFFF5F5F5)
    val footerColor = primaryPurpleColor

    var selectedTabIndex by remember { mutableStateOf(0) }
    var authState by remember { mutableStateOf(AuthFormState()) }
    val context = LocalContext.current
    val auth = Firebase.auth

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGrayBackground)
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

            // Tab untuk Login/Signup
            LoginRegisterTabs(
                primaryPurpleColor,
                selectedTabIndex
            ) { index -> selectedTabIndex = index }

            Spacer(modifier = Modifier.height(24.dp))

            // Form berdasarkan tab yang dipilih
            if (selectedTabIndex == 0) {
                // Login Form
                LoginForm(
                    authState = authState,
                    primaryColor = primaryPurpleColor,
                    onStateChange = { authState = it },
                    onLogin = { email, password ->
                        authState = authState.copy(isLoading = true)
                        signInWithEmail(auth, email, password, context) { success ->
                            authState = authState.copy(isLoading = false)
                            if (success) {
                                Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                navController.navigate(NavRoutes.Beranda.route) {
                                    popUpTo(NavRoutes.Auth.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            } else {
                // Signup Form
                SignupForm(
                    authState = authState,
                    primaryColor = primaryPurpleColor,
                    onStateChange = { authState = it },
                    onSignup = { email, password, fullName, phoneNumber ->
                        authState = authState.copy(isLoading = true)
                        signUpWithEmail(auth, email, password, fullName, phoneNumber, context) { success ->
                            authState = authState.copy(isLoading = false)
                            if (success) {
                                Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                                selectedTabIndex = 0
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            OrContinueDivider()
            Spacer(modifier = Modifier.height(20.dp))
            SocialLoginOptions(auth, context)
            Spacer(modifier = Modifier.height(15.dp))
            TermsAndPolicyText()
            Spacer(modifier = Modifier.height(24.dp))
        }
        Footer(footerColor)
    }
}

@Composable
fun LoginForm(
    authState: AuthFormState,
    primaryColor: Color,
    onStateChange: (AuthFormState) -> Unit,
    onLogin: (String, String) -> Unit
) {
    Column {
        EmailInput(
            value = authState.email,
            onValueChange = { onStateChange(authState.copy(email = it, emailError = null)) },
            focusColor = primaryColor,
            error = authState.emailError
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordInput(
            value = authState.password,
            onValueChange = { onStateChange(authState.copy(password = it, passwordError = null)) },
            focusColor = primaryColor,
            label = "Kata Sandi",
            error = authState.passwordError
        )

        Spacer(modifier = Modifier.height(10.dp))
        ForgotPasswordLink(primaryColor)
        Spacer(modifier = Modifier.height(24.dp))

        SignInButton(
            buttonColor = primaryColor,
            isLoading = authState.isLoading,
            onClick = {
                if (validateLoginForm(authState, onStateChange)) {
                    onLogin(authState.email, authState.password)
                }
            }
        )
    }
}

@Composable
fun SignupForm(
    authState: AuthFormState,
    primaryColor: Color,
    onStateChange: (AuthFormState) -> Unit,
    onSignup: (String, String, String, String) -> Unit
) {
    Column {
        FullNameInput(
            value = authState.fullName,
            onValueChange = { onStateChange(authState.copy(fullName = it, fullNameError = null)) },
            focusColor = primaryColor,
            error = authState.fullNameError
        )

        Spacer(modifier = Modifier.height(16.dp))

        PhoneNumberInput(
            value = authState.phoneNumber,
            onValueChange = { onStateChange(authState.copy(phoneNumber = it, phoneNumberError = null)) },
            focusColor = primaryColor,
            error = authState.phoneNumberError
        )

        Spacer(modifier = Modifier.height(16.dp))

        EmailInput(
            value = authState.email,
            onValueChange = { onStateChange(authState.copy(email = it, emailError = null)) },
            focusColor = primaryColor,
            error = authState.emailError
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordInput(
            value = authState.password,
            onValueChange = { onStateChange(authState.copy(password = it, passwordError = null)) },
            focusColor = primaryColor,
            label = "Kata Sandi",
            error = authState.passwordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordInput(
            value = authState.confirmPassword,
            onValueChange = { onStateChange(authState.copy(confirmPassword = it, confirmPasswordError = null)) },
            focusColor = primaryColor,
            label = "Konfirmasi Kata Sandi",
            error = authState.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(24.dp))

        SignUpButton(
            buttonColor = primaryColor,
            isLoading = authState.isLoading,
            onClick = {
                if (validateSignupForm(authState, onStateChange)) {
                    onSignup(authState.email, authState.password, authState.fullName, authState.phoneNumber)
                }
            }
        )
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
fun LoginRegisterTabs(
    selectedColor: Color,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
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
                    height = 2.5.dp
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
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
        Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
    }
}

@Composable
fun EmailInput(
    value: String,
    onValueChange: (String) -> Unit,
    focusColor: Color,
    error: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusColor,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = focusColor,
                focusedLabelColor = focusColor,
                unfocusedLabelColor = Color.Gray,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    focusColor: Color,
    label: String,
    error: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon", tint = Color.Gray) },
            trailingIcon = {
                // Perbaikan: Gunakan icon mata yang benar
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description, tint = Color.Gray)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusColor,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = focusColor,
                focusedLabelColor = focusColor,
                unfocusedLabelColor = Color.Gray,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red
            ),
            shape = RoundedCornerShape(8.dp)
        )
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun FullNameInput(
    value: String,
    onValueChange: (String) -> Unit,
    focusColor: Color,
    error: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Nama Lengkap") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Name Icon", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusColor,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = focusColor,
                focusedLabelColor = focusColor,
                unfocusedLabelColor = Color.Gray,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red
            ),
            shape = RoundedCornerShape(8.dp)
        )
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PhoneNumberInput(
    value: String,
    onValueChange: (String) -> Unit,
    focusColor: Color,
    error: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Nomor Telepon") },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Phone Icon", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusColor,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = focusColor,
                focusedLabelColor = focusColor,
                unfocusedLabelColor = Color.Gray,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red
            ),
            shape = RoundedCornerShape(8.dp)
        )
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
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
            .padding(vertical = 4.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
fun SignInButton(
    buttonColor: Color,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        shape = RoundedCornerShape(8.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text("Masuk", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SignUpButton(
    buttonColor: Color,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        shape = RoundedCornerShape(8.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text("Daftar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
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
            text = "atau lanjutkan dengan",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
    }
}

@Composable
fun SocialLoginOptions(auth: FirebaseAuth, context: android.content.Context) {
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
                .background(Color.White)
                .border(BorderStroke(1.dp, Color.LightGray), CircleShape)
                .clickable {
                    // Handle Google login
                    Toast.makeText(context, "Google login akan diimplementasikan", Toast.LENGTH_SHORT).show()
                }
        ) {
            Image(
                painter = painterResource(R.drawable.google_logo_graphic),
                contentDescription = null,
                modifier = Modifier.size(33.dp),
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Apple Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    // Handle Apple login
                    Toast.makeText(context, "Apple login akan diimplementasikan", Toast.LENGTH_SHORT).show()
                }
        ) {
            Image(
                painter = painterResource(R.drawable.apple_logo_graphic),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
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
        modifier = Modifier.padding(horizontal = 16.dp),
        lineHeight = 16.sp
    )
}

@Composable
fun Footer(backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "© All Rights Reserved",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 12.sp
        )
    }
}

// Fungsi validasi
fun validateLoginForm(state: AuthFormState, onStateChange: (AuthFormState) -> Unit): Boolean {
    var isValid = true
    var newState = state

    if (state.email.isBlank()) {
        newState = newState.copy(emailError = "Email tidak boleh kosong")
        isValid = false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
        newState = newState.copy(emailError = "Format email tidak valid")
        isValid = false
    }

    if (state.password.isBlank()) {
        newState = newState.copy(passwordError = "Kata sandi tidak boleh kosong")
        isValid = false
    } else if (state.password.length < 8) {
        newState = newState.copy(passwordError = "Kata sandi minimal 6 karakter")
        isValid = false
    }

    onStateChange(newState)
    return isValid
}

fun validateSignupForm(state: AuthFormState, onStateChange: (AuthFormState) -> Unit): Boolean {
    var isValid = true
    var newState = state

    if (state.fullName.isBlank()) {
        newState = newState.copy(fullNameError = "Nama lengkap tidak boleh kosong")
        isValid = false
    }

    if (state.phoneNumber.isBlank()) {
        newState = newState.copy(phoneNumberError = "Nomor telepon tidak boleh kosong")
        isValid = false
    }

    if (state.email.isBlank()) {
        newState = newState.copy(emailError = "Email tidak boleh kosong")
        isValid = false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
        newState = newState.copy(emailError = "Format email tidak valid")
        isValid = false
    }

    if (state.password.isBlank()) {
        newState = newState.copy(passwordError = "Kata sandi tidak boleh kosong")
        isValid = false
    } else if (state.password.length < 8) {
        newState = newState.copy(passwordError = "Kata sandi minimal 8 karakter")
        isValid = false
    } else if (!state.password.any { it.isUpperCase() }) {
        newState = newState.copy(passwordError = "Kata sandi harus memiliki setidaknya satu huruf kapital")
        isValid = false
    } else if (!state.password.any { it.isDigit() }) {
        newState = newState.copy(passwordError = "Kata sandi harus memiliki setidaknya satu angka")
        isValid = false
    }

    if (state.confirmPassword.isBlank()) {
        newState = newState.copy(confirmPasswordError = "Konfirmasi kata sandi tidak boleh kosong")
        isValid = false
    } else if (state.password != state.confirmPassword) {
        newState = newState.copy(confirmPasswordError = "Kata sandi tidak cocok")
        isValid = false
    }

    onStateChange(newState)
    return isValid
}

// Fungsi autentikasi Firebase
fun signInWithEmail(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: android.content.Context,
    onComplete: (Boolean) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true)
            } else {
                Toast.makeText(context, "Login gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                onComplete(false)
            }
        }
}

fun signUpWithEmail(
    auth: FirebaseAuth,
    email: String,
    password: String,
    name: String,
    phoneNumber: String,
    context: android.content.Context,
    onComplete: (Boolean) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = task.result?.user?.uid ?: return@addOnCompleteListener

                val user = UserModel(
                    uid = uid,
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber
                )

                db.collection("users").document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Gagal simpan data user: ${e.message}", Toast.LENGTH_LONG).show()
                        onComplete(false)
                    }
            } else {
                Toast.makeText(context, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                onComplete(false)
            }
        }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, backgroundColor = 0xFFCCCCCC)
@Composable
fun AuthScreenPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AuthScreen(navController = rememberNavController())
            }
        }
    }
}