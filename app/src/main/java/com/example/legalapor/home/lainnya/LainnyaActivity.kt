import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.example.legalapor.R

// --- Color Palette ---
val ProfileBlue = Color(0xFF4C5FBF)
val ProfileLightGrayBg = Color(0xFFF5F5F5)
val ProfileIconBlue = Color(0xFF4C5FBF)
val ProfileTextDark = Color(0xFF333333)
val ProfileTextLight = Color(0xFF666666)
val ProfileCardBorder = Color(0xFFE0E0E0)

// Data class untuk menyimpan data user
data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val gender: String = ""
)

@Composable
fun ProfileNavigationHost() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(
        navController = navController,
        startDestination = "profile_main"
    ) {
        composable("profile_main") {
            ProfileScreenPage1(navController = navController, auth = auth)
        }
        composable("profile_settings") {
            ProfileSettingsScreen(navController = navController, auth = auth)
        }
        composable("language_settings") {
            LanguageSettingsScreen(navController = navController)
        }
        composable("help_screen") {
            HelpScreen(navController = navController)
        }
    }
}

@Composable
fun ProfileScreenPage1(navController: NavController, auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileLightGrayBg)
    ) {
        ProfileHeader()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-40).dp)
        ) {
            ProfileCard(currentUser = currentUser)
        }

        Spacer(modifier = Modifier.height(8.dp))
        SettingsMenu(
            onProfileSettingsClick = { navController.navigate("profile_settings") },
            onLanguageClick = { navController.navigate("language_settings") },
            onHelpClick = { navController.navigate("help_screen") },
            onLogoutClick = {
                coroutineScope.launch {
                    auth.signOut()
                    // Navigate to login screen
                }
            }
        )
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                ProfileBlue,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Lainnya",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Composable
fun ProfileCard(currentUser: FirebaseUser?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Image(
                painter = painterResource(R.drawable.chris_james),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = currentUser?.displayName ?: "Christ James",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ProfileTextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Email
            Text(
                text = currentUser?.email ?: "christjames@gmail.com",
                fontSize = 14.sp,
                color = ProfileTextLight
            )

            Spacer(modifier = Modifier.height(24.dp))

            ActionButtonsRow()
        }
    }
}

@Composable
fun ActionButtonsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(icon = Icons.Outlined.Assignment, label = "Aktivitas")
        ActionButton(icon = Icons.Outlined.Assessment, label = "Laporan")
        ActionButton(icon = Icons.Outlined.Message, label = "Pesan")
        ActionButton(icon = Icons.Outlined.Star, label = "Penilaian")
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* TODO: Handle action */ }
    ) {
        Surface(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
            color = Color(0xFFF8F9FF),
            border = BorderStroke(1.dp, Color(0xFFE3E7FF))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = ProfileIconBlue,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = ProfileTextDark,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsMenu(
    onProfileSettingsClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            SettingsMenuItem(
                icon = Icons.Outlined.Settings,
                text = "Pengaturan Profil",
                onClick = onProfileSettingsClick
            )
            Divider(color = ProfileLightGrayBg, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsMenuItem(
                icon = Icons.Outlined.Language,
                text = "Bahasa",
                onClick = onLanguageClick
            )
            Divider(color = ProfileLightGrayBg, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsMenuItem(
                icon = Icons.Outlined.HelpOutline,
                text = "Bantuan",
                onClick = onHelpClick
            )
            Divider(color = ProfileLightGrayBg, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingsMenuItem(
                icon = Icons.AutoMirrored.Outlined.Logout,
                text = "Keluar",
                isLogout = true,
                onClick = onLogoutClick
            )
        }
    }
}

@Composable
fun SettingsMenuItem(icon: ImageVector, text: String, isLogout: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (isLogout) Color.Red.copy(alpha = 0.8f) else ProfileIconBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isLogout) Color.Red.copy(alpha = 0.8f) else ProfileTextDark,
            modifier = Modifier.weight(1f)
        )
        if (!isLogout) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go to $text",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Profile Settings Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(navController: NavController, auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    var userProfile by remember { mutableStateOf(UserProfile()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Initialize with current user data
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            userProfile = userProfile.copy(
                name = user.displayName ?: "",
                email = user.email ?: "",
                phone = user.phoneNumber ?: ""
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileLightGrayBg)
    ) {
        // Header with back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    ProfileBlue,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Pengaturan Profil",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Image(
                            painter = painterResource(R.drawable.chris_james),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd)
                                .clickable { /* TODO: Change profile picture */ },
                            shape = CircleShape,
                            color = ProfileBlue
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile Picture",
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Information Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Name Field
                    OutlinedTextField(
                        value = userProfile.name,
                        onValueChange = { userProfile = userProfile.copy(name = it) },
                        label = { Text("Christ James") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Name"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Birth Date Field
                    OutlinedTextField(
                        value = userProfile.birthDate.ifEmpty { "01/01/1998" },
                        onValueChange = { },
                        label = { Text("Tanggal Lahir") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Birth Date"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select Date"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Field
                    OutlinedTextField(
                        value = userProfile.email,
                        onValueChange = { userProfile = userProfile.copy(email = it) },
                        label = { Text("christjames@gmail.com") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phone Number Field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        OutlinedTextField(
                            value = "+62",
                            onValueChange = { },
                            modifier = Modifier.width(80.dp),
                            readOnly = true,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = userProfile.phone.removePrefix("+62"),
                            onValueChange = { userProfile = userProfile.copy(phone = "+62$it") },
                            label = { Text("812-8888-1111") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gender Field
                    OutlinedTextField(
                        value = userProfile.gender.ifEmpty { "Laki-laki" },
                        onValueChange = { },
                        label = { Text("Jenis Kelamin") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Gender"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showGenderDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Gender"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        // TODO: Save profile data to Firebase
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ProfileBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Edit Profil",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // Gender Selection Dialog
    if (showGenderDialog) {
        AlertDialog(
            onDismissRequest = { showGenderDialog = false },
            title = { Text("Pilih Jenis Kelamin") },
            text = {
                Column {
                    listOf("Laki-laki", "Perempuan").forEach { gender ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    userProfile = userProfile.copy(gender = gender)
                                    showGenderDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = userProfile.gender == gender,
                                onClick = {
                                    userProfile = userProfile.copy(gender = gender)
                                    showGenderDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = gender)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGenderDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

// Language Settings Screen
@Composable
fun LanguageSettingsScreen(navController: NavController) {
    var selectedLanguage by remember { mutableStateOf("Bahasa Indonesia") }
    val languages = listOf(
        "Bahasa Indonesia" to "Bahasa Indonesia",
        "English" to "Pilih bahasa alternatif 1",
        "Other" to "Pilih bahasa alternatif 2"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileLightGrayBg)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    ProfileBlue,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Bahasa",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                Text(
                    text = "Pengaturan Bahasa",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(20.dp)
                )

                Text(
                    text = "Bahasa Utama",
                    fontSize = 14.sp,
                    color = ProfileTextLight,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                languages.forEachIndexed { index, (key, value) ->
                    LanguageOption(
                        primaryText = key,
                        secondaryText = value,
                        isSelected = selectedLanguage == key,
                        onClick = { selectedLanguage = key }
                    )
                    if (index < languages.size - 1) {
                        Divider(
                            color = ProfileLightGrayBg,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Konfirmasi")
                }
            }
        }
    }
}

@Composable
fun LanguageOption(
    primaryText: String,
    secondaryText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = primaryText,
                fontSize = 16.sp,
                color = ProfileTextDark
            )
            if (secondaryText != primaryText) {
                Text(
                    text = secondaryText,
                    fontSize = 14.sp,
                    color = ProfileTextLight
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Select",
            tint = if (isSelected) ProfileBlue else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

// Help Screen
@Composable
fun HelpScreen(navController: NavController) {
    val helpItems = listOf(
        "Laporkan Masalah" to Icons.Outlined.ReportProblem,
        "Status Akun" to Icons.Outlined.AccountCircle,
        "Help Center" to Icons.Outlined.Help,
        "Privasi dan Keamanan" to Icons.Outlined.Security,
        "Dukungan" to Icons.Outlined.Support
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileLightGrayBg)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    ProfileBlue,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Bantuan",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                helpItems.forEachIndexed { index, (title, icon) ->
                    HelpMenuItem(
                        icon = icon,
                        text = title,
                        onClick = { /* TODO: Handle help item click */ }
                    )
                    if (index < helpItems.size - 1) {
                        Divider(
                            color = ProfileLightGrayBg,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HelpMenuItem(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = ProfileIconBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = ProfileTextDark,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go to $text",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileNavigationPreview() {
    MaterialTheme {
        ProfileNavigationHost()
    }
}