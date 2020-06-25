package com.jarvis.audiotimemachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import java.io.File

class TrackPage : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    var listView: ListView? = null
    var list = ArrayList<String>()
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_page)
        file = File(this.filesDir.toString())
        listView = findViewById(R.id.listView)
        file?.listFiles()?.forEach {
            list.add(it.absolutePath)
            Log.d(TAG, it.absolutePath)
        }
        listView?.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
    }
}
