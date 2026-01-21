package com.sarva.expenses.presentation.expense_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun MonthHeaderShimmer(
    shimmerInstance: Shimmer,
    isDark: Boolean = isSystemInDarkTheme()
) {
    val glassBase = if (isDark) Color.White.copy(alpha = 0.05f) else Color.White

//    val itemFill = if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.04f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .shimmer(shimmerInstance),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(12.dp)
                .background(
                    color = glassBase,
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}

@Composable
fun ExpenseCardShimmer(
    shimmerInstance: Shimmer,
    isDark: Boolean = isSystemInDarkTheme(),
) {
    val glassBase = if (isDark) Color.White.copy(alpha = 0.05f) else Color.White

    val itemFill = if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.04f)

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shimmer(shimmerInstance),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = glassBase),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(modifier = Modifier.size(48.dp).background(itemFill, CircleShape))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title & Date lines
                Box(
                    modifier = Modifier.fillMaxWidth(0.6f).height(16.dp)
                        .background(itemFill, RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier.fillMaxWidth(0.4f).height(12.dp)
                        .background(itemFill, RoundedCornerShape(4.dp))
                )
            }

            // Price block
            Box(
                modifier = Modifier.width(60.dp).height(20.dp)
                    .background(itemFill, RoundedCornerShape(4.dp))
            )
        }
    }
}