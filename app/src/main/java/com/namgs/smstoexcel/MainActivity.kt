package com.namgs.smstoexcel


import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.namgs.smstoexcel.viewmodel.ShardViewModel



class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        private const val REQUEST_CODE_PERMISSION = 400
    }
    private val arrPermission = arrayOf(
        android.Manifest.permission.READ_SMS,
        android.Manifest.permission.SEND_SMS
        )

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var sharedViewModel: ShardViewModel
    private lateinit var view :View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init1()
        view = window.decorView.findViewById<View>(android.R.id.content)

        //enableEdgeToEdge()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController // 네비게이션 컨트롤러

        appBarConfiguration = AppBarConfiguration(navController.graph) // 뒤로가기 버튼 선택시 이전 프래그먼트 이동
     //   setupActionBarWithNavController(navController, appBarConfiguration)

    }

    fun init1() {

        if (   !(isAllPermissionGranted() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            requestPermissions(arrPermission, REQUEST_CODE_PERMISSION)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)//이부분 추가로 뒤로가기 설정
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun isAllPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        } else {
            for (permission in arrPermission) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

/*    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readSmsGranted = permissions[android.Manifest.permission.READ_SMS] ?: false
        val writeStorageGranted =
            permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false


        Log.d("main","main to permission to result")
        if (readSmsGranted *//*&& writeStorageGranted*//*) {
            *//*var loadSms  =*//*
            Log.d("main","main to permission to result secussecess")
            sharedViewModel = ViewModelProvider(this).get(ShardViewModel::class)
            sharedViewModel._defaltdata = loadsms().loadSms(this)

        } else {
            //    Toast.makeText(this, "권한을 주지 않으면 실행되지 않습니다.", Toast.LENGTH_SHORT).show()
            //   Snackbar.make(this,"권한을 주지 않으면 실행되지 않습니다.",Snackbar.LENGTH_LONG)



            Utill().setSnackbar(view, "권한을 주지 않으면 실행되지 않습니다.2", MainActivity())

        }
    }*/
}




