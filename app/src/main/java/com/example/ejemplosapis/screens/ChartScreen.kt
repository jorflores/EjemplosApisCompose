package com.example.ejemplosapis.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.ejemplosapis.model.ExerciseHabits.GetExerciseHabitsRequest
import com.example.ejemplosapis.viewModel.AppViewModel


@Composable
fun ChartScreen(viewModel: AppViewModel) {
    val chartDataResult by viewModel.exerciseHabitsResult.collectAsState()

    val initialBarData = emptyList<BarData>()

    var barData by remember { mutableStateOf(initialBarData) }

    var maxRange = barData.size
    var yStepSize = barData.size

    if (barData.isNotEmpty()) {
        val xAxisData = AxisData.Builder()
            .axisStepSize(30.dp)
            .steps(barData.size - 1)
            .bottomPadding(40.dp)
            .axisLabelAngle(20f)
            .startDrawPadding(48.dp)
            .labelData { index -> barData[index].label }
            .build()

        val yAxisData = AxisData.Builder()
            .steps(barData.size)
            .labelAndAxisLinePadding(20.dp)
            .axisOffset(20.dp)
            .labelData { index -> (index * (maxRange / yStepSize)).toString() }
            .build()


        val barChartData = BarChartData(
            chartData = barData,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            barStyle = BarStyle(
                paddingBetweenBars = 20.dp,
                barWidth = 25.dp
            ),
            showYAxis = true,
            showXAxis = true,
            horizontalExtraSpace = 10.dp,
        )

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.7f),
                contentAlignment = Alignment.Center
            ) {
                val fecha = "MAYO 13-17"
                Column {
                    Text(text = fecha, modifier = Modifier.align(Alignment.CenterHorizontally))
                    BarChart(modifier = Modifier, barChartData = barChartData)
                    Spacer(modifier = Modifier.padding(end = 200.dp))
                }
            }

        }

    }
    else {
        Button(onClick = {
            viewModel.getExerciseHabits(
                GetExerciseHabitsRequest(
                    "2024-05-19",
                    "true",
                    "2024-05-15",
                    1
                )
            )
        }) {
            Text(text = "Get Chart Data")
        }
    }

    LaunchedEffect(chartDataResult) {
        chartDataResult?.let { result ->
            result.onSuccess { response ->
                val listOfBarData = response.map { data ->
                    BarData(
                        point = Point(data.date.toFloat(), data.exerciseLevel.toFloat()),
                        label = "May ${data.date}"/*,
                        color = Color(
                            red = Random.nextInt(256),
                            green = Random.nextInt(256),
                            blue = Random.nextInt(256)
                        )*/
                    )
                }
                maxRange = 5
                yStepSize = 5
                barData = listOfBarData
            }.onFailure { exception ->
                // Handle failure
                println("Error fetching data: ${exception.message}")
            }
        }
    }


}
