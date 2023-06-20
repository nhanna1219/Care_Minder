package com.example.careminder.Activity.HealthConnect

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.records.*
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.*
import com.example.careminder.Activity.Food.Food
import com.example.careminder.Activity.Water.Water

import java.time.*

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

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


    suspend fun aggregateDailySteps() : Triple<String,String,String> {
        try {
            val today = ZonedDateTime.now()
            val startOfDay = today.truncatedTo(ChronoUnit.DAYS)
            val timeRangeFilter = TimeRangeFilter.between(
                startOfDay.toLocalDateTime(),
                today.toLocalDateTime()
            )
            val response =
                healthConnectClient.aggregate(
                    AggregateRequest(
                        metrics = setOf(StepsRecord.COUNT_TOTAL,
                                        DistanceRecord.DISTANCE_TOTAL,
                                        ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
                        timeRangeFilter = timeRangeFilter,
                    )
                )
                // The result may be null if no data is available in the time range.
            val totalSteps = response[StepsRecord.COUNT_TOTAL] ?: 0L
            val totalDistance = response[DistanceRecord.DISTANCE_TOTAL]?.inKilometers ?: 0F
            val totalCalories = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inKilocalories ?: 0F
//            val distanceString = totalDistance.toString().substring(0, totalDistance.toString().indexOf(" "))
//            val caloriesString = totalCalories.toString().substring(0, totalCalories.toString().indexOf(""))
            return Triple(totalSteps.toString(), totalDistance.toString(), totalCalories.toString())
        } catch (e: Exception) {
            // Run error handling here.
            e.printStackTrace()
        }
        return Triple("0", "0", "0",) ;
    }

    suspend fun aggregateDailySteps(today: ZonedDateTime) : Triple<String,String,String> {
        try {
            val startOfDay = today.truncatedTo(ChronoUnit.DAYS)
            val timeRangeFilter = TimeRangeFilter.between(
                startOfDay.toLocalDateTime(),
                today.toLocalDateTime()
            )
            val response =
                healthConnectClient.aggregate(
                    AggregateRequest(
                        metrics = setOf(StepsRecord.COUNT_TOTAL,
                            DistanceRecord.DISTANCE_TOTAL,
                            ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
                        timeRangeFilter = timeRangeFilter,
                    )
                )
            // The result may be null if no data is available in the time range.
            val totalSteps = response[StepsRecord.COUNT_TOTAL] ?: 0L
            val totalDistance = response[DistanceRecord.DISTANCE_TOTAL]?.inKilometers ?: 0F
            val totalCalories = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inKilocalories ?: 0F
//            val distanceString = totalDistance.toString().substring(0, totalDistance.toString().indexOf(" "))
//            val caloriesString = totalCalories.toString().substring(0, totalCalories.toString().indexOf(""))
            return Triple(totalSteps.toString(), totalDistance.toString(), totalCalories.toString())
        } catch (e: Exception) {
            // Run error handling here.
            e.printStackTrace()
        }
        return Triple("0", "0", "0",) ;
    }

    suspend fun aggregateWeight() : Triple<Double,Double,Double>{
        try {
            val today = ZonedDateTime.now()
            val last30Days = today.minusDays(30)
            val timeRangeFilter = TimeRangeFilter.between(
                last30Days.toLocalDateTime(),
                today.toLocalDateTime()
            )
            val response =
                healthConnectClient.aggregate(
                    AggregateRequest(
                        metrics = setOf(WeightRecord.WEIGHT_AVG,
                                        WeightRecord.WEIGHT_MAX,
                                        WeightRecord.WEIGHT_MIN),
                        timeRangeFilter = timeRangeFilter
                    )
                )
            val avgWeight = response[WeightRecord.WEIGHT_AVG]?.inKilograms ?: 0F
            val maxWeight = response[WeightRecord.WEIGHT_MAX]?.inKilograms ?: 0f
            val minHeight = response[WeightRecord.WEIGHT_MIN]?.inKilograms ?: 0f
            return Triple(avgWeight.toDouble(),maxWeight.toDouble(),minHeight.toDouble())
        } catch (e: Exception){
            e.printStackTrace()
        }
        return Triple(0.0,0.0,0.0)
    }

    suspend fun readWeightInput(): Double {
        val startTime = ZonedDateTime.now().minusDays(30).toInstant()
        val endTime = ZonedDateTime.now().toInstant()
        val request = ReadRecordsRequest(
            recordType = WeightRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
        )
        val response = healthConnectClient.readRecords(request)
        val sizeResponse = response.records.size
        val weightString = response.records[sizeResponse - 1].weight.toString()
        val weightValue = weightString.substring(0, weightString.indexOf(" "))
        return weightValue.toDouble()
    }

    suspend fun readHeightInput(): Double {
        val startTime = ZonedDateTime.now().minusDays(30).toInstant()
        val endTime = ZonedDateTime.now().toInstant()
        val request = ReadRecordsRequest(
            recordType = HeightRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
        )
        val response = healthConnectClient.readRecords(request)
        val sizeResponse = response.records.size
        val heightString = response.records[sizeResponse - 1].height.toString()
        val heightValue = heightString.substring(0, heightString.indexOf(" "))
        Log.d("readHeightInput:", "$heightValue")
        return heightValue.toDouble()
    }

    suspend fun writeWeightInput(weightInput: Double) {
        val uuid = UUID.randomUUID().toString()
        val version = System.currentTimeMillis()
        Log.d("UUID:", uuid)
        Log.d("Version:", version.toString())
        try {
            val time = ZonedDateTime.now().withNano(0)
            val weightRecord = WeightRecord(
                weight = Mass.kilograms(weightInput),
                time = time.toInstant(),
                zoneOffset = time.offset,
                metadata = Metadata(
                    clientRecordId = uuid,
                    clientRecordVersion = version
                )
            )
            val records = listOf(weightRecord)
            val response = healthConnectClient.insertRecords(records)
            Log.d("Insert weight: ", "Successfully")
        } catch (e: Exception) {
            Log.d("Insert weight: ", e.message.toString())
        }
    }

    suspend fun writeHeightInput(heightInput: Double) {
        val uuid = UUID.randomUUID().toString()
        val version = System.currentTimeMillis()
        Log.d("UUID:", uuid)
        Log.d("Version:", version.toString())

        try {
            val time = ZonedDateTime.now().minusSeconds(1)
            val heightRecord =  HeightRecord(
                height = Length.meters(heightInput),
                time = time.toInstant(),
                zoneOffset = time.offset,
                metadata = Metadata(
                    clientRecordId = uuid,
                    clientRecordVersion = version
                )
            )
            val records = listOf(heightRecord)

            healthConnectClient.insertRecords(records)
            Log.d("Insert height: ", "Successfully")
        } catch (e: Exception) {
            Log.d("Insert height: ", e.message.toString())
        }
    }

    suspend fun writeStepsInput(sec: Long, steps: Long, caloriesBurned: Double, distance: Double) {
        val uuid = UUID.randomUUID().toString()
        val version = System.currentTimeMillis()
        Log.d("UUID:", uuid)
        Log.d("Version:", version.toString())
        val second : Long
        try {
            if (sec == 0L) {
                second = 1
            } else {
                second = sec
            }
            val startTime = ZonedDateTime.now().minusSeconds(second).toInstant()
            val endTime = ZonedDateTime.now().toInstant()
            val records = listOf(
                StepsRecord(
                    metadata = Metadata(
                        clientRecordId = uuid,
                        clientRecordVersion = version
                    ),
                    count = steps,
                    startTime = startTime,
                    endTime = endTime,
                    startZoneOffset = null,
                    endZoneOffset = null,
                ),
                ActiveCaloriesBurnedRecord(
                    metadata = Metadata(
                        clientRecordId = uuid,
                        clientRecordVersion = version
                    ),
                    energy = Energy.kilocalories(caloriesBurned),
                    startTime = startTime,
                    endTime = endTime,
                    startZoneOffset = null,
                    endZoneOffset = null,
                ),
                DistanceRecord(
                    metadata = Metadata(
                        clientRecordId = uuid,
                        clientRecordVersion = version,
                    ),
                    distance = Length.meters(distance),
                    startTime = startTime,
                    endTime = endTime,
                    startZoneOffset = null,
                    endZoneOffset = null,
                )
            )

            healthConnectClient.insertRecords(records)
            Log.d("Insert steps: ", "Successfully")
            Log.d("Calories: ", caloriesBurned.toString())
            Log.d("Steps: ", steps.toString())


        } catch (e: Exception) {
            Log.d("Insert steps: ", e.message.toString())
        }
    }

    //Water
    suspend fun writeWaterInput(waterInput: Double) {

        val uuid = UUID.randomUUID().toString()
        val version = System.currentTimeMillis()
        Log.d("UUID:", uuid)
        Log.d("Version:", version.toString())

        try {
            val startTime = ZonedDateTime.now().minusSeconds(1L).toInstant()
            val endTime = ZonedDateTime.now().toInstant()
            val waterRecord =  HydrationRecord(
                startTime =  startTime,
                startZoneOffset =  null,
                endTime = endTime,
                endZoneOffset = null,
                volume =  Volume.milliliters(waterInput),
                metadata =  Metadata(
                    clientRecordId = uuid,
                    clientRecordVersion = version)
            )

            val records = listOf(waterRecord)

            healthConnectClient.insertRecords(records)
            Log.d("Insert water: ", "Successfully")
        } catch (e: Exception) {
            Log.d("Insert water: ", e.message.toString())
        }
    }

    // Water
    suspend fun readDailyRecords(client: HealthConnectClient) :Double{
        try {
            val today = ZonedDateTime.now()
            val startOfDay = today.truncatedTo(ChronoUnit.DAYS)
            val timeRangeFilter = TimeRangeFilter.between(
                startOfDay.toLocalDateTime(),
                today.toLocalDateTime()
            )
            val response = client.aggregate(
                AggregateRequest(
                    metrics = setOf(HydrationRecord.VOLUME_TOTAL),
                    timeRangeFilter = timeRangeFilter,
                )
            )
            val WaterToday = response[HydrationRecord.VOLUME_TOTAL]?.inMilliliters ?: 0F
            Log.d("Read daily water", WaterToday.toString())

            return WaterToday.toDouble();
        } catch (e: Exception){
            Log.d("Read daily water", e.message.toString())
        }
        return 0.0;
    }



    ///WRITE FOOD
    suspend fun writeFoodInput(foodInput: Food, mealType: Int) {
        val uuid = UUID.randomUUID().toString()
        val version = System.currentTimeMillis()
        Log.d("UUID:", uuid)
        Log.d("Version:", version.toString())

        try {
            val start = ZonedDateTime.now().minusSeconds(1L).toInstant()
            val end = ZonedDateTime.now().toInstant()
            val foodRecord =  NutritionRecord(
                startTime = start,
                startZoneOffset = null,
                endTime = end,
                endZoneOffset = null,
                energy = Energy.calories(foodInput.calories),
                name = foodInput.name,
                mealType = mealType,
                metadata = Metadata(
                    clientRecordId = uuid,
                    clientRecordVersion = version)
            )

            val records = listOf(foodRecord)

            healthConnectClient.insertRecords(records)
            Log.d("Insert FOOD: ", "Successfully")
        } catch (e: Exception) {
            Log.d("Insert FOOD: ", e.message.toString())
        }
    }

    ///read TOTAL CALORIES DAILY
    suspend fun readfoodDailyRecords(client: HealthConnectClient) :Double{
        try {
            val today = ZonedDateTime.now()
            val startOfDay = today.truncatedTo(ChronoUnit.DAYS)
            val timeRangeFilter = TimeRangeFilter.between(
                startOfDay.toLocalDateTime(),
                today.toLocalDateTime()
            )
            val response = client.aggregate(
                AggregateRequest(
                    metrics = setOf(NutritionRecord.ENERGY_TOTAL),
                    timeRangeFilter = timeRangeFilter,
                )
            )
            val foodToday = response[NutritionRecord.ENERGY_TOTAL]?.inCalories ?: 0.0
            Log.d("Read daily FOOD", foodToday.toString())

            return foodToday.toDouble();
        } catch (e: Exception){
            Log.d("Read daily FOOD", e.message.toString())
        }
        return 0.0;
    }

    suspend fun readFoodInputs(client: HealthConnectClient): List<NutritionRecord> {
        val today = ZonedDateTime.now()
        val startOfDay = today.truncatedTo(ChronoUnit.DAYS)
        val request = ReadRecordsRequest(
            recordType = NutritionRecord::class,
            timeRangeFilter = TimeRangeFilter.between( startOfDay.toLocalDateTime(),
                today.toLocalDateTime())
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }





    // function for delete personal data (Settings -> Advanced Setting -> Delete personal data)
    // Ref: https://developer.android.com/guide/health-and-fitness/health-connect/common-workflows/delete-data

    // Delete using Record IDs

//    suspend fun deleteStepsByUniqueIdentifier(
//        healthConnectClient: HealthConnectClient,
//        idList: List<String>
//    ) {
//        try {
//            healthConnectClient.deleteRecords(
//                StepsRecord::class,
//                idList = idList,
//                clientRecordIdsList = emptyList()
//            )
//        } catch (e: Exception) {
//            // Run error handling here
//        }
//    }
//    ExerciseSessionRecord::class,
//    StepsRecord::class,
//    TotalCaloriesBurnedRecord::class,
//    WeightRecord::class,
//    ActiveCaloriesBurnedRecord::class,
//    BodyFatRecord::class,
//    BodyWaterMassRecord::class,
//    BoneMassRecord::class,
//    ExerciseSessionRecord::class,
//    DistanceRecord::class,
//    SpeedRecord::class,
//    NutritionRecord::class,
//    HydrationRecord::class,
//    HeightRecord::class,
    // Delete using a time range
    suspend fun deleteStepsByTimeRange(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            healthConnectClient.deleteRecords(
                StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        } catch (e: Exception) {
            // Run error handling here
        }
    }

    suspend fun deleteWeight(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            healthConnectClient.deleteRecords(
                WeightRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        } catch (e: Exception) {
            // Run error handling here
        }
    }

    suspend fun deleteWater(
    healthConnectClient: HealthConnectClient,
    startTime: Instant,
    endTime: Instant
    ) {
        try {
            healthConnectClient.deleteRecords(
                HydrationRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        } catch (e: Exception) {
            // Run error handling here
        }
    }

    suspend fun deleteFood(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            healthConnectClient.deleteRecords(
                NutritionRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        } catch (e: Exception) {
            // Run error handling here
        }
    }

    suspend fun deleteCaloriesBurned(
    healthConnectClient: HealthConnectClient,
    startTime: Instant,
    endTime: Instant
    ) {
        try {
            healthConnectClient.deleteRecords(
                ActiveCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        } catch (e: Exception) {
            // Run error handling here
        }
    }
}