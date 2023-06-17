package com.example.careminder.Activity.HealthConnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.lifecycle.lifecycleScope
import com.example.careminder.Activity.Home.HomeActivity
import com.example.careminder.Activity.Information.InformationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlin.math.round
import kotlin.math.roundToInt

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

    // Write steps record
    fun writeSteps(healthConnectClient: HealthConnectClient, steps: Long){
        val management = HealthConnectManagement(healthConnectClient)
        val cadence = 105
        val calsPerSteps = 0.04
        val avgStrideLength = 0.67
        val sec = steps / cadence * 60;
        val caloriesBurned = calsPerSteps * steps
        val distance = steps * avgStrideLength
        lifecycleScope.launch {
            management.writeStepsInput(sec ,steps, caloriesBurned, distance)
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
//            val weight = readWeight(healthConnectClient).toString().toDouble()
//            Log.d("getWeight: ", readWeight(healthConnectClient).toString())
//            writeSteps(healthConnectClient, sec, actualSteps, caloriesBurned, distance)
//        }
//    }

    ///water
    suspend fun writeWaterActivity(healthConnectClient: HealthConnectClient, water: Double){
        val management = HealthConnectManagement(healthConnectClient)
        management.writeWaterInput(water)
    }

    suspend fun readWater(healthConnectClient: HealthConnectClient, totalWater: TextView) {
        val management = HealthConnectManagement(healthConnectClient)
        val result = management.readDailyRecords(healthConnectClient).toString()
        totalWater.text = result
    }











}