package com.glory.wewenunua.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.glory.wewenunua.ui.screens.auth.RegisterScreen
import com.glory.wewenunua.ui.screens.products.AddProductScreen
import com.glory.wewenunua.ui.screens.products.ProductsScreen
import com.glory.wewenunua.ui.screens.products.UpdateProductScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_VIEW_PRODUCTS
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_ADD_PRODUCT) {
            AddProductScreen(navController)
        }
        composable(ROUT_VIEW_PRODUCTS) {
            ProductsScreen(navController)
        }
        composable(ROUT_UPDATE_PRODUCT) {
            UpdateProductScreen(navController)
        }
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUT_LOGIN) {
            LoginScreen(navController)
        }















    }
}
