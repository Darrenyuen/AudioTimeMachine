package com.jarvis.audiotimemachine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var toRecordPage: Button? = null
    var toTrackPage: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toRecordPage = findViewById(R.id.toRecordPage)
        toTrackPage = findViewById(R.id.toTrackPage)
        toRecordPage?.setOnClickListener(this)
        toTrackPage?.setOnClickListener(this)

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
