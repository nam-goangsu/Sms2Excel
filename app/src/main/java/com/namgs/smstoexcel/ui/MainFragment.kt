package com.namgs.smstoexcel.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.namgs.Utill
import com.namgs.smstoexcel.Adapter.Mainadapter
import com.namgs.smstoexcel.MainActivity
import com.namgs.smstoexcel.MainActivity.Companion.arrPermission
import com.namgs.smstoexcel.R
import com.namgs.smstoexcel.data.loadsms
import com.namgs.smstoexcel.databinding.FragmentMainBinding
import com.namgs.smstoexcel.viewmodel.ShardViewModel
import com.namgs.smstoexcel.vo.SMS
import com.namgs.smstoexcel.vo.SmsDataList_1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class MainFragment : Fragment() {

    companion object {
    //    fun newInstance() = MainFragment()
    }

    private var binding: FragmentMainBinding? = null
    private val shviewModel: ShardViewModel by viewModels()
    private lateinit var activity : Context
    private lateinit var adapter :Mainadapter

    private val selectedSMS = mutableListOf<SmsDataList_1>()
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

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = shviewModel
            mainFragment = this@MainFragment
        }

        return binding!!.root


    }

    override fun onResume() {
        super.onResume()
        if(checkPermissions()) {
            shviewModel.loadSmsMessages(
                loadsms(activity),
                startDate,
                calendar.getTimeInMillis(),
                messagetype
            )  //기본적인 리스트
        }

    }

    private fun checkPermissions(): Boolean {
        return arrPermission.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        startDate =Utill().convertToMillis(year,month+1,1)
        endDate =Utill().convertToMillis(year,month+1,day)

        if(checkPermissions()) {
            shviewModel.loadSmsMessages(
                loadsms(activity),
                startDate,
                calendar.getTimeInMillis(),
                messagetype
            )  //기본적인 리스트
        }



        shviewModel.defaltdata.observe(viewLifecycleOwner, Observer { messages  -> // 리스트
            binding!!.recyclerViewSMS.layoutManager = LinearLayoutManager(activity)


            adapter  = Mainadapter(messages,selectedSMS )
            binding!!.recyclerViewSMS.adapter = adapter
            adapter .notifyDataSetChanged()
            Log.d("test","size ${messages .size}")
        })


        shviewModel.stDate.observe(viewLifecycleOwner, Observer { date ->   // 시작일
          //  binding!!.stdayTextview.text = date
            startDate = dateFormat.parse(date)?.time ?: 0L
        })
        shviewModel.selectedDate.observe(viewLifecycleOwner, Observer { date -> /// 종료일
            binding!!.eddayTextview.text = date
            endDate = dateFormat.parse(date)?.time ?: 0L
        })
        shviewModel.stMessageint.observe(viewLifecycleOwner,Observer{date ->  // 문자 발신 여부 타입 0전체 1수신 2 발신
            messagetype = date

        })


     //   binding!!.stview.setOnClickListener(this)
        //binding!!.edview.setOnClickListener(this)
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

    fun stviewclick(){
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

    fun edviewclick(){

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



    fun saveSelectedSmsToExcel() {


        if(selectedSMS.size>0){
            Toast.makeText(activity, "저장을 시작 합니다. 잠시만 기달리세요.", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
        //    Utill().setSnackbar(requireView(),"저장을 시작 합니다. 잠시만 기달리세요.","확인")
            val workbook = XSSFWorkbook()
            val timestamp = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault()).format(Date())
            val fileName = "sms_$timestamp.xlsx"
            val sheet = workbook.createSheet("$timestamp SMS")



        val header = sheet.createRow(0)
        header.createCell(1).setCellValue("날짜")
        header.createCell(0).setCellValue("번호")
        header.createCell(2).setCellValue("내용")


        selectedSMS.forEachIndexed { index, sms ->
            val row = sheet.createRow(index + 1)
            row.createCell(1).setCellValue(Utill().mill2Format(sms.date))
            row.createCell(0).setCellValue(sms.address)
            row.createCell(2).setCellValue(sms.body)
        }


        val smsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path


        val filePath = File(smsDir, fileName)
        FileOutputStream(filePath).use { fileOut ->
            workbook.write(fileOut)
        }
        workbook.close()
        Utill().setSnackbar(requireView(),"엑셀 저장 완료 저장 위치는 Documents, 파일명은 ${fileName}","확인")
     //   Toast.makeText(activity, "엑셀 저장 완료 저장 위치는 Documents, 파일명은 ${fileName}", Toast.LENGTH_SHORT).show()
            }

        }else{
            Utill().setSnackbar(requireView(),"선택된 메시지 수가 ${selectedSMS.size}개 입니다.","확인")
        }

    }

}




