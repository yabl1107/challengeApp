package com.myself.mychallengueapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarRow(startDate: LocalDate,
                daysToShow: Int = 14,
                selectedDate: LocalDate = LocalDate.now(),
                onClick : (LocalDate) -> Unit = {}) {
    val dates = remember {
        List(daysToShow) { startDate.minusDays(it.toLong()) }
    }


    LazyRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true
    ) {
        items(dates) { date ->
            val isSelected = date == selectedDate
            DateItem(date,
                modifier= Modifier
                    .clickable {
                    onClick(date)
                },
                isSelected
            )
        }
    }
}

@Composable
fun DateItem(date: LocalDate, modifier: Modifier, selected: Boolean) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .background(if (selected) colors.tertiaryContainer else colors.secondaryContainer, shape = RoundedCornerShape(8.dp))
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.format(DateTimeFormatter.ofPattern("MMM")),
            fontWeight = FontWeight.Bold,
            color = if (selected) colors.onTertiaryContainer else colors.onSecondaryContainer
        )
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 20.sp,
            color = if (selected) colors.onTertiaryContainer else colors.onSecondaryContainer
        )
    }
}
