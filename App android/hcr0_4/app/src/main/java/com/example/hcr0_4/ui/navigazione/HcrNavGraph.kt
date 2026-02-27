package com.example.hcr0_4.ui.navigazione

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.Screens
import com.example.hcr0_4.ui.schermate.menuCartelle.InserisciCartella
import com.example.hcr0_4.ui.schermate.menuCartelle.InserisciCartellaDestination
import com.example.hcr0_4.ui.schermate.menuCartelle.InserisciReferto
import com.example.hcr0_4.ui.schermate.menuCartelle.InserisciRefertoDestination
import com.example.hcr0_4.ui.schermate.menuParametriVitali.InserisciMisurazioni
import com.example.hcr0_4.ui.schermate.menuParametriVitali.InserisciMisurazioniDestination
import com.example.hcr0_4.ui.schermate.menuParametriVitali.InserisciPv
import com.example.hcr0_4.ui.schermate.menuParametriVitali.InserisciPvDestination
import com.example.hcr0_4.ui.schermate.home.InserisciPosologia
import com.example.hcr0_4.ui.schermate.home.InserisciPosologiaDestination
import com.example.hcr0_4.ui.schermate.home.InserisciTerapiaDestination
import com.example.hcr0_4.ui.schermate.home.InserisciTerapiaScreen

import com.example.hcr0_4.ui.schermate.menuPersonalizzazioni.ModificaTerapia
import com.example.hcr0_4.ui.schermate.menuPersonalizzazioni.ModificaTerapiaDestination

import com.example.hcr0_4.ui.schermate.menuPersonalizzazioni.PersonalizeScreen
import com.example.hcr0_4.ui.schermate.menuCartelle.FolderScreen
import com.example.hcr0_4.ui.schermate.home.HomeScreen
import com.example.hcr0_4.ui.schermate.menuUtente.UserScreen
import com.example.hcr0_4.ui.schermate.menuParametriVitali.ParametriVitaliScreen
import com.example.hcr0_4.ui.schermate.menuParametriVitali.StoricoParametriVitaliScreen
import com.example.hcr0_4.ui.schermate.menuParametriVitali.StoricoPvDestination


@Composable
fun HcrNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Home.name,
        modifier = modifier
    ) {


        //HOME
        composable(Screens.Home.name) {
            HomeScreen(
                navigateToInserisciTerapia = {
                    navController.navigate("${InserisciTerapiaDestination.route}/${it}")
                }
            )
        }



        composable(
            route =  InserisciTerapiaDestination.routeWithArgs,
            arguments = listOf(navArgument(InserisciTerapiaDestination.terapiaDetailsArg) {
                type = NavType.StringType
            })
        ){
            InserisciTerapiaScreen(
                navigateToInserisciPosologia = {
                    navController.navigate("${InserisciPosologiaDestination.route}/${it}")
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = InserisciPosologiaDestination.routeWithArgs,
            arguments = listOf(navArgument(InserisciPosologiaDestination.terapiaDetailsArg) {
                type = NavType.StringType
            })
        ) {
            InserisciPosologia(
                navigateToHome = { navController.navigate("Home") },
                onNavigateUp = { terapiaDetailsString ->
                    Log.d("NavHost", "Navigating up with terapiaDetailsString: $terapiaDetailsString")

                    navController.navigateUp()
                }
            )
        }

        composable(
            route= ModificaTerapiaDestination.route
        ) {
            ModificaTerapia(
                navigateBack = { navController.navigate(Screens.Personalize.name)  },
                onNavigateUp = { navController.navigateUp() }
            )
        }



        //PARAMETRI VITALI

       composable(route = Screens.VitalParameters.name) {
    ParametriVitaliScreen(
        navigateToAggiungiMisurazione = { navController.navigate(
            "${InserisciMisurazioniDestination.route }/${it}" )},
        navigateToInserisciPv = { navController.navigate(InserisciPvDestination.route) },
        navigateToStoricoPv = { idParametroVitale ->
            val route = "${StoricoPvDestination.route}/${idParametroVitale}"
            navController.navigate(route)
        }
    )
}

        composable(
            route = StoricoPvDestination.routeWithArgs,
            arguments = listOf(navArgument(StoricoPvDestination.idPvArg) {
                type = NavType.IntType
            })
        ){
            StoricoParametriVitaliScreen(
                navigateBack = { navController.navigate(route = Screens.VitalParameters.name) }
            )
        }
        composable(
            route= InserisciPvDestination.route
        ) {
            InserisciPv(
                navigateBack = { navController.navigate(route = Screens.VitalParameters.name)  },
                onNavigateUp = { navController.navigateUp() }

            )
        }
        composable(
            route = InserisciMisurazioniDestination.routeWithArgs,
            arguments = listOf(navArgument(InserisciMisurazioniDestination.idPvArg) {
                type = NavType.IntType
            })
        ){
            InserisciMisurazioni(
                navigateBack = { navController.navigate(route = Screens.VitalParameters.name)   },
                onNavigateUp = { navController.navigateUp() }
            )
        }





        composable(route = Screens.User.name) {
            UserScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }




        composable(route = Screens.Folder.name) {

            FolderScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium)),
                navigateToInserisciCartella = { navController.navigate(InserisciCartellaDestination.route) },
                     )
        }

        composable(route = Screens.Personalize.name) {
            PersonalizeScreen()

        }



//
        //LUCA



        composable(
            route = InserisciCartellaDestination.route
        ){
            InserisciCartella(
                navigateBack = { navController.navigate(route = Screens.Folder.name)   },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = InserisciRefertoDestination.routeWithArgs,
            arguments = listOf(navArgument(InserisciRefertoDestination.idCartella) {
                type = NavType.IntType
            })
        ){
            InserisciReferto(
                navigateBack = { navController.navigate(route = Screens.Folder.name)   },
                onNavigateUp = { navController.navigateUp() }
            )
        }


    }
}