package com.algorigo.algorigoutils

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.algorigo.algorigoutils.databinding.ActivityDatetimeutilBinding
import com.algorigo.library.DateTimeUtil
import org.joda.time.DateTime
import org.joda.time.Period
import java.util.*

class DateTimeUtilActivity : AppCompatActivity() {

    private val spinnerAdapter = LocaleAdapter()
    private lateinit var binding: ActivityDatetimeutilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatetimeutilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.localeSpinner.adapter = spinnerAdapter
        binding.localeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                uiSetText(Locale.getDefault())
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val locale = spinnerAdapter.getItem(position) as Locale
                uiSetText(locale)
            }
        }
        uiSetText(Locale.getDefault())
    }

    private fun uiSetText(locale: Locale) {
        val now = DateTime.now()
        binding.textView1.setText(DateTimeUtil.getMediumDateTimeString(now, locale))
        binding.textView2.setText(DateTimeUtil.getShortDateTimeString(now, locale))
        binding.textView3.setText(DateTimeUtil.getMediumDateString(now, locale))
        binding.textView4.setText(DateTimeUtil.getShortDateString(now, locale))
        binding.textView5.setText(DateTimeUtil.getMediumTimeString(now, locale))
        binding.textView6.setText(DateTimeUtil.getShortTimeString(now, locale))

        val lastDayOfMonth = DateTimeUtil.getLastDayOfMonth(now)
        binding.textView7.setText(DateTimeUtil.getMediumDateString(lastDayOfMonth))
        val firstDayOfNextMonth = lastDayOfMonth.plusDays(1)
        binding.textView8.setText(DateTimeUtil.getMediumDateString(firstDayOfNextMonth))

        val pattern = "MM/dd(E)"
        val firstDayOfWeek = DateTimeUtil.getFirstDayOfWeek(now)
        binding.textView9.setText(DateTimeUtil.getDateTimeStringWithPattern(firstDayOfWeek.toDateTimeAtStartOfDay(), pattern))
        val lastDayOfPrevWeek = firstDayOfWeek.minusDays(1)
        binding.textView10.setText(DateTimeUtil.getDateTimeStringWithPattern(lastDayOfPrevWeek.toDateTimeAtStartOfDay(), pattern))
        val lastDayOfWeek = DateTimeUtil.getLastDayOfWeek(now)
        binding.textView11.setText(DateTimeUtil.getDateTimeStringWithPattern(lastDayOfWeek.toDateTimeAtStartOfDay(), pattern))
        val firstDayOfNextWeek = lastDayOfWeek.plusDays(1)
        binding.textView12.setText(DateTimeUtil.getDateTimeStringWithPattern(firstDayOfNextWeek.toDateTimeAtStartOfDay(), pattern))

        val strPeriod0 = DateTimeUtil.getHourMinutePeriodString(Period(0, 2, 30, 200))
        binding.textView13.setText(strPeriod0)
        val strPeriod1 = DateTimeUtil.getHourMinutePeriodString(Period(0, 12, 30, 200))
        binding.textView14.setText(strPeriod1)
        val strPeriod2 = DateTimeUtil.getHourMinutePeriodString(Period(1, 2, 30, 200))
        binding.textView15.setText(strPeriod2)
        val strPeriod3 = DateTimeUtil.getHourMinutePeriodString(Period(12, 34, 30, 200))
        binding.textView16.setText(strPeriod3)

        val datePattern = "yyyyMMdd"
        val from = "20160521"
        val to = "20170322"
        val fromDateTime = DateTimeUtil.getDateTimeWithPattern(from, datePattern)
        val toDateTime = DateTimeUtil.getDateTimeWithPattern(to, datePattern)
        val daysBetween = DateTimeUtil.getDaysBetweenDateTimes(fromDateTime, toDateTime)
        binding.textView17.setText("$from - $to = $daysBetween")
    }

}
