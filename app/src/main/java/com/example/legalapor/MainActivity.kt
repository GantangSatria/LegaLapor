package com.example.legalapor

import android.icu.text.ListFormatter.Width
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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

    val actualSelectableNavItems = listOf(
        NavItem("Home", Icons.Filled.Home),
        NavItem("Forum", Icons.AutoMirrored.Outlined.Send),
        NavItem("Riwayat", Icons.Filled.Person),
        NavItem("Lainnya", Icons.Filled.Settings)
    )

    val navigationItems: List<NavBarElement> = listOf(
        NavItemData(actualSelectableNavItems[0], 0), // Home
        NavItemData(actualSelectableNavItems[1], 1), // Forum
        SpacerData,
        NavItemData(actualSelectableNavItems[2], 2), // Profile
        NavItemData(actualSelectableNavItems[3], 3)  // Settings
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
            FloatingActionButton(modifier = Modifier.offset(y = 70.dp),
                shape = CircleShape,
                onClick = { /* TODO */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(),
//            contentAlignment = Alignment.Center
        )
    }
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier,
                     items: List<NavBarElement>,
                     selectedItemIndex: Int,
                     onItemSelected: (Int) -> Unit) {
    NavigationBar(modifier = modifier.height(IntrinsicSize.Min)) {
        items.forEach { element ->
            when (element) {
                is NavItemData -> {
                    NavigationBarItem(
                        selected = selectedItemIndex == element.originalIndex,
                        onClick = { onItemSelected(element.originalIndex) },
                        icon = { Icon(element.navItem.icon, contentDescription = element.navItem.title) },
                        label = { Text(element.navItem.title) }
                    )
                }
                is SpacerData -> {
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 12.dp)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier.height(35.dp))
                        Text(
                            text = "Lapor",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
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