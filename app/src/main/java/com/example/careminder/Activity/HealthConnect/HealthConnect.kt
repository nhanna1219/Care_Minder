package com.example.careminder.Activity.HealthConnect

import androidx.health.connect.client.HealthConnectClient
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.careminder.Activity.Home.HomeActivity
import com.example.careminder.Activity.Information.InformationActivity

open class HealthConnect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the application context
        val context: Context = applicationContext

        // Call the sdkStatus() function with the context and providerPackageName parameters
        val providerPackageName = "com.google.android.apps.healthdata"

        val availabilityStatus = HealthConnectClient.sdkStatus(context, providerPackageName)
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return // early return as there is no viable integration
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            // Optionally redirect to package installer to find a provider, for example:
            val uriString =
                "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
            startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", context.packageName)
                }
            )
            return
        }

        startActivity(Intent(this,InformationActivity::class.java))

    }
}