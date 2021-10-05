package com.ssafy.herehear.feature.calender.main

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainCalenderBinding
import com.ssafy.herehear.feature.calender.CalenderFragment
import com.ssafy.herehear.model.network.RetrofitClientAI
import java.util.*
import kotlin.collections.ArrayList

class MainCalenderFragment : Fragment() {

    lateinit var binding: FragmentMainCalenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCalenderBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var events: ArrayList<EventDay> = ArrayList()

        val calendar1 = Calendar.getInstance()

        calendar1.set(2021, 10, 5)
        events.add(EventDay(calendar1, R.drawable.audiobook))
        binding.calendarView.setEvents(events)
        binding.calendarView.setDate(calendar1)

        val calendar2 = Calendar.getInstance()
        calendar2.set(2021, 10, 6)
        events.add(EventDay(calendar2, R.drawable.audiobook))
        binding.calendarView.setEvents(events)
        binding.calendarView.setDate(calendar2)

        val calendar3 = Calendar.getInstance()
        calendar3.set(2021, 10, 7)
        events.add(EventDay(calendar3, R.drawable.audiobook))
        binding.calendarView.setEvents(events)
        binding.calendarView.setDate(calendar3)





        binding.calendarView.setOnDayClickListener(OnDayClickListener() {
            Log.d("test", "클릭됨${it.calendar}")
            Log.d("test", "클릭됨${it.calendar.time}")
            Log.d("test", "클릭됨${it.calendar.timeZone}")

            var calendar = it.calendar
            events.add(EventDay(calendar, R.drawable.audiobook))
            binding.calendarView.setEvents(events)
        })

//        val builder: DatePickerBuilder = DatePickerBuilder(activity, listener)
//            .setPickerType(CalendarView.ONE_DAY_PICKER)
//
//        val datePicker = builder.build()
//        datePicker.show()

    }

}