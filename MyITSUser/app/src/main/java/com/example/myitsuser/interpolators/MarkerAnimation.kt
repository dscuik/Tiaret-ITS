package com.example.myitsuser.interpolators


import android.os.Handler
import android.os.SystemClock
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

object MarkerAnimation {

    fun animateMarkerToGB(marker: Marker, finalPosition: LatLng, latLngInterpolator: LatLngInterpolator) {
        val startPosition = marker.position
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = 2000f

        handler.post(object : Runnable {
            var elapsed: Long = 0
            var t: Float = 0.toFloat()
            var v: Float = 0.toFloat()
            override fun run() {
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed / durationInMs
                v = interpolator.getInterpolation(t)
                marker.position = latLngInterpolator.interpolate(v, startPosition, finalPosition)
                if (t < 1) {
                    handler.postDelayed(this, 16)
                }
            }
        })
    }
}