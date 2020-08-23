package io.hairwashing.structure.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.R
import io.hairwashing.TimeRange
import io.hairwashing.tools.adapter.WeeklyAdapter

class WeeklyFragment : Fragment() {
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weekly_list, container, false)
        if (view is RecyclerView) {
            recyclerView = view
            view.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically() = false
            }
            view.adapter = WeeklyAdapter(Hair.asDefault(), TimeRange.ONE_WEEK)
        }
        return view
    }

    fun updateAdapter(hair: Hair, timeRange: TimeRange) {
        recyclerView?.adapter = WeeklyAdapter(hair, timeRange)
    }
}