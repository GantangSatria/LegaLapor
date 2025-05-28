@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.legalapor.R
import com.example.legalapor.home.forum.components.CreatePostSection
import com.example.legalapor.home.forum.components.PostItem
import com.example.legalapor.models.PostModel
import com.example.legalapor.models.UserModel
import com.example.legalapor.service.viewmodel.PostViewModel

//data class ForumPost(
//    val id: Int,
//    val authorName: String,
//    val authorImage: Int,
//    val timeAgo: String,
//    val content: String,
//    val images: List<Int> = emptyList(),
//    val likes: Int,
//    val comments: Int,
//    val isLiked: Boolean = false
//)

@Composable
fun ForumScreen(viewModel: PostViewModel = viewModel(), user: UserModel) {
    val posts by viewModel.posts.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        CreatePostSection(user = user, onPost = { content ->
            val post = PostModel(
                authorId = user.uid,
                authorName = user.name,
                authorImageUrl = user.profileImageUrl,
                content = content
            )
            viewModel.addPost(post)
        })

        if (posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Belum ada postingan")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(posts) { post ->
                    PostItem(post = post, user = user, viewModel = viewModel)
                }
            }
        }
    }
}

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        // Header Section
////        HeaderSection()
//
//        // Create Post Section
//        CreatePostSection()
//
//        // Posts List
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//        ) {
//            items(samplePosts) { post ->
//                PostItem(post = post)
//            }
//        }
//    }
//}

//@Composable
//fun CreatePostSection() {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = "Buat Pertanyaan",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.padding(bottom = 12.dp)
//            )
//
//            OutlinedTextField(
//                value = "",
//                onValueChange = { },
//                placeholder = {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Outlined.Message,
//                            contentDescription = "Message",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(16.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "Tanya kasusmu di Komunitas",
//                            color = Color.Gray
//                        )
//                    }
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent
//                ),
//                shape = RoundedCornerShape(8.dp),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                OutlinedButton(
//                    onClick = { },
//                    colors = ButtonDefaults.outlinedButtonColors(
//                        contentColor = Color.Black
//                    ),
//                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text("Galeri")
//                }
//
//                Button(
//                    onClick = { },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF4A5FD7)
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text("Publish", color = Color.White)
//                }
//            }
//        }
//    }
//}

//@Composable
//fun PostItem(post: ForumPost) {
//    var isLiked by remember { mutableStateOf(post.isLiked) }
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            // Author Header
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(bottom = 12.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = post.authorImage),
//                    contentDescription = "Profile Picture",
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                Column {
//                    Text(
//                        text = post.authorName,
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 16.sp
//                    )
//                    Text(
//                        text = post.timeAgo,
//                        color = Color.Gray,
//                        fontSize = 12.sp
//                    )
//                }
//            }
//
//            // Post Content
//            Text(
//                text = post.content,
//                fontSize = 14.sp,
//                lineHeight = 20.sp,
//                modifier = Modifier.padding(bottom = 12.dp)
//            )
//
//            // Images (if any)
//            if (post.images.isNotEmpty()) {
//                Row(
//                    modifier = Modifier.padding(bottom = 12.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    post.images.forEach { imageRes ->
//                        Image(
//                            painter = painterResource(id = imageRes),
//                            contentDescription = "Post Image",
//                            modifier = Modifier
//                                .size(120.dp)
//                                .clip(RoundedCornerShape(8.dp)),
//                            contentScale = ContentScale.Crop
//                        )
//                    }
//                }
//            }
//
//            // Interaction Row
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Like Button
//                    IconButton(
//                        onClick = { isLiked = !isLiked }
//                    ) {
//                        Icon(
//                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
//                            contentDescription = "Like",
//                            tint = if (isLiked) Color.Red else Color.Gray
//                        )
//                    }
//                    Text(
//                        text = "${post.likes} Likes",
//                        color = Color.Gray,
//                        fontSize = 12.sp
//                    )
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    // Comment
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Outlined.Message,
//                            contentDescription = "Comment",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(
//                            text = "${post.comments} Komen",
//                            color = Color.Gray,
//                            fontSize = 12.sp
//                        )
//                    }
//                }
//
//                // Share Button
//                Button(
//                    onClick = { },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF4A5FD7)
//                    ),
//                    shape = RoundedCornerShape(20.dp),
//                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Share,
//                        contentDescription = "Share",
//                        modifier = Modifier.size(16.dp),
//                        tint = Color.White
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "Share",
//                        color = Color.White,
//                        fontSize = 12.sp
//                    )
//                }
//            }
//        }
//    }
//}

//@Preview(showBackground = true, widthDp = 400)
//@Composable
//fun HeaderSectionPreview() {
//    MaterialTheme {
//        HeaderSection()
//    }
//}