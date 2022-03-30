package com.abc.pythondemo

import android.os.Bundle
import android.widget.RelativeLayout
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchOptions
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import kotlinx.android.synthetic.main.activity_router.*
import xxx.xxx.zzzz.*

class RouterActivity : MyAct(R.layout.activity_router) {

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        val p = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
        bottomSheet.layoutParams = p
        root.addView(bottomSheet)
        bottomSheet.apply {
            addOnCategory(savedInstanceState){
                loadingView.show()
                searchEngine.search(
                    it.geocodingCanonicalName,
                    SearchOptions(),
                    callback
                )
            }
            addOnFavorite {
                loadingView.show()
                route2Result(it.coordinate, ResultActivity::class.java)
            }
            addOnHistory {
                loadingView.show()
                it.coordinate?.let {point->
                    route2Result(point, ResultActivity::class.java)
                } ?: kotlin.run {
                    searchEngine.search(
                        it.name,
                        SearchOptions(),
                        callback
                    )
                }
            }
            addOnSearchResult { searchResult, responseInfo ->
                loadingView.show()
                searchResult.coordinate?.let {
                    route2Result(it, ResultActivity::class.java)
                } ?: kotlin.run {
                    searchEngine.search(
                        searchResult.name,
                        SearchOptions(),
                        callback
                    )
                }
            }
        }
    }

    override fun initView() {

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