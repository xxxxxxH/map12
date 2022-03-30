package com.abc.pythondemo

import com.mapbox.geojson.Point
import kotlinx.android.synthetic.main.activity_result.*
import xxx.xxx.zzzz.addMarker
import xxx.xxx.zzzz.moveMap

class ResultActivity : MyAct(R.layout.activity_result) {
    private val lat by lazy {
        intent.getDoubleExtra("lat", 0.0)
    }
    private val lng by lazy {
        intent.getDoubleExtra("lng", 0.0)
    }

    override fun initView() {
        root.addView(mapView)
        if (lat == 0.0 || lng == 0.0) {
            finish()
        }
        setMapCurrent()
        mapView.let {
            it.addMarker(Point.fromLngLat(lng, lat))
            it.moveMap(Point.fromLngLat(lng, lat))
        }
    }

}