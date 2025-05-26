package com.example.legalapor.home.ui

import ForumScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.legalapor.R
import com.example.legalapor.home.beranda.ui.BerandaScreen
import com.example.legalapor.home.riwayat.RiwayatScreen
import com.example.legalapor.home.ui.components.CustomTopAppBar
import com.example.legalapor.home.ui.theme.LegaLaporTheme
import com.example.legalapor.laporan.LawyerSelectPage
import com.example.legalapor.navigation.AppNavigation
import com.example.legalapor.navigation.NavRoutes
import com.example.legalapor.service.viewmodel.UserViewModel

//class HomeActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
////            LegaLaporTheme {
////                AppNavigation()
////            }
////        }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, userViewModel: UserViewModel = viewModel()) {
//    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
//    var selectedItemIndex by remember { mutableIntStateOf(0) }

    val actualSelectableNavItems = listOf(
        NavItem("Home", Icons.Filled.Home, NavRoutes.Beranda.route),
        NavItem("Forum", Icons.AutoMirrored.Outlined.Send, NavRoutes.Forum.route),
        NavItem("Riwayat", Icons.Filled.Person, NavRoutes.Riwayat.route),
        NavItem("Lainnya", Icons.Filled.Settings, NavRoutes.Lainnya.route)
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
            CustomTopAppBar(userName = userViewModel.name.value)
        },
//        content = {
//            Box(modifier = Modifier.padding(it).fillMaxSize(),) {
//                when (selectedItemIndex) {
//                    0 -> BerandaScreen()
////                    1 -> ForumScreen()
////                    2 -> RiwayatScreen()
////                    3 -> SettingsScreen()
//                }
//            }
//        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(120.dp),
            ) {
                BottomNavBar(
                    items = navigationItems,
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(NavRoutes.Beranda.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = @Composable {
            FloatingActionButton(modifier = Modifier.offset(y = 70.dp).size(57.dp),
                containerColor = Color(0xFF31469F),
                shape = CircleShape,
                onClick = {navController.navigate(NavRoutes.LawyerSelect.route)}) {
                Image(painter = painterResource(id = R.drawable.lapor_button_image), modifier = Modifier.size(20.dp), contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentRoute) {
                NavRoutes.Beranda.route -> BerandaScreen()
                NavRoutes.Forum.route -> ForumScreen()
                NavRoutes.Riwayat.route -> RiwayatScreen()
                NavRoutes.Lainnya.route -> {/* SettingsScreen() */}
            }
        }
    }
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier,
                 items: List<NavBarElement>,
//                 selectedItemIndex: Int,
                 currentRoute: String?,
                 onItemSelected: (String) -> Unit) {
    NavigationBar(modifier = modifier.height(IntrinsicSize.Min)) {
        items.forEach { element ->
            when (element) {
                is NavItemData -> {
                    NavigationBarItem(
                        selected = currentRoute == element.navItem.route,
                        onClick = { onItemSelected(element.navItem.route) },
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
    MainScreen(rememberNavController())
}

// local class untuk ui
data class NavItem(val title: String, val icon: ImageVector, val route: String)
sealed class NavBarElement
data class NavItemData(val navItem: NavItem, val originalIndex: Int) : NavBarElement()
object SpacerData : NavBarElement()