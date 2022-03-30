package com.abc.pythondemo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

open class Item1 : AbstractItem<Item1.ViewHolder>() {

    var title: String? = null
    var imgUrl: String? = null

    override val type: Int
        get() = R.id.fastadapter_sample_item_id
    override val layoutRes: Int
        get() = R.layout.item_inter

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<Item1>(view) {
        var title = view.findViewById<TextView>(R.id.itemTv)
        var img = view.findViewById<ImageView>(R.id.image)

        override fun bindView(item: Item1, payloads: List<Any>) {
            title.text = item.title
            Glide.with(view.context).load(item.imgUrl).into(img)
        }

        override fun unbindView(item: Item1) {
            title.text = null
            Glide.with(view.context).load("").into(img)
        }

    }
}