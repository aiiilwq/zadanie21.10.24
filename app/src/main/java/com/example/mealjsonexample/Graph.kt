package com.example.mealjsonexample

object Graph {
    val mainScreen: Screen = Screen("MainScreen")
    val secondScreen: Screen = Screen("SecondScreen")
    val dishDetailScreen: Screen = Screen("DishDetailScreen") // Добавлено определение для деталей блюда
}

data class Screen(
    val route: String,
)