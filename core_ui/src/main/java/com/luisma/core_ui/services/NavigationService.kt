package com.luisma.core_ui.services
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.luisma.core.models.food.MealType
import com.luisma.core.models.food.ProductsFood
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// 自定义： NavigationService ？能够实现前进、后退，都是因为缓存过页面进后台栈里。。
enum class NavigationAction {
    Forward, PutAsStartRoute, GoBack
}
interface INavigationCommand {
    val route: String // 身份证标记，字符串 
    val navigationAction: NavigationAction
        get() = NavigationAction.Forward
    val arguments: List<NamedNavArgument>
        get() = listOf()
}

object NavigationCommands {
    val GoBack = object : INavigationCommand {
        override val route: String
            get() = "GoBack"
        override val navigationAction = NavigationAction.GoBack
    }
    val Startup = object : INavigationCommand {
        override val route: String
            get() = "Startup"
    }
    val OnboardingWelcomeAsInitial = object : INavigationCommand {
        override val route: String
            get() = "OnboardingWelcomeAsInitial"
        override val navigationAction = NavigationAction.PutAsStartRoute
    }
    val OnbaordingGender = object : INavigationCommand {
        override val route: String
            get() = "OnbaordingGender"
    }
    val OnboardingAge = object : INavigationCommand {
        override val route: String
            get() = "OnboardingAge"
    }
    val OnbaordingHeight = object : INavigationCommand {
        override val route: String
            get() = "OnbaordingHeight"
    }
    val OnboardingWeight = object : INavigationCommand {
        override val route: String
            get() = "OnboardingWeight"
    }
    val OnboardingActivityLevel = object : INavigationCommand {
        override val route: String
            get() = "OnboardingActivityLevel"
    }
    val OnboardingGoal = object : INavigationCommand {
        override val route: String
            get() = "OnboardingGoal"
    }
    val OnboardingNutrientsGoal = object : INavigationCommand {
        override val route: String
            get() = "OnboardingNutrientsGoal"
    }
    val TrackerDashboardAsInitial = object : INavigationCommand {
        override val route: String
            get() = "TrackerDashboardAsInitial"
        override val navigationAction = NavigationAction.PutAsStartRoute
    }
    // 【TODO】：亲爱的表哥的活宝妹，感觉，它这里没有实现完整，或是【模拟器上、网络问题】，搜索不出任何东西，功能开发完整的应用，应该不至于如此才对
    object TrackerSearchFood {
        const val baseRoute = "TrackerSearchFood"
        // 这个 key: 是可以哪里更新值的吗？ const val
        const val key = "type_food" // 这里是写死的 type_food ？有这个路径吗？缺少这些，功能不完整，也就是仍然是、一个废弃的应用而已。。。
        const val fullRoute = "$baseRoute/{$key}"
        val namedNavArguments: List<NamedNavArgument> = listOf(navArgument(name = key) {
                                                                   type = NavType.StringType
                                                               })
    }
    data class TrackerSearchFoodFromTrackerDashboard(val foodType: String?) : INavigationCommand {
        override val route: String
            get() = "${TrackerSearchFood.baseRoute}/$foodType"
        override val arguments: List<NamedNavArgument>
            get() = TrackerSearchFood.namedNavArguments
    }
}
sealed class GoBackNavigationCommand {
    data class TrackerFromSearchToDashboard(
        val mealType: MealType,
        val food: ProductsFood,
    ) : GoBackNavigationCommand()
}

// 感觉这里，像是 Flow 流式处理般，写得、像是、狠高级的样子。。。。
// Kotlin 语言、协程、流式写法，还是比较强大滴。。。
// 总之，这里就是，把用户的【按键命令】，转换成、安卓Compose 的、这里自定义的一个个【跳转命令】，然后根据跳转命令，实现不同页面的跳转。。
class NavigationService(
    private val navigationFlow: MutableSharedFlow<INavigationCommand>,
    private val returnNavigationFlow: MutableSharedFlow<GoBackNavigationCommand>,
) {
    val navigationCommands = navigationFlow.asSharedFlow()
    suspend fun setNavigationCommand(
        command: INavigationCommand,
    ) {
        navigationFlow.emit(command)
    }
    // 像亲爱的表哥的活宝妹的第1 个安卓应用【涂鸦板上的、重做与撤销、命令】，这里，每前进到一个新页面，都同步发布、缓存这个命令、进【可以返回的、后台栈】，从而提供【可撤销。。操作】
    // 就是，当前上面、执行过了的导航命令 command, 就成为了、刚执行过的最后一个命令，加入到、可撤销可回退的流式缓存中。
    // 这里是，定义了两个协程函数，【TODO】：去找，这两个函数，真正调用、使用的地方
    val returnNavigationCommands = returnNavigationFlow.asSharedFlow()
    suspend fun setGoBackNavigationCommand(
        command: GoBackNavigationCommand,
    ) {
        returnNavigationFlow.emit(command)
    }
}