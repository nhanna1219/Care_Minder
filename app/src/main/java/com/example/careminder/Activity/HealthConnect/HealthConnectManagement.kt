package com.example.careminder.Activity.HealthConnect

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.changes.Change
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.*
import kotlinx.coroutines.future.future
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.util.concurrent.CompletableFuture

class HealthConnectManagement(private val healthConnectClient: HealthConnectClient) {

    suspend fun getChangesToken(): String {
        return healthConnectClient.getChangesToken(
            ChangesTokenRequest(
                setOf(
                    ExerciseSessionRecord::class,
                    StepsRecord::class,
                    TotalCaloriesBurnedRecord::class,
                    WeightRecord::class,
                    ActiveCaloriesBurnedRecord::class,
                    BodyFatRecord::class,
                    BodyWaterMassRecord::class,
                    BoneMassRecord::class,
                    ExerciseSessionRecord::class,
                    DistanceRecord::class,
                    SpeedRecord::class,
                    NutritionRecord::class,
                    HydrationRecord::class,
                    HeightRecord::class,
                )
            )
        )
    }

    private fun processUpsertionChange(change: UpsertionChange) {
        // Filter out data inserts or updates from the app itself
        when (val record = change.record) {
            is StepsRecord -> {
                // Use this value to distinguish inserts from updates
                val stepCount = record.count
            }
        }
    }

    suspend fun processChanges(token: String): String {
        var nextChangesToken = token
        do {
            val response = healthConnectClient.getChanges(nextChangesToken)
            response.changes.forEach { change ->
                when (change) {
                    is UpsertionChange -> processUpsertionChange(change)
                }
            }
            nextChangesToken = response.nextChangesToken
        } while (response.hasMore)
        return nextChangesToken
    }


    suspend fun aggregateStepsIntoMinutes() : String {
        try {
            val startTime = LocalDateTime.of(2023, 6, 2, 6, 6)
            val endTime = LocalDateTime.of(2023, 6, 2, 23, 59)
            val response =
                healthConnectClient.aggregateGroupByDuration(
                    AggregateGroupByDurationRequest(
                        metrics = setOf(StepsRecord.COUNT_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
                        timeRangeSlicer = Duration.ofMinutes(20)
                    )
                )
            for (dailyResult in response) {
                // The result may be null if no data is available in the time range.
                val totalSteps = dailyResult.result[StepsRecord.COUNT_TOTAL]
                return totalSteps.toString();
            }
        } catch (e: Exception) {
            // Run error handling here.
            e.printStackTrace()
        }
        return "0";
    }
}