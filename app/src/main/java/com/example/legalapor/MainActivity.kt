package com.example.legalapor

import android.icu.text.ListFormatter.Width
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.twotone.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
//    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val navigationItems = listOf(
        NavItem("Home", Icons.Filled.Home),
        NavItem("Forum", Icons.AutoMirrored.Outlined.Send),
        NavItem("Lapor", Icons.Filled.Add),
        NavItem("Profile", Icons.Filled.Person),
        NavItem("Settings", Icons.Filled.Settings)
    )

    Scaffold (
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(120.dp),
            ) {
                BottomNavBar(
                    items = navigationItems,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { index ->
                        selectedItemIndex = index
                    }
                )
            }
        },
        floatingActionButton = @Composable {
            FloatingActionButton(onClick = { /* TODO */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize())

    }
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier,
                     items: List<NavItem>,
                     selectedItemIndex: Int,
                     onItemSelected: (Int) -> Unit) {
    NavigationBar(modifier = modifier) {
        items.forEachIndexed { index, item ->
        NavigationBarItem(
            selected = selectedItemIndex == index,
            onClick = { onItemSelected(index) },
            icon = { Icon(item.icon, contentDescription = item.title) },
            label = { Text(item.title) }
        )
    }
}
    }

@Preview(showBackground = true)
@Composable
fun SimpleComposablePreview() {
    MainScreen()
}


data class NavItem(val title: String, val icon: ImageVector)

sealed class NavBarElement
data class NavItemData(val navItem: NavItem, val originalIndex: Int) : NavBarElement()
object SpacerData : NavBarElement()