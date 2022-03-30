package com.abc.pythondemo

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.mapbox.maps.Style
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_float.*
import xxx.xxx.zzzz.click
import xxx.xxx.zzzz.loadStyle
import xxx.xxx.zzzz.next


@SuppressLint("UnsafeDynamicallyLoadedCode", "SdCardPath")
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : MyAct(R.layout.activity_main) {

    override fun initView() {
        root.addView(mapView)
        setMapCurrent()
        mapOutdoor.click {
            mapView.loadStyle(Style.OUTDOORS)
        }
        mapSatellite.click {
            mapView.loadStyle(Style.SATELLITE)
        }
        mapLight.click {
            mapView.loadStyle(Style.LIGHT)
        }
        mapTraffic.click {
            mapView.loadStyle(Style.TRAFFIC_DAY)
        }
        streetView.click {
            next(StreetActivity::class.java)
        }
    }
}