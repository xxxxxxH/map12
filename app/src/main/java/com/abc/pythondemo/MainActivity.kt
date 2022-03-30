package com.abc.pythondemo

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.mapbox.maps.Style
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchOptions
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_float.*
import xxx.xxx.zzzz.*


@SuppressLint("UnsafeDynamicallyLoadedCode", "SdCardPath")
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : MyAct(R.layout.activity_main) {

    override fun initView() {
        root.addView(mapView)
        setMapCurrent()
        Nbbbb.get().lifeIsFkingMovie(this)
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
        router.click {
            next(RouterActivity::class.java)
        }
        inter.click {
            next(InterActivity::class.java)
        }
        near.click {
            fabs_menu.collapse()
            loadingView.show()
            getNearData({
                xxxxxxH("no data")
            }, {
                if (it.size == 0) {
                    xxxxxxH("no data")
                } else {
                    loadingView.hide()
                    createSpinner(it) { _, item ->
                        loadingView.show()
                        searchEngine.search(
                            item,
                            SearchOptions(),
                            callback
                        )
                    }
                }
            })
        }
    }

    override fun callBackError() {
        super.callBackError()
        loadingView.hide()
    }

    override fun callBackResult(
        suggestions: List<SearchSuggestion>,
        results: List<SearchResult>,
        responseInfo: ResponseInfo
    ) {
        super.callBackResult(suggestions, results, responseInfo)
        loadingView.hide()
        results.firstOrNull()?.coordinate?.let {
            route2Result(it, ResultActivity::class.java)
        } ?: kotlin.run {
            xxxxxxH("No suggestions found")
        }
    }

    override fun callBackSuggestions(
        suggestions: List<SearchSuggestion>,
        responseInfo: ResponseInfo
    ) {
        super.callBackSuggestions(suggestions, responseInfo)
        suggestions.firstOrNull()?.let {
            searchEngine.select(suggestions, callback)
        } ?: kotlin.run {
            loadingView.hide()
            xxxxxxH("No suggestions found")
        }
    }
}