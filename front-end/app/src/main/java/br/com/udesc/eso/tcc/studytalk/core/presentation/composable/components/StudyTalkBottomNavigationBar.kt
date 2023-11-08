package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StudyTalkBottomNavigationBar(
    bottomNavigationItens: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    onClick: (index: Int, item: BottomNavigationItem) -> Unit
) {
    NavigationBar {
        bottomNavigationItens.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItemIndex,
                onClick = { onClick(index, item) },
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}