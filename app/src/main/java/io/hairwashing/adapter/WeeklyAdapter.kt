package io.hairwashing.adapter

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.hairwashing.R
import io.hairwashing.dependences.TimeRange
import io.hairwashing.dependences.Hair
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class WeeklyAdapter(private val hair: Hair, private val timeRange: TimeRange) :
    RecyclerView.Adapter<WeeklyAdapter.ViewHolder>() {
    private lateinit var resources: Resources
    private var currentDate = LocalDate.now()
    private var currentDayOfWeek = currentDate.dayOfWeek
    private var nextWashingDay =  hair.getNextWashingDay()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        resources = parent.context.resources
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_weekly_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDayOfWeekField()
        holder.adjustWashingPermissionView()
        holder.setDate()
        updateCurrentDate()
        updateCurrentDayOfWeek()
    }

        private fun updateCurrentDate() { currentDate = currentDate.plusDays(1) }

        private fun updateCurrentDayOfWeek() { currentDayOfWeek = currentDayOfWeek.plus(1) }

    override fun getItemCount() = timeRange.days

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layout: ConstraintLayout = view.findViewById(R.id.weekly_layout)
        private val dayOfWeek: TextView = view.findViewById(R.id.week_day_text_view)
        private val washingDescription: TextView = view.findViewById(R.id.washing_permission_text_view)
        private val date: TextView = view.findViewById(R.id.date_text_view)

        fun setDayOfWeekField() {
            dayOfWeek.text = currentDayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }

        fun adjustWashingPermissionView() {
            when (currentDate) {
                hair.lastWashing -> {
                    washingDescription.setText(R.string.hair_already_washed)
                    layout.setBackgroundColor(resources.getColor(R.color.hair_already_washed))
                }
                nextWashingDay -> {
                    washingDescription.setText(R.string.wash_hair)
                    layout.setBackgroundColor(resources.getColor(R.color.wash_hair))
                    updateNextWashingDay()
                }
                else -> {
                    washingDescription.setText(R.string.do_not_wash_hair)
                    layout.setBackgroundColor(resources.getColor(R.color.do_not_wash_hair))
                }
            }
        }

            private fun updateNextWashingDay() { nextWashingDay = hair.getWashingDayAfter(currentDate) }

        fun setDate() {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
            date.text = currentDate.format(formatter)
        }
    }
}