package com.namgs.smstoexcel.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.namgs.Utill
import com.namgs.smstoexcel.Adapter.Mainadapter
import com.namgs.smstoexcel.R
import com.namgs.smstoexcel.data.loadsms
import com.namgs.smstoexcel.databinding.FragmentMainBinding
import com.namgs.smstoexcel.viewmodel.ShardViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainFragment : Fragment(), View.OnClickListener {

    companion object {
    //    fun newInstance() = MainFragment()
    }

    private var binding: FragmentMainBinding? = null
    private val shviewModel: ShardViewModel by viewModels()
    private lateinit var activity : Context
    private lateinit var adapter :Mainadapter



    val calendar : Calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)


    var startDate = 0L
    var endDate =0L

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var messagetype =0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        activity = context as Activity
        return binding!!.root


    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = shviewModel
            mainFragment = this@MainFragment
        }


        startDate =Utill().convertToMillis(year,month+1,1)
        endDate =Utill().convertToMillis(year,month+1,day)


        shviewModel.loadSmsMessages(loadsms(activity),startDate, calendar.getTimeInMillis(),messagetype)  //기본적인 리스트




        shviewModel.defaltdata.observe(viewLifecycleOwner, Observer { messages  -> // 리스트
            binding!!.recyclerViewSMS.layoutManager = LinearLayoutManager(activity)


            adapter  = Mainadapter(messages )
            binding!!.recyclerViewSMS.adapter = adapter
            adapter .notifyDataSetChanged()
            Log.d("test","size ${messages .size}")
        })


        shviewModel.stDate.observe(viewLifecycleOwner, Observer { date ->   // 시작일
            binding!!.stdayTextview.text = date
            startDate = dateFormat.parse(date)?.time ?: 0L
        })
        shviewModel.selectedDate.observe(viewLifecycleOwner, Observer { date -> /// 종료일
            binding!!.eddayTextview.text = date
            endDate = dateFormat.parse(date)?.time ?: 0L
        })
        shviewModel.stMessageint.observe(viewLifecycleOwner,Observer{date ->  // 문자 발신 여부 타입 0전체 1수신 2 발신
            messagetype = date

        })


        binding!!.stview.setOnClickListener(this)
        binding!!.edview.setOnClickListener(this)
        binding!!.messageGroup.setOnCheckedChangeListener{group,checkedid->
            when(checkedid){
                R.id.SHOW_ALL->{shviewModel.setMessageType(0)
                    }
                R.id.input_message->{shviewModel.setMessageType(1)
                    }
                R.id.output_message->{shviewModel.setMessageType(2)
                    }
            }
            shviewModel.loadSmsMessages(loadsms(activity),startDate,endDate,messagetype)
            adapter .notifyDataSetChanged()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.stview -> {
                val test =Utill().getMillisto_Calendar(startDate)
                DatePickerDialog(
                    requireContext(),
                    { _, selectedYear, selectedMonth, selectedDay ->

                        shviewModel.setDate(selectedYear, selectedMonth, selectedDay)
                        shviewModel.loadSmsMessages(loadsms(activity),startDate,endDate,messagetype)
                        adapter .notifyDataSetChanged()
                    },

                    test.first, test.second-1,test.third
                ).apply {
                    datePicker.maxDate = endDate
                }.show()
            }
            R.id.edview -> {
                val test2 =Utill().getMillisto_Calendar(endDate)
                DatePickerDialog(
                    requireContext(),
                    { _, selectedYear, selectedMonth, selectedDay ->
                        shviewModel.setDate1(selectedYear, selectedMonth, selectedDay)
                        shviewModel.loadSmsMessages(loadsms(activity),startDate,endDate,messagetype)
                        adapter .notifyDataSetChanged()
                    },
                    test2.first, test2.second-1, test2.third
                ).apply {
                    datePicker.minDate = startDate
                datePicker.maxDate = System.currentTimeMillis()
                }.show()
            }
        }
    }
}



