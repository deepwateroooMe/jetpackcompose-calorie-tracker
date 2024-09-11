package com.luisma.router

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luisma.core_ui.services.NavigationAction
import com.luisma.core_ui.services.NavigationCommands
import com.luisma.core_ui.services.NavigationService
import com.luisma.onboarding_ui.pages.onbaording_age.OnboardingAge
import com.luisma.onboarding_ui.pages.onbaording_age.OnboardingAgeVM
import com.luisma.onboarding_ui.pages.onboarding_activity.OnboardingActivity
import com.luisma.onboarding_ui.pages.onboarding_activity.OnboardingActivityVM
import com.luisma.onboarding_ui.pages.onboarding_gender.OnboardingGender
import com.luisma.onboarding_ui.pages.onboarding_gender.OnboardingGenderVM
import com.luisma.onboarding_ui.pages.onboarding_goal.OnboardingGoal
import com.luisma.onboarding_ui.pages.onboarding_goal.OnboardingGoalVM
import com.luisma.onboarding_ui.pages.onboarding_height.OnboardingHeight
import com.luisma.onboarding_ui.pages.onboarding_height.OnboardingHeightVM
import com.luisma.onboarding_ui.pages.onboarding_nutrients.OnboardingNutrients
import com.luisma.onboarding_ui.pages.onboarding_nutrients.OnboardingNutrientsVM
import com.luisma.onboarding_ui.pages.onboarding_weight.OnboardingWeight
import com.luisma.onboarding_ui.pages.onboarding_weight.OnboardingWeightVM
import com.luisma.onboarding_ui.pages.onboarding_welcome.OnboardingWelcome
import com.luisma.onboarding_ui.pages.onboarding_welcome.OnboardingWelcomeVM
import com.luisma.startup_ui.Startup
import com.luisma.startup_ui.StartupVM
import com.luisma.tracker_ui.pages.tracker_search_food.TrackerSearchFood
import com.luisma.tracker_ui.pages.tracker_search_food.TrackerSearchFoodVM
import com.luisma.tracker_ui.pages.tracker_dashboard.TrackerDashboard
import com.luisma.tracker_ui.pages.tracker_dashboard.TrackerDashboardVM
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun NavHostController.putAsStartRoute(route: String) {
     // 【TODO】：下面一行是说，如果导航图里，先前【曾经设置过什么起始页】，这里弹出来，就是更新起始页的意思？？？
    popBackStack(graph.startDestinationId, true) // 后面有个栈：可以缓存页面，跳转起来更高效
    graph.setStartDestination(route) // 更新、设置、新的首页
    navigate(route) // 跳转到新的首页
}
@Composable
fun Router(
     // 应用入口处，全局注入的第一个服务。自顶向下，创建上下文，后续所有自动注入时所需要的、上下文参数，也都会一一准备齐备
    navigationService: NavigationService,
) {
 // 两个、不可变、本地成员变量：感觉，像是记住、当前什么上下文的，简写，如回字的四样写法般。。。。
    val navController = rememberNavController()
    val navigationCoroutine = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = NavigationCommands.Startup.route // 【初始化】：一样的、指定，起始页，是哪一个页面画面
    ) {
        navigationCoroutine.launch {
             // 流：永远收集，最后一个、最新一个、命令。当新命令出现时，先前的命令、会被自动取消掉
            navigationService.navigationCommands.collectLatest { navigationCommand ->
                 // 相当于是，设定，三种不同用户按键命令的，三个回调逻辑
                when (navigationCommand.navigationAction) {
                    NavigationAction.Forward -> navController.navigate(navigationCommand.route)
                    NavigationAction.PutAsStartRoute -> navController.putAsStartRoute(
                        navigationCommand.route
                    )
                    NavigationAction.GoBack -> navController.popBackStack()
                }
            }
        }
        composable(
            route = NavigationCommands.Startup.route
        ) {
            hiltViewModel<StartupVM>()
            Startup()
        }
        composable(
            route = NavigationCommands.OnboardingWelcomeAsInitial.route
        ) {
            val vm = hiltViewModel<OnboardingWelcomeVM>()
            OnboardingWelcome(
                eventDispatcher = vm::eventDispatcher
            )
        }
        composable(
            route = NavigationCommands.OnbaordingGender.route
        ) {
            val vm = hiltViewModel<OnboardingGenderVM>()
            OnboardingGender(
                state = vm.state,
                eventDispatcher = vm::eventDispatcher
            )
        }
        composable(
            route = NavigationCommands.OnboardingAge.route
        ) {
            val vm = hiltViewModel<OnboardingAgeVM>()
            OnboardingAge(
                state = vm.state,
                eventDispatcher = vm::eventDispatcher
            )
        }
        composable(
            route = NavigationCommands.OnbaordingHeight.route
        ) {
            val vm = hiltViewModel<OnboardingHeightVM>()
            OnboardingHeight(
                state = vm.state,
                eventDispatcher = vm::eventDispatcher
            )
        }
        composable(
            route = NavigationCommands.OnboardingWeight.route
        ) {
            val vm = hiltViewModel<OnboardingWeightVM>()
            OnboardingWeight(
                state = vm.state,
                eventDispatcher = vm::eventDispatcher
            )
        }
        composable(
            route = NavigationCommands.OnboardingActivityLevel.route
        ) {
            val vm = hiltViewModel<OnboardingActivityVM>()
            OnboardingActivity(
                eventDispatcher = vm::eventDispatcher,
                state = vm.state
            )
        }
        composable(
            route = NavigationCommands.OnboardingGoal.route
        ) {
            val vm = hiltViewModel<OnboardingGoalVM>()
            OnboardingGoal(
                state = vm.state,
                eventDispatcher = vm::eventDispatcher
            )
        }
        composable(
            route = NavigationCommands.OnboardingNutrientsGoal.route
        ) {
            val vm = hiltViewModel<OnboardingNutrientsVM>()
            OnboardingNutrients(
                state = vm.state,
                eventDispatcher = vm::eventDispatcher
            )
        }
        composable(
            route = NavigationCommands.TrackerDashboardAsInitial.route
        ) {
            // 真正使用的地方，也是，使用 hilt 库里定义的 hiltViewModel() 方法，这么自动注入的
            // 【TODO】：想要知道，TrackerDashboardVM 构造器自动注入所需要的7 个参数，这个上下文中，全了吗？应该一定是全的
            val vm = hiltViewModel<TrackerDashboardVM>()
            TrackerDashboard(
                eventDispatcher = vm::eventDispatcher,
                state = vm.state
            )
        }
        composable(
            route = NavigationCommands.TrackerSearchFood.fullRoute,
            arguments = NavigationCommands.TrackerSearchFood.namedNavArguments
        ) {
            val vm = hiltViewModel<TrackerSearchFoodVM>()
            TrackerSearchFood(
                state = vm.state,
                eventDispatcher = vm::eventDispatcher
            )
        }

    }
}