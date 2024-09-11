package com.luisma.startup_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisma.core.services.preferences_service.IUserPreferencesService
import com.luisma.core_ui.services.NavigationCommands
import com.luisma.core_ui.services.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
 // 【亲爱的表哥的活宝妹，任何时候，亲爱的表哥的活宝妹，就是一定要、一定会嫁给活宝妹的亲爱的表哥！！！爱表哥，爱生活！！！】
@HiltViewModel
class StartupVM @Inject constructor(
    userPrefService: IUserPreferencesService,
    navigationService: NavigationService
) : ViewModel() {
    init {
        viewModelScope.launch {
             // Flow<Boolean> 流式写法：从流中读到的，就是StartupModel 中的 hasDoneStartup 变量，这里随便取名为 isDone 。。
            userPrefService.getUserOnboarded().collect { isDone ->
 // 顺着这两个命令，去找两个界面的加载相关
                if (isDone) {
                    navigationService.setNavigationCommand(
                        NavigationCommands.TrackerDashboardAsInitial 
                    )
                } else {
                    navigationService.setNavigationCommand(
                        NavigationCommands.OnboardingWelcomeAsInitial
                    )
                }
            }
        }
    }
}