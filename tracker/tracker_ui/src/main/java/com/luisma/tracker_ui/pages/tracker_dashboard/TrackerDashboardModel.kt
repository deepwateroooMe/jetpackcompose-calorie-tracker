package com.luisma.tracker_ui.pages.tracker_dashboard

import com.luisma.core.models.food.MealType
import com.luisma.core.models.food.MealsByDate
import java.time.LocalDate
data class TrackerDashboardModel(
    val currentFood: MealsByDate,
    val currentDate: LocalDate,
    val expandedMeal: Map<MealType, Boolean> // 这四种不同类型，是都展开、或是任意展不展开的。所以字典来记
) {
    companion object {
        fun initial(): TrackerDashboardModel {
            val mealInitial = MealsByDate.initial() // 初始化 
            return TrackerDashboardModel(
                currentFood = mealInitial,
                currentDate = LocalDate.now(),
                expandedMeal = MealType.values().associateWith { false }  // 所有四种餐点，都不展开
            )
        }
    }
    fun mealTypes(): Array<MealType> = MealType.values()
}
