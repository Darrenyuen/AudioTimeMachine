package com.jarvis.audiotimemachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class RecordPage : AppCompatActivity(), View.OnClickListener {

    var finish: TextView? = null
    var cancel: TextView? = null
    var record: ImageView? = null
    var pause: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_page)
        finish = findViewById(R.id.finish)
        cancel = findViewById(R.id.cancel)
        record = findViewById(R.id.record)
        pause = findViewById(R.id.pause)
        finish?.setOnClickListener(this)
        cancel?.setOnClickListener(this)
        record?.setOnClickListener(this)
        pause?.setOnClickListener(this)

        AudioRecorder.getInstance().createDefaultRecorder()

        AudioRecorder.getInstance().setRecordListener(object : AudioRecorder.RecordListener {
            override fun onStartRecord() {
                TODO("Not yet implemented")
            }

            override fun onRecordData(bytes: ByteArray?) {
                TODO("记录录音数据")
            }

            override fun onStopRecord() {
                TODO("保存录音数据")
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.record -> {
                Toast.makeText(this@RecordPage, "开始录制", Toast.LENGTH_SHORT).show()
                record?.visibility = View.GONE
                pause?.visibility = View.VISIBLE
                AudioRecorder.getInstance().startRecord()
            }
            R.id.pause -> {
                Toast.makeText(this@RecordPage, "暂停录制", Toast.LENGTH_SHORT).show()
                pause?.visibility = View.GONE
                record?.visibility = View.VISIBLE
                AudioRecorder.getInstance().stopRecord()
            }
        }
    }
}
