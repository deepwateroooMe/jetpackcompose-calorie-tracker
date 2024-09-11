package com.luisma.core_ui.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.luisma.core_ui.theme.CTrackerTheme
import com.luisma.core_ui.theme.CalorieTrackerTheme

@Composable
fun CTLoader(
    modifier: Modifier = Modifier,
    color: Color = CTrackerTheme.colors.brightGreen,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth // 安卓： material 3? 库里的、缺省、字宽
) {

    // 【环形、加载进度条】：它的显示，应该只是在必要的时候（随某些 flag参数值的设定）才显示出来，其它是隐藏的
    CircularProgressIndicator(  // 【环形、加载进度条】：给用户一点儿加载进度提示
        modifier = modifier,
        color = color,
        strokeWidth = strokeWidth
    )
}

@Preview
@Composable
fun CTLoaderPreview() {
    CalorieTrackerTheme {
        CTLoader()
    }
}