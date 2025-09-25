package com.glory.wewenunua.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.glory.wewenunua.ui.screens.auth.RegisterScreen
import com.glory.wewenunua.ui.screens.products.AddProductScreen
import com.glory.wewenunua.ui.screens.products.ProductsScreen
import com.glory.wewenunua.ui.screens.products.UpdateProductScreen
import com.glory.wewenunua.ui.screens.task.AddTaskScreen
import com.glory.wewenunua.ui.screens.task.ViewTaskScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ADD_TASK
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        //Product
        composable(ROUT_ADD_PRODUCT) {
            AddProductScreen(navController)
        }
        composable(ROUT_VIEW_PRODUCTS) {
            ProductsScreen(navController)
        }
        composable(ROUT_UPDATE_PRODUCT,
            arguments = listOf(navArgument("productId")
            {type = NavType.StringType})) {
                backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")!!
            UpdateProductScreen(navController,productId)
        }

        //End


        //Auth
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUT_LOGIN) {
            LoginScreen(navController)
        }
        //End


        //Task

        composable(ADD_TASK){
            AddTaskScreen(navController = navController)
        }
        composable(VIEW_TASKS){
            ViewTaskScreen(navController = navController)
        }
        //End















    }
}
