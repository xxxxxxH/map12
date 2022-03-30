package com.abc.pythondemo

import androidx.recyclerview.widget.LinearLayoutManager
import com.mapbox.geojson.Point
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_details.*
import xxx.xxx.zzzz.*

class DetailsActivity : MyAct(R.layout.activity_details) {
    val url = "https://www.google.com/streetview/feed/gallery/collection/"

    private val bigPlace by lazy {
        intent.getSerializableExtra("data") as DataEntity
    }

    override fun initView() {
        if (bigPlace == null) return
        root.addView(mapView)
        setMapCurrent()
        loadingView.show()
        val u = url + bigPlace.key + ".json"
        getDetailsData(u, bigPlace, {
            xxxxxxH("no data")
            finish()
        }, {
            if (it.size == 0) {
                xxxxxxH("no data")
                finish()
            } else {
                val itemAdapter = ItemAdapter<Item1>()
                val fastAdapter = FastAdapter.with(itemAdapter)
                val items = ArrayList<Item1>()
                it.forEach { entity ->
                    val i = Item1()
                    i.title = entity.title
                    i.imgUrl = entity.imageUrl
                    items.add(i)
                }
                itemAdapter.add(items)
                recycler.adapter = fastAdapter
                recycler.layoutManager = LinearLayoutManager(this@DetailsActivity)
                loadingView.hide()
                fastAdapter.onClickListener = { view, adapter, item, position ->
                    val entity = it[position]
                    mapView.moveMap(Point.fromLngLat(entity.lng, entity.lat))
                    mapView.addMarker(Point.fromLngLat(entity.lng, entity.lat))
                    false
                }
            }
        })
    }
}