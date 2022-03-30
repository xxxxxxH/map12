package xxx.xxx.zzzz

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.search.ResponseInfo
import com.mapbox.search.record.FavoriteRecord
import com.mapbox.search.record.HistoryRecord
import com.mapbox.search.result.SearchResult
import com.mapbox.search.ui.view.SearchBottomSheetView
import com.mapbox.search.ui.view.category.Category
import com.rw.loadingdialog.LoadingView
import jahirfiquitiva.libs.fabsmenu.TitleFAB

fun TitleFAB.click(block: () -> Unit) {
    setOnClickListener { block() }
}

fun MapView.currentLocation() {
    val listener = object : OnIndicatorPositionChangedListener {
        override fun onIndicatorPositionChanged(point: Point) {
            setTag(R.id.appViewMyLocationId, point)
            getMapboxMap().setCamera(CameraOptions.Builder().center(point).build())
            gestures.focalPoint = getMapboxMap().pixelForCoordinate(point)
            location.removeOnIndicatorPositionChangedListener(this)
        }
    }
    location.addOnIndicatorPositionChangedListener(listener)
}

fun MapView.setCameraClick(block: (Double, Double) -> Unit) {
    getMapboxMap().addOnMapClickListener {
        block(it.longitude(), it.latitude())
        true
    }
}

fun MapView.moveMap(p: Point) {
    getMapboxMap().flyTo(cameraOptions {
        center(p)
        zoom(14.0)
    })
}

fun MapView.loadStyle(style: String) {
    getMapboxMap().apply {
        setCamera(cameraOptions {
            zoom(14.0)
        })
        loadStyleUri(style) {
            addMyLocationPoint()
        }
    }
}

private fun MapView.addMyLocationPoint() {
    location.updateSettings {
        enabled = true
        pulsingEnabled = true
    }
}

fun MapView.setCameraChangeListener(block: (Double, Double) -> Unit) {
    getMapboxMap().addOnCameraChangeListener {
        getMapboxMap().cameraState.center.let {
            block(it.longitude(), it.latitude())
        }
    }
}


fun MapView.setCamera(center: Point) {
    getMapboxMap().setCamera(
        cameraOptions {
            center(center)
            zoom(14.0)
        }
    )
}

fun AppCompatActivity.next(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun AppCompatActivity.createLoading(): LoadingView {
    return LoadingView.Builder(this)
        .setProgressColorResource(R.color.orange)
        .setBackgroundColorRes(android.R.color.transparent)
        .setProgressStyle(LoadingView.ProgressStyle.HORIZONTAL)
        .setCustomMargins(0, 0, 0, 0)
        .attachTo(this)
}

fun SearchBottomSheetView.addOnCategory(
    savedInstanceState: Bundle?,
    block: (Category) -> Unit
) {
    initializeSearch(savedInstanceState, SearchBottomSheetView.Configuration())
    addOnCategoryClickListener {
        block(it)
    }
}

fun SearchBottomSheetView.addOnFavorite(block: (FavoriteRecord) -> Unit) {
    addOnFavoriteClickListener {
        block(it)
    }
}

fun SearchBottomSheetView.addOnHistory(block: (HistoryRecord) -> Unit) {
    addOnHistoryClickListener {
        block(it)
    }

}

fun SearchBottomSheetView.addOnSearchResult(block: (SearchResult, ResponseInfo) -> Unit) {
    addOnSearchResultClickListener { searchResult, responseInfo ->
        block(searchResult, responseInfo)
    }
}

fun AppCompatActivity.route2Result(it: Point, clazz: Class<*>) {
    startActivity(Intent(this, clazz).apply {
        putExtra("lat", it.latitude())
        putExtra("lng", it.longitude())
    })
}

fun MapView.addMarker(p: Point) {
    val bitmap: Bitmap = BitmapFactory.decodeResource(this.context.resources, R.drawable.red_marker)
    annotations.cleanup()
    val markerManager = annotations.createPointAnnotationManager(AnnotationConfig())
    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(p)
        .withIconImage(bitmap)
    markerManager.create(pointAnnotationOptions)
}

fun AppCompatActivity.createSpinner(data:ArrayList<String>, block: (Int, String) -> Unit){
    val sp = SpinnerDialog(this,data,"Select or Search","Close")
    sp.setCancellable(true)
    sp.setShowKeyboard(false)
    sp.bindOnSpinerListener { item, position ->
        block(position, item)
    }
    sp.showSpinerDialog()
}