package com.example.mealjsonexample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage


@Composable
fun Navigation(
    modifier: Modifier,
    navigationController: NavHostController,
){
    val viewModel: MealsViewModel = viewModel()
    NavHost(
        modifier = modifier,
        navController = navigationController,
        startDestination = Graph.mainScreen.route
    ){
        composable(route = "${Graph.secondScreen.route}"){
            SecondScreen(viewModel)
        }
        composable(route = Graph.mainScreen.route){
            MainScreen(viewModel, navigationController)
        }
    }
}

@Composable
fun SecondScreen(viewModel: MealsViewModel) {
    val categoryName = viewModel.chosenCategoryName.collectAsState()
    val dishesState = viewModel.mealsState.collectAsState()
    viewModel.getAllDishesByCategoryName(categoryName.value)

    Column(modifier = Modifier.padding(16.dp)) {
        if (dishesState.value.isLoading) {
            LoadingScreen()
        } else if (dishesState.value.isError) {
            ErrorScreen(dishesState.value.error!!)
        } else if (dishesState.value.result.isNotEmpty()) {
            DishesScreen(dishesState.value.result)
        }
    }
}

@Composable
fun DishesScreen(result: List<Meal>) {
    LazyColumn{
        items(result){
            DishItem(it)
        }
    }
}

@Composable
fun DishItem(meal: Meal) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = null,
                modifier = Modifier
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = meal.mealName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MainScreen(viewModel: MealsViewModel, navigationController: NavHostController) {
    val categoriesState = viewModel.categoriesState.collectAsState()

    if (categoriesState.value.isLoading) {
        LoadingScreen()
    } else if (categoriesState.value.isError) {
        ErrorScreen(categoriesState.value.error!!)
    } else {
        CategoriesScreen(viewModel, categoriesState.value.result, navigationController)
    }
}
@Composable
fun CategoriesScreen(viewModel: MealsViewModel, result: List<Category>, navigationController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(result){
            CategoryItem(viewModel, it, navigationController)
        }
    }
}
@Composable
fun CategoryItem(viewModel: MealsViewModel, category: Category, navigationController: NavHostController) {
    Card(
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
            .clickable {
                viewModel.setChosenCategory(category.strCategory)
                navigationController.navigate("${Graph.secondScreen.route}")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = category.strCategoryThumb,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = category.strCategory,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ErrorScreen(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $error",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
        )
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
        )
    }
}
