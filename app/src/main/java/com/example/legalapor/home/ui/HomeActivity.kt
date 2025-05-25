package com.example.legalapor.home.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.legalapor.home.beranda.ui.BerandaScreen
import com.example.legalapor.home.ui.components.CustomTopAppBar
import com.example.legalapor.home.ui.theme.LegaLaporTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
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
        topBar = {
            CustomTopAppBar(userName = "Febro")
        },
        content = {
            Box(modifier = Modifier.padding(it).fillMaxSize(),) {
                when (selectedItemIndex) {
                    0 -> BerandaScreen()
//                    1 -> ForumScreen()
//                    2 -> RiwayatScreen()
//                    3 -> SettingsScreen()
                }
            }
        },
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
    )
//        innerPadding ->
//        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(),
////            contentAlignment = Alignment.Center
//        )
//    }
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

                else -> {}
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