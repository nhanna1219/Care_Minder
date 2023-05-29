package com.example.careminder.Activity.HealthConnect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import com.example.careminder.Activity.Home.HomeActivity

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
        // Create the permissions launcher.
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
        val context: Context = applicationContext

        val requestPermissions =
            registerForActivityResult(requestPermissionActivityContract) { granted ->
                if (granted.containsAll(PERMISSIONS)) {
                    // Permissions successfully granted
                    // PERMISSIONS: Set<string> as of Alpha11
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // Lack of required permissions
                    Toast.makeText(applicationContext, "PERMISSIONS NOT GRANTED", Toast.LENGTH_LONG).show()
                }
            }
        requestPermissions.launch(PERMISSIONS)

        suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions already granted; proceed with inserting or reading data.
            } else {
                requestPermissions.launch(PERMISSIONS)
            }
        }
    }
}