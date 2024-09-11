package com.luisma.core.models.food

// 这是：可以公网API 搜索到、并流式下载下来的、食谱菜单定义
// 如果，这些都是公网API 抓下来的、亲爱的表哥的活宝妹自定义的食谱菜单，就只能、打包进 .apk 资源文件读入了。
// 亲爱的表哥的活宝妹，可能找不到【糖尿病患】相关的菜单【糖含量、升糖指数】之类的、可用、公网API... 合并【肾脏病】【糖尿病、肾病。。。】
data class Food(
    val count: Int = 0,
    val page: Int = 0,
    val pageCount: Int = 0,
    val pageSize: Int = 0,
    val products: List<ProductsFood> = emptyList(),
)

data class ProductsFood( // 菜谱菜单、数据类
    val name: String = "",
    val imageUrl: String = "",
    val nutriments: NutrimentsFood = NutrimentsFood(),
    val gramsConsumedByUser: Int = 0,
    val expanded: Boolean = false,
) {
    fun kCalByGrams(): Int {
        return nutrimentsByGrams(nutriments.energyKcal100g)
    }

    fun carbsByGrams(): Int {
        return nutrimentsByGrams(nutriments.carbohydrates100g)
    }

    fun fatsByGrams(): Int {
        return nutrimentsByGrams(nutriments.fat100g)
    }

    fun proteinsByGrams(): Int {
        return nutrimentsByGrams(nutriments.proteins100g)
    }

    private fun nutrimentsByGrams(value: Int): Int {
        if (gramsConsumedByUser == 0) {
            return 0
        }
        return (value * gramsConsumedByUser) / 100
    }
}

data class NutrimentsFood(
    val carbohydrates100g: Int = 0,
    val energyKcal100g: Int = 0,
    val fat100g: Int = 0,
    val proteins100g: Int = 0,
)
