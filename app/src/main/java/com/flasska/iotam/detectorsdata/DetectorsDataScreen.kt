package com.flasska.iotam.detectorsdata

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.flasska.domain.entity.ActuatorState
import com.flasska.domain.entity.DetectorData
import com.flasska.domain.entity.TemperatureActuatorState
import com.flasska.iotam.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

@Composable
internal fun DetectorsScreen(
    screenState: DetectorsDataState,
    screenEvent: (DetectorsDataEvent) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Slider for changing outside temperature
        Text(
            text = stringResource(R.string.out_temp, screenState.current.outsideTemperature),
            fontSize = 22.sp
        )

        Spacer(Modifier.height(16.dp))

        // Slider for changing inside temperature
        Text(
            text = stringResource(R.string.in_temp, screenState.current.insideTemperature),
            fontSize = 22.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.actuator_state, screenState.current.actuatorState.name),
            fontSize = 22.sp
        )

        Spacer(Modifier.height(16.dp))

        if (screenState.current.actuatorState != ActuatorState.Auto) {
            // Custom switch for changing temperature actuator state
            Switch(screenState, screenEvent)
        } else {
            Text(
                text = stringResource(
                    R.string.current_temperature_state,
                    screenState.current.temperatureActuatorState.name
                ),
                fontSize = 22.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Graphic(screenState.history)
    }
}

@Composable
private fun Switch(
    screenState: DetectorsDataState,
    screenAction: (DetectorsDataEvent) -> Unit
) {
    val directionRight = remember { mutableStateOf(false) }

    // Animation for changing state
    val transition = updateTransition(
        targetState = screenState.current.temperatureActuatorState,
        label = "switchTransition"
    )

    val offset = transition.animateDp(label = "offsetAnimation") { state ->
        when (state) {
            TemperatureActuatorState.Cold -> 10.dp
            TemperatureActuatorState.None -> 40.dp
            else -> 70.dp
        }
    }

    // Subtitle
    Text(
        text = stringResource(R.string.actuator_run),
        fontSize = 22.sp
    )

    Spacer(Modifier.height(16.dp))

    // Custom switch
    Box(
        modifier = Modifier
            .width(130.dp)
            .height(40.dp)
            .background(Color.Gray, shape = CircleShape)
            .clip(CircleShape)
            .clickable {
                // Error if now is not handle state
                if (screenState.current.actuatorState != ActuatorState.Handle) {
                    return@clickable
                }

                // Change direction and get state
                val state = when (screenState.current.temperatureActuatorState) {
                    TemperatureActuatorState.Hot -> {
                        if (directionRight.value) {
                            directionRight.value = false
                        }
                        TemperatureActuatorState.None
                    }

                    TemperatureActuatorState.Cold -> {
                        if (!directionRight.value) {
                            directionRight.value = true
                        }

                        TemperatureActuatorState.None
                    }

                    else -> if (!directionRight.value) TemperatureActuatorState.Cold else TemperatureActuatorState.Hot
                }

                // Change state
                screenAction.invoke(DetectorsDataEvent.ChangeTemperatureActuatorState(state))
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Thumb color
        val color = when (screenState.current.temperatureActuatorState) {
            TemperatureActuatorState.Hot -> Color.Red
            TemperatureActuatorState.Cold -> Color(77, 225, 255)
            else -> Color.White
        }

        // Thumb
        Box(
            modifier = Modifier
                .offset(x = offset.value)
                .height(30.dp)
                .width(50.dp)
                .clip(RoundedCornerShape(60.dp))
                .background(color)
        )
    }
}

@Composable
fun Graphic(data: List<DetectorData>) {
    if (data.isNotEmpty()) {
        val insideTemperatureParam = LineParameters(
            label = stringResource(R.string.inside_temperature),
            data = data.map { abs(it.insideTemperature.toDouble()) },
            lineColor = Color.Blue,
            lineShadow = true,
            lineType = LineType.DEFAULT_LINE
        )

        val outsideTemperatureParam = LineParameters(
            label = stringResource(R.string.outside_temperature),
            data = data.map { abs(it.outsideTemperature.toDouble()) },
            lineColor = Color.Green,
            lineShadow = true,
            lineType = LineType.DEFAULT_LINE
        )

        LineChart(
            animateChart = true,
            oneLineChart = false,
            linesParameters = listOf(
                insideTemperatureParam,
                outsideTemperatureParam
            ),
            xAxisData = data.map { it.dateTime.second.toString() },
            modifier = Modifier.fillMaxSize()
        )
    }
}