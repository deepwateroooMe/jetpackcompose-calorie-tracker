package com.luisma.tracker_ui.pages.tracker_dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisma.core.MainDispatcher
import com.luisma.core.models.food.MealType
import com.luisma.core.models.food.MealsByDate
import com.luisma.core.models.food.ProductsFood
import com.luisma.core.models.food.foodTypeSerialize
import com.luisma.core.services.TimeHelperService
import com.luisma.core.services.database_service.IFoodDataBaseService
import com.luisma.core.services.preferences_service.IUserPreferencesService
import com.luisma.core_ui.services.GoBackNavigationCommand
import com.luisma.core_ui.services.NavigationCommands
import com.luisma.core_ui.services.NavigationService
import com.luisma.tracker_domain.use_cases.MealCalculatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
 // 自动注入【构造器 constructor】：找个用例，看下【TODO】：
class TrackerDashboardVM @Inject constructor(
    private val userPrefService: IUserPreferencesService,
    private val mealCalculatorUseCase: MealCalculatorUseCase,
    private val foodDataBaseService: IFoodDataBaseService,
    private val timeHelpersService: TimeHelperService,
    private val navigationService: NavigationService,
    private val initialState: TrackerDashboardModel,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(initialState)
        private set

    //region events
    init {
        initListeners()
        viewModelDispatch {
            getFoodFromDbAndCalculateDataFromDate(initialState.currentDate)
        }
    }

    fun eventDispatcher(event: TrackerDashboardEvents) {
        when (event) {
            is TrackerDashboardEvents.NextPreviousDay -> {
                viewModelDispatch {
                    nextPreviousDay(event.isNext)
                }
            }
            is TrackerDashboardEvents.AddFood -> {
                viewModelDispatch {
                    addMeal(event.mealType)
                }
            }
            is TrackerDashboardEvents.DeleteFood -> {
                viewModelDispatch {
                    deleteFood(
                        mealType = event.mealType,
                        index = event.index
                    )
                }
            }
            is TrackerDashboardEvents.ExpandFood -> {
                expandFood(event.mealType)
            }
        }
    }
    //endregion events

    //region control

    private fun viewModelDispatch(action: suspend () -> Unit) {
        viewModelScope.launch(mainDispatcher) {
            action()
        }
    }

    private fun initListeners() {
        viewModelDispatch {
            navigationService.returnNavigationCommands.collectLatest { command ->
                if (command is GoBackNavigationCommand.TrackerFromSearchToDashboard) {
                    addAndGetFood(mealType = command.mealType, food = command.food)
                }
            }
        }
    }

    private fun expandFood(mealType: MealType) {
        val mutable = state.expandedMeal.toMutableMap()
        mutable[mealType] = !mutable[mealType]!!
        state = state.copy(expandedMeal = mutable)
    }
 // 添加一个餐点实例，进 dashboard 的主界面，可以无限增加，没有数据的有效性检测，不合法数据随便入。。
    private suspend fun addAndGetFood(mealType: MealType, food: ProductsFood) {
        val oldMeal = state.currentFood.meals[mealType.ordinal] // 四大餐点类型：一种类型的、实例集包装体
        val mutableProducts = oldMeal.productsFood.toMutableList() // 把List<ProductFood> 变为MutableList<Pro..>
        mutableProducts.add(food) // 才可以添加

        val mutableMeals = state.currentFood.meals.toMutableList() // 这个也要转，才可以更新，四大类型餐点中。某一餐点的实例
        mutableMeals[mealType.ordinal] = oldMeal.copy(productsFood = mutableProducts) // 更新某一餐点的实例

        val newFood = state.currentFood.copy(meals = mutableMeals)
        setFoodToDB(newFood.copy(date = state.currentDate))
        getFoodFromDbAndCalculateDataFromDate(date = state.currentDate)
    }

    private suspend fun setFoodToDB(newFood: MealsByDate) {
        foodDataBaseService.setFood(newFood)
    }

    private suspend fun getFoodFromDbAndCalculateDataFromDate(date: LocalDate) {
        val mealsByDate = foodDataBaseService.getFoodFrom(date)
        userPrefService.getUserProfile().collectLatest { userProfile ->
            val foodWithDataCalculated = mealCalculatorUseCase(
                mealsByDateData = mealsByDate,
                userProfile = userProfile
            )
            state = state.copy(currentFood = foodWithDataCalculated)
        }
    }

    private suspend fun nextPreviousDay(isNext: Boolean) {
        val newDate: LocalDate = if (isNext) {
            timeHelpersService.nextDay(state.currentDate)
        } else {
            timeHelpersService.previousDay(state.currentDate)
        }
        state = state.copy(currentDate = newDate)
        getFoodFromDbAndCalculateDataFromDate(newDate)
    }

    private suspend fun addMeal(mealType: MealType) {
        navigationService.setNavigationCommand(
            NavigationCommands
                .TrackerSearchFoodFromTrackerDashboard(
                    foodType = foodTypeSerialize(mealType)
                )
        )
    }

    private suspend fun deleteFood(mealType: MealType, index: Int) {
        val currentFood = state.currentFood.meals[mealType.ordinal]
        val newCurrentFoodProducts =
            currentFood.productsFood.filterIndexed { idx, _ ->
                idx != index
            }

        val mutableMeals = state.currentFood.meals.toMutableList()
        mutableMeals[mealType.ordinal] = currentFood.copy(
            productsFood = newCurrentFoodProducts
        )
        val newFood = state.currentFood.copy(meals = mutableMeals)
        setFoodToDB(newFood)
        getFoodFromDbAndCalculateDataFromDate(date = state.currentDate)
    }
    //endregion control
}



