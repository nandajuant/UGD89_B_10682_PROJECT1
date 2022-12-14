package com.example.ugd89_b_10682_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.hardware.*
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity(){
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    private var currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK

    //Proximity
    //lateinit var sensorStatusTV: TextView
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            mCamera = Camera.open()
        } catch (e: Exception) {
            Log.d("error", "Failed to get Camera" + e.message)
        }
        if (mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        //Proximity
        //sensorStatusTV = findViewById(R.id.idTVSensorStatus)

        // on below line we are initializing our sensor manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // on below line we are initializing our proximity sensor variable
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        // on below line we are checking if the proximity sensor is null
        if (proximitySensor == null) {
            // on below line we are displaying a toast if no sensor is available
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            // on below line we are registering
            // our sensor with sensor manager
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }


        @SuppressLint("MissingInflaterId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? -> System.exit(0) }
    }

    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // method to check accuracy changed in sensor.
        }

        override fun onSensorChanged(event: SensorEvent) {
//            if (event.values[0] == 0f) {
//                    mCamera?.stopPreview()
//                    mCamera?.release()
//                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
//                    mCamera = Camera.open(currentCameraId);
//
//                    mCameraView = CameraView(this@MainActivity, mCamera!!)
//                    val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
//                    camera_view.addView(mCameraView)
//                } else {
//                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
//
//                }
            if (event.values[0] == 0f) {
                if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                    if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    } else {
                        currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    }

                    if (mCameraView != null) {
                        mCamera?.stopPreview();
                    }
                    mCamera?.release();
                    try {
                        mCamera = Camera.open(currentCameraId)
                    } catch (e: Exception) {
                        Log.d("Error", "Failed to get Camera" + e.message)
                    }
                    if (mCamera != null) {
                        mCameraView = CameraView(applicationContext, mCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(mCameraView)
                    }
                }
            }
        }
    }

}