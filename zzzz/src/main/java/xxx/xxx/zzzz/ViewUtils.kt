package xxx.xxx.zzzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
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

fun AppCompatActivity.next(clazz: Class<*>){
   startActivity(Intent(this,clazz))
}