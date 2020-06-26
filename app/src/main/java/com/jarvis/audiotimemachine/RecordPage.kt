package com.jarvis.audiotimemachine

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.*

class RecordPage : AppCompatActivity(), View.OnClickListener {

    private val TAG = this.javaClass.simpleName

    var finish: TextView? = null
    var cancel: TextView? = null
    var record: ImageView? = null
    var pause: ImageView? = null

    var file: File? = null

    var outputStream: OutputStream? = null
    var bufferedOutputStream: BufferedOutputStream? = null
    var dataOutputStream: DataOutputStream? = null

//    private var recordFileDir = Settings.Global.getFile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_page)

        file = File(this.filesDir, "recorddata.pcm")

        if (file!!.exists()) {
            Log.d(TAG, "文件已存在，先删除")
            file?.delete()
        }

        finish = findViewById(R.id.finish)
        cancel = findViewById(R.id.cancel)
        record = findViewById(R.id.record)
        pause = findViewById(R.id.pause)
        finish?.setOnClickListener(this)
        cancel?.setOnClickListener(this)
        record?.setOnClickListener(this)
        pause?.setOnClickListener(this)

//        AudioRecorder.getInstance().createDefaultRecorder()

//        AudioRecorder.getInstance().setRecordListener(object : AudioRecorder.RecordListener {
//            override fun onStartRecord() {
//
//            }
//
//            override fun onRecordData(bytes: ByteArray?) {
//                Log.d(TAG, file!!.absolutePath)
//                try {
//                    if (!file!!.exists()) file?.createNewFile()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                try {
//                    outputStream = FileOutputStream(file)
//                    bufferedOutputStream = BufferedOutputStream(outputStream!!)
//                    dataOutputStream = DataOutputStream(bufferedOutputStream)
//                    dataOutputStream?.write(bytes!!)
//                } catch (t: Throwable) {
//
//                }
//            }
//
//            override fun onStopRecord() {
//                dataOutputStream?.close()
//            }
//        })

        AudioRecordManager.getInstance().initConfig()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.record -> {
                Toast.makeText(this@RecordPage, "开始录制", Toast.LENGTH_SHORT).show()
                record?.visibility = View.GONE
                pause?.visibility = View.VISIBLE
//                AudioRecorder.getInstance().startRecord()
                AudioRecordManager.getInstance().startRecord(file?.absolutePath)
            }
            R.id.pause -> {
                Toast.makeText(this@RecordPage, "暂停录制", Toast.LENGTH_SHORT).show()
                pause?.visibility = View.GONE
                record?.visibility = View.VISIBLE
//                AudioRecorder.getInstance().stopRecord()
                AudioRecordManager.getInstance().stopRecord()
            }
        }
    }
}
