package com.abc.pythondemo

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_near.*
import xxx.xxx.zzzz.getInterActiveData
import xxx.xxx.zzzz.xxxxxxH

class InterActivity : MyAct(R.layout.activity_near) {
    override fun initView() {
        loadingView.show()
        getInterActiveData({
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
                recycler.layoutManager = LinearLayoutManager(this@InterActivity)
                loadingView.hide()
                fastAdapter.onClickListener = { view, adapter, item, position ->
                    val i = Intent(this@InterActivity, DetailsActivity::class.java)
                    i.putExtra("data", it[position])
                    startActivity(i)
                    false
                }
            }
        })
    }
}