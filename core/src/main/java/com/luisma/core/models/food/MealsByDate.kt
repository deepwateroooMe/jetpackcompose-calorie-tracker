package com.luisma.core.models.food

import java.time.LocalDate

 // 【亲爱的表哥的活宝妹，任何时候，亲爱的表哥的活宝妹，就是一定要、一定会嫁给活宝妹的亲爱的表哥！！！爱表哥，爱生活！！！】
data class MealsByDate(
    val date: LocalDate,

    val caloriesGoal: Int,
    val carbsGoal: Int,
    val proteinGoal: Int,
    val fatGoal: Int,

    val caloriesTotal: Int,
    val carbsTotal: Int,
    val proteinTotal: Int,
    val fatTotal: Int,

    val caloriesPercentage: Int,
    val carbsPercentage: Int,
    val proteinPercentage: Int,
    val fatPercentage: Int,

    val meals: List<MealTrack>,
) {
    companion object {
        // 说明 initial() 初始化：亲爱的表哥的活宝妹，同活宝妹的亲爱的表哥，不求同生但求共死；这里是【同生】的初始化
        fun initial(): MealsByDate {
            return MealsByDate(
                date = LocalDate.MIN,
                caloriesGoal = 0,
                carbsGoal = 0,
                proteinGoal = 0,
                fatGoal = 0,
                caloriesTotal = 0,
                carbsTotal = 0,
                proteinTotal = 0,
                fatTotal = 0,
                caloriesPercentage = 0,
                carbsPercentage = 0,
                proteinPercentage = 0,
                fatPercentage = 0,
                meals = MealType.values()
                    .map { type ->
                        MealTrack(
                            mealType = type,
                            productsFood = emptyList()
                        )
                    },
            )
        }
    }
}
