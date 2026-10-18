package com.zaitxcode.android.hardware

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.zaitxcode.android.core.AppHelper

object Sensors {

    private fun getSensorManager(context: Context): SensorManager? {
        return context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    }

    @JvmStatic
    @JvmOverloads
    fun isAccelerometerAvailable(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return getSensorManager(context)?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
    }

    @JvmStatic
    @JvmOverloads
    fun isGyroscopeAvailable(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return getSensorManager(context)?.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null
    }

    @JvmStatic
    @JvmOverloads
    fun isBarometerAvailable(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return getSensorManager(context)?.getDefaultSensor(Sensor.TYPE_PRESSURE) != null
    }

    @JvmStatic
    @JvmOverloads
    fun isStepCounterAvailable(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return getSensorManager(context)?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
    }

    @JvmStatic
    @JvmOverloads
    fun getAmbientLightLux(context: Context = AppHelper.ctx()): Float {
        AppHelper.checkInitialized()
        var lux = -1f
        val manager = getSensorManager(context) ?: return lux
        val lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT) ?: return lux

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && event.values.isNotEmpty()) {
                    lux = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        try {
            manager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST)
            Thread.sleep(50)
            manager.unregisterListener(listener)
        } catch (_: Exception) {
            // Guard
        }
        return lux
    }
}
