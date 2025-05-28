package com.example.legalapor.home.forum.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.legalapor.models.UserModel

@Composable
fun CreatePostSection(user: UserModel, onPost: (String) -> Unit) {
    var content by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Buat Pertanyaan", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(11.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("Tanya kasusmu di Komunitas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (content.isNotBlank()) {
                        onPost(content)
                        content = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A5FD7)),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Publish", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun CreatePostSectionPreview() {
    val user = UserModel(
        uid = "123",
        name = "dion"
    )
    CreatePostSection(user = user, onPost = {})
}