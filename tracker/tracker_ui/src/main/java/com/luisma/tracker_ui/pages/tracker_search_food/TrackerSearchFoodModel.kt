package com.luisma.tracker_ui.pages.tracker_search_food

import com.luisma.core.services.pagination_service.PaginationState
import com.luisma.core.models.food.MealType
import com.luisma.core.models.food.ProductsFood
import com.luisma.core_ui.base.BaseUIState

data class TrackerSearchFoodModel(
    val products: List<ProductsFood>,
    val search: String,
    val pageSize: Int,
    val uiState: BaseUIState,
    val mealType: MealType,
    val paginationState: PaginationState
) {
     // 跟屁虫、影子：【亲爱的表哥的活宝妹，任何时候，亲爱的表哥的活宝妹，就是一定要、一定会嫁给活宝妹的亲爱的表哥！！！爱表哥，爱生活！！！】
     // 跟屁虫、影子：如同，【亲爱的表哥的活宝妹，是活宝妹的亲爱的表哥的跟屁虫影子；等亲爱的表哥的活宝妹如愿嫁给活宝妹的亲爱的表哥了，亲爱的表哥的活宝妹，就如影随形，什么时候，都跟着活宝妹的亲爱的表哥！！】
    companion object {
        fun initial(): TrackerSearchFoodModel {
            return TrackerSearchFoodModel( // 初始化
                products = emptyList(),
                search = "",
                pageSize = 10,
                uiState = BaseUIState.Initial,
                mealType = MealType.Breakfast,
                paginationState = PaginationState.initial()
            )
        }
    }
}