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
import com.example.careminder.Activity.Daily.DailyActivity
import com.example.careminder.Activity.Home.HomeActivity
import com.example.careminder.R
import kotlinx.coroutines.launch
import java.time.LocalDateTime

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissionsAndRun()
    }
    private fun checkPermissionsAndRun() {
        val client = HealthConnectClient.getOrCreate(this)
        // Create the permissions launcher.
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

        val requestPermissions =
            registerForActivityResult(requestPermissionActivityContract) { granted ->
                if (granted.containsAll(PERMISSIONS)) {
                    // Permissions successfully granted
                    // PERMISSIONS: Set<string> as of Alpha11
                    lifecycleScope.launch {
                        onPermissionsAvailable(client)
                    }
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
    suspend fun onPermissionsAvailable(healthConnectClient: HealthConnectClient) {

    }


    suspend fun loadDailyData(healthConnectClient: HealthConnectClient, steps: TextView) {
        val management: HealthConnectManagement = HealthConnectManagement(healthConnectClient)
        val result = management.aggregateStepsIntoMinutes()
        Log.d("PermissionRationaleActivity", "checkPermissionsAndRun: $result")
        steps.text = result
    }


}