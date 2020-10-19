package io.tl.mvp.lib.toolbar

import android.util.Log
import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface SearchingToolbar {
    enum class SearchEvent{
        QueryTextChanged,
        SearchClicked,
        QueryTextSubmitted,
        FocusChanged
    }

    val searchEventsPublishSubject : PublishSubject<Pair<SearchEvent, Any?>>
    var searchView: SearchView

    fun initSearchingToolbar(toolbarCenterView: SearchView){

        toolbarCenterView.let { aSearchView ->
            this.searchView = aSearchView

            aSearchView.isIconified = true

            aSearchView.setOnQueryTextFocusChangeListener{_, isFocused->
                searchEventsPublishSubject.onNext(Pair(SearchEvent.FocusChanged, isFocused))
            }

            aSearchView.setOnSearchClickListener{
                Log.d("toolbarCenterView", "search clicked")
                searchEventsPublishSubject.onNext(Pair(SearchEvent.SearchClicked, null))
            }

            aSearchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        Log.d("toolbarCenterView", "query submitted $query")

                        searchEventsPublishSubject.onNext(Pair(SearchEvent.QueryTextSubmitted, query))
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        Log.d("toolbarCenterView", "new text $newText")
                        searchEventsPublishSubject.onNext(Pair(SearchEvent.QueryTextChanged, newText))

                        return true
                    }
                }
            )

        }
    }

    fun onSearchEvents(): Observable<Pair<SearchEvent, Any?>> = searchEventsPublishSubject
}