package com.example.careminder.Activity.HealthConnect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.lifecycle.lifecycleScope
import com.example.careminder.Activity.Food.Food
import com.example.careminder.Activity.Home.HomeActivity
import com.example.careminder.Activity.Information.InformationActivity
import com.example.careminder.Data.CustomListViewAdapter
import com.example.careminder.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.coroutines.*
import java.time.Instant

import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round

class PermissionsRationaleActivity : AppCompatActivity() {
    // build a set of permissions for required data types
    // Each permission value is a string data type
    private val PERMISSIONS =
        setOf(
            HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
            HealthPermission.getWritePermission(ActiveCaloriesBurnedRecord::class),
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class),
            HealthPermission.getReadPermission(BodyFatRecord::class),
            HealthPermission.getWritePermission(BodyFatRecord::class),
            HealthPermission.getReadPermission(BodyWaterMassRecord::class),
            HealthPermission.getWritePermission(BodyWaterMassRecord::class),
            HealthPermission.getReadPermission(BoneMassRecord::class),
            HealthPermission.getWritePermission(BoneMassRecord::class),
            HealthPermission.getReadPermission(ExerciseSessionRecord::class),
            HealthPermission.getWritePermission(ExerciseSessionRecord::class),
            HealthPermission.getReadPermission(DistanceRecord::class),
            HealthPermission.getWritePermission(DistanceRecord::class),
            HealthPermission.getReadPermission(WeightRecord::class),
            HealthPermission.getWritePermission(WeightRecord::class),
            HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
            HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class),
            HealthPermission.getReadPermission(SpeedRecord::class),
            HealthPermission.getWritePermission(SpeedRecord::class),
            HealthPermission.getReadPermission(NutritionRecord::class),
            HealthPermission.getWritePermission(NutritionRecord::class),
            HealthPermission.getReadPermission(HydrationRecord::class),
            HealthPermission.getWritePermission(HydrationRecord::class),
            HealthPermission.getReadPermission(HeightRecord::class),
            HealthPermission.getWritePermission(HeightRecord::class),
        )
    private val db = Firebase.firestore
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissionsAndRun()
    }
    private fun checkPermissionsAndRun() {
        // Create the permissions launcher.
        val client =  HealthConnectClient.getOrCreate(this)

        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

        val requestPermissions =
            registerForActivityResult(requestPermissionActivityContract) { granted ->
                if (granted.containsAll(PERMISSIONS)) {
                    // Permissions successfully granted
                        onPermissionsAvailable(client)
                } else {
                    // Lack of required permissions
                    Toast.makeText(this, "Permissions Not Granted", Toast.LENGTH_SHORT).show()
                }
            }
        lifecycleScope.launch{
            val granted = client.permissionController.getGrantedPermissions()
            if (granted.containsAll(PERMISSIONS)) {
                onPermissionsAvailable(client)
            }
            else {
                requestPermissions.launch(PERMISSIONS)
            }
        }
    }


    private fun onPermissionsAvailable(healthConnectClient: HealthConnectClient) {
        val docRef = mAuth.currentUser?.let { db.collection("users").document(it.uid) }
        docRef?.get()?.addOnSuccessListener { document ->
            val gender = document.getString("gender")
            if (!document.contains("gender") || gender.isNullOrEmpty()){
                startActivity(Intent(this, InformationActivity::class.java))
            }
            else
                startActivity(Intent(this, HomeActivity::class.java))
        }
    }


    // Read steps aggregate
    fun loadDailyData(healthConnectClient: HealthConnectClient, steps: TextView, distance: TextView, caloriesBurned: TextView, duration: TextView) {
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            val (TOTAL_STEPS, TOTAL_DISTANCE, TOTAL_CALORIES_BURNED) = management.aggregateDailySteps()
            val totalCalories = round(TOTAL_CALORIES_BURNED.toDouble() * 100) / 100
            val caloriesString = "$totalCalories  (kcal)"
            caloriesBurned.text = caloriesString

            val stepsString = "$TOTAL_STEPS  (steps)"
            steps.text = stepsString

            val distanceInKM = round(TOTAL_DISTANCE.toDouble() * 100) / 100
            distance.text = distanceInKM.toString()

            val TOTAL_DURATION = round((TOTAL_STEPS.toDouble() / 105) * 100) / 100
            val durationString = "$TOTAL_DURATION  (min)"
            duration.text = durationString
        }
    }

    fun loadDailyData(healthConnectClient: HealthConnectClient, steps: TextView, distance: TextView, caloriesBurned: TextView, duration: TextView, today: ZonedDateTime) {
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            val (TOTAL_STEPS, TOTAL_DISTANCE, TOTAL_CALORIES_BURNED) = management.aggregateDailySteps(today)
            val totalCalories = round(TOTAL_CALORIES_BURNED.toDouble() * 100) / 100
            val caloriesString = "$totalCalories  (kcal)"
            caloriesBurned.text = caloriesString

            val stepsString = "$TOTAL_STEPS  (steps)"
            steps.text = stepsString

            val distanceInKM = round(TOTAL_DISTANCE.toDouble() * 100) / 100
            distance.text = distanceInKM.toString()

            val TOTAL_DURATION = round((TOTAL_STEPS.toDouble() / 105) * 100) / 100
            val durationString = "$TOTAL_DURATION  (min)"
            duration.text = durationString
        }
    }

    fun loadDailyData(healthConnectClient: HealthConnectClient, stepsCounting: TextView, steps: TextView, caloriesBurned: TextView, duration: TextView, temp: Int) {
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            val (TOTAL_STEPS, TOTAL_DISTANCE, TOTAL_CALORIES_BURNED) = management.aggregateDailySteps()
            val totalCalories = round(TOTAL_CALORIES_BURNED.toDouble() * 100) / 100
            val caloriesString = "$totalCalories"
            steps.text = TOTAL_STEPS
            stepsCounting.text = TOTAL_STEPS
            caloriesBurned.text = caloriesString
            val TOTAL_DURATION = round((TOTAL_STEPS.toDouble() / 105) * 100) / 100
            val durationString = "$TOTAL_DURATION"
            duration.text = durationString
        }
    }

    fun readAggregateWeight(healthConnectClient: HealthConnectClient, weight: TextView) {
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            val (avgWeight,maxWeight,minWeight) = management.aggregateWeight()
            val avgWeightString = round(avgWeight * 100) / 100
            val content = "In the past 30 days, your weight falls within a specific range of <b>$minWeight kg</b> - <b>$maxWeight kg</b>, " +
                    "and the average weight is calculated to be <b>$avgWeightString kg</b>."
            weight.text = (Html.fromHtml(content))
        }
    }
    private fun evaluateBMI(bmi: Double): Pair<String, String> {
        var result = ""
        var resultContent = ""
        when {
            bmi < 18.5 -> {
                result = "Underweight 😭"
                // Recommendation for underweight
                resultContent += "\nYou may want to consider increasing your calorie intake and incorporating strength training exercises to gain weight in a healthy way."
            }
            bmi < 25 -> {
                result = "Normal Weight 🎉"
                // Recommendation for normal weight
                resultContent += "\nCongratulations! You are within a healthy weight range. Keep up the good work by maintaining a balanced diet and regular exercise routine."
            }
            bmi < 30 -> {
                result = "Overweight 😞"
                // Recommendation for overweight
                resultContent += "\nYou may want to consider reducing your calorie intake and increasing your physical activity levels to lose weight in a healthy way."
            }
            else -> {
                result = "Obese 👨‍"
                // Recommendation for obese
                resultContent += "\nYou may want to consult with a healthcare professional to create a personalized plan for losing weight and improving your overall health."
            }
        }
        return Pair(result, resultContent)
    }
    fun readBasicInformation(healthConnectClient: HealthConnectClient, weight: TextView, height: TextView, BMI: TextView, result: TextView, resultContent: TextView) {
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            val weightInput = management.readWeightInput()
            val heightInput = management.readHeightInput()
            weight.text = "$weightInput kilogram"
            height.text = "$heightInput meters"
            val bmi = round((weightInput / heightInput.pow(2)) * 100) / 100
            BMI.text = bmi.toString()
            val (rs, rsContent) = evaluateBMI(bmi)
            result.text = rs
            resultContent.text = rsContent
        }
    }

    fun readBasicInformation(healthConnectClient: HealthConnectClient, weight: TextView, height: TextView) {
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            val weightInput = management.readWeightInput()
            val heightInput = management.readHeightInput()
            weight.text = "$weightInput"
            height.text = "$heightInput"
        }
    }

    // Write steps record
    fun writeSteps(healthConnectClient: HealthConnectClient, steps: Long){
        val management = HealthConnectManagement(healthConnectClient)
        val cadence = 105
        val calsPerSteps = 0.04
        val avgStrideLength = 0.67
        val sec = round(steps.toDouble() / cadence.toDouble() * 60.0).toLong()
        val caloriesBurned = calsPerSteps * steps
        val distance = steps * avgStrideLength
        lifecycleScope.launch {
            management.writeStepsInput(sec,steps, caloriesBurned, distance)
        }
    }

    // Write weight and height data
    fun writeBasicInformation(healthConnectClient: HealthConnectClient, height: Double, weight: Double){
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            management.writeHeightInput(height)
            management.writeWeightInput(weight)
        }
    }

    //     Read weight
    suspend fun readWeight(healthConnectClient: HealthConnectClient): Double = withContext(Dispatchers.IO) {
            val management = HealthConnectManagement(healthConnectClient)
            return@withContext management.readWeightInput()
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    fun readWeightWriteStep(healthConnectClient: HealthConnectClient, actualSteps: Long) {
//        GlobalScope.launch {
//            val dailyWater = readWeight(healthConnectClient).toString().toDouble()
//            Log.d("getWeight: ", readWeight(healthConnectClient).toString())
//            writeSteps(healthConnectClient, sec, actualSteps, caloriesBurned, distance)
//        }
//    }

    ///water
    fun writeWaterActivity(healthConnectClient: HealthConnectClient, water: Double, totalWater: TextView, circular: CircularFillableLoaders){
        lifecycleScope.launch {
            val management = HealthConnectManagement(healthConnectClient)
            management.writeWaterInput(water)
            readWater(healthConnectClient,totalWater, circular)
        }
    }

    fun readWater(healthConnectClient: HealthConnectClient, totalWater: TextView, circular: CircularFillableLoaders) {
        lifecycleScope.launch {
            val management = HealthConnectManagement(healthConnectClient)
            val result = management.readDailyRecords(healthConnectClient)
            val waterDouble = round(result * 100) / 100
            totalWater.text = waterDouble.toString()
            val maxWater = 3000
            var waterProgress = abs(100 -(waterDouble.toInt() * 100 / maxWater))
            if (waterProgress > 100 || waterDouble > 3000) {
                waterProgress = 0
            }
            try{
                updateProgress(waterProgress, circular)
            } catch (e: Exception) {
                Log.d("Water Progress", e.message.toString())
            }
        }
    }
    private suspend fun updateProgress(waterProgress: Int, circular: CircularFillableLoaders) {
        withContext(Dispatchers.Main) { // switch to the main thread
            circular.setProgress(waterProgress)
            Log.d("Water Progress", "$waterProgress")
        }
    }
    fun readWater(healthConnectClient: HealthConnectClient, totalWater: TextView) {
        lifecycleScope.launch {
            val management = HealthConnectManagement(healthConnectClient)
            val result = management.readDailyRecords(healthConnectClient)
            val waterDouble = round(result / 250).toInt()
            totalWater.text = waterDouble.toString()
        }
    }
    fun writeWaterActivity(healthConnectClient: HealthConnectClient, water: Double, totalWater: TextView){
        lifecycleScope.launch {
            val management = HealthConnectManagement(healthConnectClient)
            management.writeWaterInput(water)
            readWater(healthConnectClient,totalWater)
        }
    }

    ///// FOOD


    fun readFood(healthConnectClient: HealthConnectClient, totalCalories: TextView) {
        lifecycleScope.launch {
            val management = HealthConnectManagement(healthConnectClient)
            val result = management.readfoodDailyRecords(healthConnectClient)
            val foodDouble = round(result * 100) / 100
            totalCalories.text = foodDouble.toString()

        }
    }
    fun writeFoodActivity(healthConnectClient: HealthConnectClient, food: Food, mealType: Int, totalCalories: TextView, listView: ListView, context: Activity){
        lifecycleScope.launch {
            val management = HealthConnectManagement(healthConnectClient)
            management.writeFoodInput(food, mealType)
            readFood(healthConnectClient,totalCalories)
            // Write and read in the same time
            readListFood(healthConnectClient, listView, context)
        }
    }


    fun readListFood(healthConnectClient: HealthConnectClient, listView: ListView, context: Activity){
        val dbFoods = ArrayList<Food>()
        lifecycleScope.launch {
            val management = HealthConnectManagement(healthConnectClient)
            val nutritionRecords = management.readFoodInputs(healthConnectClient)
            var i = 1;
            for (nutritionRecord in nutritionRecords) {
                var id = 1
                val nutritionName = nutritionRecord.name.toString()
                val nutritionCalo = nutritionRecord.energy?.inCalories?: 0.0
                val mealType = nutritionRecord.mealType
                var mealName: String
                mealName = when (mealType) {
                    1 -> "Breakfast"
                    2 -> "Lunch"
                    4 -> "Snack"
                    3 -> "Dinner"
                    else -> "unknown"
                }
                val food = Food(nutritionName, nutritionCalo, mealName, id)
                dbFoods.add(food)
                i++
            }
            val foodAdapterTemp = CustomListViewAdapter(context, R.layout.list_item, dbFoods)
            listView.adapter = foodAdapterTemp
            foodAdapterTemp.notifyDataSetChanged()
        }
    }

    // function for delete personal data (deleteStepsByTimeRange) in file HealthConnectManagement
    fun deletePersonalData(healthConnectClient: HealthConnectClient, start: Instant, end: Instant){
        val management = HealthConnectManagement(healthConnectClient)
        lifecycleScope.launch {
            management.deleteStepsByTimeRange(healthConnectClient, start, end)
            management.deleteWeight(healthConnectClient, start, end)
            management.deleteWater(healthConnectClient, start, end)
            management.deleteFood(healthConnectClient, start, end)
            management.deleteCaloriesBurned(healthConnectClient, start, end)
        }
    }

}