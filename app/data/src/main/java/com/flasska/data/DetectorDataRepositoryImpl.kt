package com.flasska.data

import com.flasska.domain.entity.ActuatorState
import com.flasska.domain.entity.DetectorData
import com.flasska.domain.entity.TemperatureActuatorState
import com.flasska.domain.repository.DetectorDataRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.ZoneOffset

class DetectorDataRepositoryImpl : DetectorDataRepository {
    private val _state = MutableStateFlow(emptyList<DetectorData>())
    override val state = _state.asStateFlow()

    private val reference = FirebaseDatabase.getInstance().reference

    init {
        reference.child("dataobjects")
            .addValueEventListener(
                valueEventListener()
            )
    }

    override suspend fun updateTemperatureActuatorState(value: TemperatureActuatorState) {
        reference.child("tas").setValue(value.name)
    }

    private fun valueEventListener() = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.getValue<HashMap<String, DetectorDataFirebase>>()
                ?.values?.let { ddfs ->
                    val newValue = ddfs.sortedBy { it.time }
                        .takeLast(15)
                        .mapNotNull { ddf ->
                            mapResponse(ddf)
                        }

                    _state.update { newValue }
                }
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    private fun mapResponse(ddf: DetectorDataFirebase): DetectorData? {
        val actuatorState = ActuatorState.deserialize(ddf.actuatorState)
        val tempActuatorState =
            TemperatureActuatorState.deserialize(ddf.temperatureActuatorState)

        return if (actuatorState != null && tempActuatorState != null) {
            DetectorData(
                insideTemperature = ddf.insideTemperature,
                outsideTemperature = ddf.outsideTemperature,
                actuatorState = actuatorState,
                temperatureActuatorState = tempActuatorState,
                dateTime = LocalDateTime.ofEpochSecond(
                    ddf.time,
                    0,
                    ZoneOffset.UTC
                )
            )
        } else {
            null
        }
    }
}