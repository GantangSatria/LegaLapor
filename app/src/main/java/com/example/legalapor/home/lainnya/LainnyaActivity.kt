import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.legalapor.home.lainnya.components.ProfileInfoItem
import com.example.legalapor.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


@Composable
fun LainnyaScreen() {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    fun loadUserProfile(
        userId: String,
        onComplete: (UserModel?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                val user = doc.toObject(UserModel::class.java)
                onComplete(user)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    fun saveUserProfile(
        userId: String,
        user: UserModel,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("users").document(userId).set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }

    var userId by remember { mutableStateOf(auth.currentUser?.uid) }
    var userProfile by remember { mutableStateOf<UserModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isEditMode by remember { mutableStateOf(false) }

    var editableName by remember { mutableStateOf("") }
    var editableDob by remember { mutableStateOf("") }
    var editableFullPhoneNumber by remember { mutableStateOf("") }
    var editableGender by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        userId?.let {
            loadUserProfile(
                userId = it,
                onComplete = { profile ->
                    userProfile = profile
                    editableName = profile?.name ?: ""
                    editableDob = profile?.birthdate ?: ""
                    editableFullPhoneNumber = profile?.phoneNumber ?: ""
                    editableGender = profile?.gender ?: ""
                    isLoading = false
                },
                onError = {
                    isLoading = false
                }
            )
        }
    }

    val context = LocalContext.current

    fun uriToByteArray(uri: Uri, context: Context): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun uploadImageToSupabaseAndSaveUrl(
        userId: String,
        imageUri: Uri,
        context: Context,
        onComplete: (String) -> Unit
    ) {
        val byteArray = uriToByteArray(imageUri, context) ?: return
        val bucket = "user-image"
        val fileName = "${userId}_${System.currentTimeMillis()}.png"

        val client = OkHttpClient()
        val mediaType = "image/png".toMediaType()
        val requestBody = byteArray.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://qqwnyvosdtoosydrtdmx.supabase.co/storage/v1/object/$bucket/$fileName")
            .put(requestBody)
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFxd255dm9zZHRvb3N5ZHJ0ZG14Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgxNTkxOTAsImV4cCI6MjA2MzczNTE5MH0.ZqKrqbOJqPur4ebpFt9t9JjQ1vd7GlvHt8vAr3e63bg")
            .addHeader("Content-Type", "image/png")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val publicUrl = "https://qqwnyvosdtoosydrtdmx.supabase.co/storage/v1/object/public/$fileName"

                    FirebaseFirestore.getInstance().collection("users").document(userId)
                        .update("profileImageUrl", publicUrl)
                        .addOnSuccessListener { onComplete(publicUrl) }
                } else {
                    println("Upload failed: ${response.message}")
                }
            }
        })
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uploadImageToSupabaseAndSaveUrl(userId!!, it, context) { imageUrl ->
                userProfile = userProfile?.copy(profileImageUrl = imageUrl)
            }
        }
    }



    Scaffold(){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                if (userProfile?.profileImageUrl.isNullOrEmpty()) {
                    Image(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    )
                } else {
                    AsyncImage(
                        model = userProfile?.profileImageUrl,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }

                if (isEditMode) {
                    SmallFloatingActionButton(
                        onClick = { launcher.launch("image/*") },
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit Picture")
                    }
                }
            }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isEditMode) editableName else userProfile?.name ?: "Nama Pengguna",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userProfile?.email ?: "email@example.com",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Verified",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                ProfileInfoItem(
                    icon = Icons.Filled.Person,
                    label = "Nama Lengkap",
                    value = editableName,
                    onValueChange = { editableName = it },
                    isEditable = isEditMode
                )
                ProfileInfoItem(
                    icon = Icons.Filled.CalendarToday,
                    label = "Tanggal Lahir (DD/MM/YYYY)",
                    value = editableDob,
                    onValueChange = { editableDob = it },
                    isEditable = isEditMode,
                    keyboardType = KeyboardType.Number
                )
                ProfileInfoItem(
                    icon = Icons.Filled.Email,
                    label = "Email",
                    value = userProfile?.email ?: "",
                    onValueChange = { /* Email not editable */ },
                    isEditable = false
                )

                ProfileInfoItem(
                    icon = Icons.Filled.Phone,
                    label = "Nomor Telepon (e.g. +628123...)",
                    value = editableFullPhoneNumber,
                    onValueChange = { editableFullPhoneNumber = it },
                    isEditable = isEditMode,
                    keyboardType = KeyboardType.Phone
                )
                ProfileInfoItem(
                    icon = Icons.Filled.Wc,
                    label = "Jenis Kelamin",
                    value = editableGender,
                    onValueChange = { editableGender = it },
                    isEditable = isEditMode,
                    trailingIcon = if (!isEditMode) Icons.Filled.ChevronRight else null,
                )

//                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (isEditMode) {

                                isLoading = true
                                val updatedProfile = userProfile?.copy(
                                    name = editableName,
                                    birthdate = editableDob,
                                    phoneNumber = editableFullPhoneNumber,
                                    gender = editableGender
                                )
                                if (updatedProfile != null && userId != null) {
                                    saveUserProfile(
                                        userId = userId!!,
                                        user = updatedProfile,
                                        onSuccess = {
                                            userProfile = updatedProfile
                                            isEditMode = false
                                            isLoading = false
                                        },
                                        onError = {
                                            isLoading = false
                                        }
                                    )
                                }

                        } else {
                            userProfile?.let {
                                editableName = it.name
                                editableDob = it.birthdate
                                editableFullPhoneNumber = it.phoneNumber
                                editableGender = it.gender
                            }
                            isEditMode = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isLoading || isEditMode
                ) {
                    if (isLoading && isEditMode) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            if (isEditMode) "Simpan Perubahan" else "Edit Profil",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

            }
        }
    }
}

@Preview
@Composable
fun LainnyaScreenPreview() {
    LainnyaScreen()
}