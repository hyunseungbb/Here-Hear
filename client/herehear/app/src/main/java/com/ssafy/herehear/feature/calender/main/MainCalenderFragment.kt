package com.ssafy.herehear.feature.calender.main

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainCalenderBinding
import com.ssafy.herehear.feature.calender.CalenderFragment
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.RetrofitClientAI
import com.ssafy.herehear.model.network.response.AllCommentsResponse
import com.ssafy.herehear.model.network.response.AllCommentsResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainCalenderFragment : Fragment() {
    lateinit var events: ArrayList<EventDay>
    lateinit var binding: FragmentMainCalenderBinding
    lateinit var commentList: AllCommentsResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCalenderBinding.inflate(inflater, container, false)

        events= ArrayList()
        RetrofitClient.api.getMyComments().enqueue(object: Callback<AllCommentsResponse> {
            override fun onResponse(
                call: Call<AllCommentsResponse>,
                response: Response<AllCommentsResponse>
            ) {
                if (response.isSuccessful) {
                    commentList = response.body()!!
                    for (item in commentList) {
                        val dateArray = item.date.split("T")[0].split("-")
                        val calendar = Calendar.getInstance()
                        Log.d("test", "${dateArray[0]}, ${dateArray[1]}, ${dateArray[2]}")
                        calendar.set(dateArray[0].toInt(), dateArray[1].toInt()-1, dateArray[2].toInt())

                        events.add(EventDay(calendar, R.drawable.audiobook))
                        binding.calendarView.setEvents(events)
                        binding.calendarView.setDate(calendar)
                    }
                } else {
                    Toast.makeText(activity, "정보를 불러오지 못했습니다.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AllCommentsResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(activity, "정보를 불러오지 못했습니다.", Toast.LENGTH_LONG).show()
            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.calendarView.setOnDayClickListener(OnDayClickListener() {
            Log.d("test", "클릭됨${it.calendar.time}")
            Log.d("test", "클릭됨${it.calendar.time.javaClass}")

            var date = it.calendar.time.dateToString("yyyy-MM-dd")
            val data: MutableList<AllCommentsResponseItem> = mutableListOf()
            for (item in commentList) {
                if (item.date.split("T")[0] == date) {
                    data.add(item)
                }
            }
//            val adapter =

        })

//        val builder: DatePickerBuilder = DatePickerBuilder(activity, listener)
//            .setPickerType(CalendarView.ONE_DAY_PICKER)
//
//        val datePicker = builder.build()
//        datePicker.show()

    }

    private fun Date.dateToString(format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }

}