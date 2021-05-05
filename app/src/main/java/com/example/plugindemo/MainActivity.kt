package com.example.plugindemo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plugindemo.process.PluginProcessService
import com.example.plugindemo.process.PpsController

class MainActivity : AppCompatActivity() {
    protected var ppsController: PpsController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.Q) {
            Toast.makeText(this, "hook是按照Api29的源码来的，你的版本可能不支持", Toast.LENGTH_SHORT).show()
        }
        initPlugin()
        findViewById<View>(R.id.start_plugin).setOnClickListener {
            if(ppsController != null){
                ppsController?.startPlugin()
            }else {
                Toast.makeText(this, "等待插件初始化", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initPlugin() {
        bindService(
            Intent(this, PluginProcessService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    ppsController = PluginProcessService.wrapBinder(service)
                    try {
                        ppsController?.initPlugin()
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {}
            },
            BIND_AUTO_CREATE
        )
    }

}