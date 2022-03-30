package com.abc.pythondemo

import android.annotation.SuppressLint
import android.view.MotionEvent
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import kotlinx.android.synthetic.main.activity_street.*
import xxx.xxx.zzzz.currentLocation
import xxx.xxx.zzzz.loadStyle
import xxx.xxx.zzzz.setCamera
import xxx.xxx.zzzz.setCameraChangeListener

@SuppressLint("ClickableViewAccessibility", "Lifecycle")
class StreetActivity : MyAct(R.layout.activity_street) {
    private var isTopTouch = false
    private var isBottomTouch = false
    private val mapview1 by lazy {
        MapView(this)
    }
    private val mapview2 by lazy {
        MapView(this)
    }

    override fun initView() {
        map1.addView(mapview1)
        map2.addView(mapview2)
        mapview1.let {
            it.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    isTopTouch = true
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    isTopTouch = false
                }
                false
            }
            it.currentLocation()
            it.setCameraChangeListener { d, d2 ->
                if (isTopTouch && !isBottomTouch) {
                    mapview2.setCamera(Point.fromLngLat(d, d2))
                }
            }
            it.loadStyle(Style.OUTDOORS)
        }

        mapview2.let {
            it.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    isBottomTouch = true
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    isBottomTouch = false
                }
                false
            }

            it.currentLocation()
            it.setCameraChangeListener { lng, lat ->
                if (!isTopTouch && isBottomTouch) {
                    mapview1.setCamera(Point.fromLngLat(lng, lat))
                }
            }
            it.loadStyle(Style.SATELLITE_STREETS)
        }
    }


    override fun onStart() {
        super.onStart()
        mapview1.onStart()
        mapview2.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapview1.onStop()
        mapview2.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapview1.onDestroy()
        mapview2.onDestroy()
    }
}