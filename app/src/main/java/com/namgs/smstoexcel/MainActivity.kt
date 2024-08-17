package com.namgs.smstoexcel


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.snackbar.Snackbar
import com.namgs.smstoexcel.viewmodel.ShardViewModel

import android.provider.Settings
import androidx.navigation.NavController
import com.namgs.Utill

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {

        val arrPermission = arrayOf(
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.SEND_SMS
        )
    }


    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var sharedViewModel: ShardViewModel
    private lateinit var view :View
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController :NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermissions()) {
            startFragment()
        } else {
            requestPermissionsLauncher.launch(arrPermission)
            //startFragment()
        }

        view = window.decorView.findViewById<View>(android.R.id.content)

        //enableEdgeToEdge()
     //   setupActionBarWithNavController(navController, appBarConfiguration)



    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)//이부분 추가로 뒤로가기 설정
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    private fun checkPermissions(): Boolean {
        return arrPermission.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.entries.all {
                it.value
            }

            if (allPermissionsGranted) {
                startFragment()
            } else {
                showPermissionDeniedSnackbar()
            }
        }

    private fun startFragment() {
         navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController // 네비게이션 컨트롤러
        appBarConfiguration = AppBarConfiguration(navController.graph)// 뒤로가기 버튼 선택시 이전 프래그먼트 이동
    }

    fun showPermissionDeniedSnackbar() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "앱을 실행 하려면, 애플리케이션정보->권한->SMS권한을 허용하여 주십시요.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("설정으로 가기") {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }.show()
    }
}




