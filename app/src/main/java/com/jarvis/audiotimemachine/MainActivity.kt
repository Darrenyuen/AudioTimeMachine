package com.jarvis.audiotimemachine

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Process
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = this.javaClass.simpleName

    var toRecordPage: Button? = null
    var toTrackPage: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toRecordPage = findViewById(R.id.toRecordPage)
        toTrackPage = findViewById(R.id.toTrackPage)
        toRecordPage?.setOnClickListener(this)
        toTrackPage?.setOnClickListener(this)
        if (Build.VERSION.SDK_INT > 23) {
            Log.d(TAG, "申请录音权限")
            val audioPer = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
            val writePer = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val readPer = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            val createPer = ContextCompat.checkSelfPermission(this, android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
            if (audioPer != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), 666)
            }
            if (writePer != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 6666)
            }
            if (readPer != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 777)
            }
            if (createPer != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS), 7)
            }
            Log.d(TAG, "PackageManager.PERMISSION_GRANTED audio $audioPer writePer $writePer readPer $readPer")
        }
    }

    override fun onClick(v: View?) {
        //通过setAction启动Activity
        var intent: Intent? = null
        when(v?.id) {
            R.id.toRecordPage -> intent = Intent(this, RecordPage::class.java)
            R.id.toTrackPage -> intent = Intent(this, TrackPage::class.java)
        }
        startActivity(intent)
    }
}
