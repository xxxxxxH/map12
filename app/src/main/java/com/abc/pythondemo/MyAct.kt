package com.abc.pythondemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchMultipleSelectionCallback
import com.mapbox.search.SearchSelectionCallback
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.mapbox.search.ui.view.SearchBottomSheetView
import xxx.xxx.zzzz.*

@SuppressLint("Lifecycle")
abstract class MyAct(id:Int) : AppCompatActivity(id){

    val mapView by lazy {
        MapView(this)
    }

    val mapBox by lazy {
        mapView.getMapboxMap()
    }

    val bottomSheet by lazy {
        SearchBottomSheetView(this)
    }

    val searchEngine by lazy {
        MapboxSearchSdk.getSearchEngine()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    abstract fun initView()

    fun setMapCurrent(){
        mapView.currentLocation()
        mapView.setCameraClick{lng,lat->
            mapView.moveMap(Point.fromLngLat(lng,lat))
        }
        mapView.loadStyle(Style.OUTDOORS)
    }

    fun setCameraChangeListener(mapView1: MapView,mapView2: MapView, b:Boolean,b2:Boolean){
        mapView1.setCameraChangeListener { d, d2 ->
            if (b &&!b2){
                mapView2.setCamera(Point.fromLngLat(d, d2))
            }
        }
    }

    fun mapview1OnTouch(){

    }


    inner class searchCallback: SearchSelectionCallback, SearchMultipleSelectionCallback{
        override fun onCategoryResult(
            suggestion: SearchSuggestion,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
            TODO("Not yet implemented")
        }

        override fun onError(e: Exception) {
            callBackError()
        }

        override fun onResult(suggestion: SearchSuggestion, result: SearchResult, responseInfo: ResponseInfo) {
            callBackResult()
        }

        override fun onSuggestions(suggestions: List<SearchSuggestion>, responseInfo: ResponseInfo) {
            callBackSuggestions()
        }

        override fun onResult(
            suggestions: List<SearchSuggestion>,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {

        }

    }

    open fun callBackError(){}

    open fun callBackResult(){}

    open fun callBackSuggestions(){}

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}