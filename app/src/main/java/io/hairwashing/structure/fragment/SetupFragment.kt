package io.hairwashing.structure.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.R
import io.hairwashing.structure.dependences.TimeRange
import kotlinx.android.synthetic.main.fragment_setup.*
import java.time.LocalDate

class SetupFragment : Fragment() {
    var hair: Hair = Hair.asDefault()
    var timeRange: TimeRange = TimeRange.ONE_WEEK
    var listener: Listener? = null

    interface Listener {
        fun updateConfig()
        fun updateWeeklyAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            hair = Hair.fromDB(it)
            timeRange = TimeRange.fromDB(it)
        }
    }

    override fun onStart() {
        super.onStart()
        updateTypeButtonImage()
        updateTimeRangeButtonImage()
        updateLengthButtonImage()
        updateClimateButtonImage()
        updateTypeTextView()
        updateTimeRangeTextView()
        updateLengthTextView()
        updateClimateTextView()
        hair_type_button.setOnClickListener { onClickTypeButton() }
        time_range_button.setOnClickListener { onClickTimeRangeButton() }
        hair_length_button.setOnClickListener { onClickLengthButton() }
        hair_climate_button.setOnClickListener { onClickClimateButton() }
    }

    private fun updateTypeButtonImage() {
        val drawableRes = when(hair.type) {
            Hair.Type.DRY -> R.drawable.dry_hair
            Hair.Type.REGULAR -> R.drawable.regular_hair
            else -> R.drawable.oily_hair
        }
        hair_type_button.setImageDrawable(context?.getDrawable(drawableRes))
    }

    private fun updateTimeRangeButtonImage() {
        val drawableRes = when(timeRange) {
            TimeRange.ONE_WEEK -> R.drawable.one_week
            TimeRange.TWO_WEEKS -> R.drawable.two_weeks
            else -> R.drawable.month
        }
        time_range_button.setImageDrawable(context?.getDrawable(drawableRes))
    }

    private fun updateLengthButtonImage() {
        val drawableRes = when(hair.length) {
            Hair.Length.SHORT -> R.drawable.shor_hair
            Hair.Length.MIDDLE -> R.drawable.middle_hair
            else -> R.drawable.long_hair
        }
        hair_length_button.setImageDrawable(context?.getDrawable(drawableRes))
    }

    private fun updateClimateButtonImage() {
        val drawableRes = when(hair.climate) {
            Hair.Climate.FRIGID -> R.drawable.frigid_climate
            Hair.Climate.SIMPLE -> R.drawable.simple_climate
            else -> R.drawable.hot_climate
        }
        hair_climate_button.setImageDrawable(context?.getDrawable(drawableRes))
    }

    private fun updateTypeTextView() {
        val stringRes = when(hair.type) {
            Hair.Type.DRY -> R.string.dry_hair
            Hair.Type.REGULAR -> R.string.regular_hair
            else -> R.string.oily_hair
        }
        hair_type_text_view.text = getString(stringRes)
    }

    private fun updateTimeRangeTextView() {
        val stringRes = when(timeRange) {
            TimeRange.ONE_WEEK -> R.string.one_week
            TimeRange.TWO_WEEKS -> R.string.two_weeks
            else -> R.string.month
        }
        time_range_text_view.text = getString(stringRes)
    }

    private fun updateLengthTextView() {
        val stringRes = when(hair.length) {
            Hair.Length.SHORT -> R.string.short_hair
            Hair.Length.MIDDLE -> R.string.middle_hair
            else -> R.string.long_hair
        }
        hair_length_text_view.text = getString(stringRes)
    }

    private fun updateClimateTextView() {
        val stringRes = when(hair.climate) {
            Hair.Climate.FRIGID -> R.string.frigid_climate
            Hair.Climate.SIMPLE -> R.string.simple_climate
            else -> R.string.hot_climate
        }
        hair_climate_text_view.text = getString(stringRes)
    }

    private fun onClickTypeButton() {
        hair.switchTypeToNext()
        updateTypeButtonImage()
        updateTypeTextView()
        callUpdatesFromListener()
    }

    private fun onClickTimeRangeButton() {
        timeRange = timeRange.getNext()
        updateTimeRangeButtonImage()
        updateTimeRangeTextView()
        callUpdatesFromListener()
    }

    private fun onClickLengthButton() {
        hair.switchLengthToNext()
        updateLengthButtonImage()
        updateLengthTextView()
        callUpdatesFromListener()
    }

    private fun onClickClimateButton() {
        hair.switchClimateToNext()
        updateClimateButtonImage()
        updateClimateTextView()
        callUpdatesFromListener()
    }

    private fun callUpdatesFromListener() {
        listener?.updateConfig()
        listener?.updateWeeklyAdapter()
    }

    fun isLastWashingToday() = hair.lastWashing == LocalDate.now()

    fun setLastWashingAsToday() {
        hair.preLastWashing = hair.lastWashing
        hair.lastWashing = LocalDate.now()
    }

    fun setLastWashingAsPrevious() {
        hair.lastWashing = hair.preLastWashing
        hair.preLastWashing = Hair.NEVER_WASHING_DATE
    }

    fun hideFragment() {
        fragmentManager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            ?.hide(this)
            ?.commit()
    }

    fun showFragment() {
        fragmentManager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.show(this)
            ?.commit()
    }
}