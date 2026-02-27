package com.example.hcr0_4.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.navigazione.HcrNavHost
import com.example.hcr0_4.ui.theme.*


enum class Screens() {
    Home,
    User,
    Folder,
    Personalize,
    VitalParameters,
}


@Composable
fun Navigation(
    viewModel: ScreensViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ){innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        //innerPadding non viene utilizzato
        HcrNavHost(navController = navController, modifier = Modifier.padding(innerPadding))


    }

}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Screens.Personalize,
        Screens.VitalParameters,
        Screens.Home,
        Screens.Folder,
        Screens.User,
    )

    NavigationBar {
        val currentRoute by navController.currentBackStackEntryAsState()

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    val icon = when (screen) {
                        Screens.Home -> ImageVector.vectorResource(id = R.drawable.home_ic)
                        Screens.User -> ImageVector.vectorResource(id = R.drawable.user_ic)
                        Screens.Folder -> ImageVector.vectorResource(id = R.drawable.folder_ic)
                        Screens.Personalize -> ImageVector.vectorResource(id = R.drawable.personalize_ic)
                        Screens.VitalParameters -> ImageVector.vectorResource(id = R.drawable.vitalparameters_ic)
                    }
                    val tint = if (currentRoute?.destination?.route == screen.name) {
                        HcrGreen
                    } else {
                        Color.White
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint
                    )
                },
                //label = { Text(screen.name) },
                selected = currentRoute?.destination?.route == screen.name,
                onClick = {
                    navController.navigate(screen.name) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = true
            )
        }
    }
}