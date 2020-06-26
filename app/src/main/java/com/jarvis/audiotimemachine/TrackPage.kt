package com.jarvis.audiotimemachine

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import java.io.*

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
            list.add(it.length().toString())
            Log.d(TAG, it.absolutePath)
        }
        AudioTrackManager.getInstance().initConfig();
        listView?.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listView?.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val file = File(list[position])
                AudioTrackManager.getInstance().play(file.absolutePath)
//                val len = (file.length() / 2).toInt()
//                val music = ShortArray(len)
//                Log.d(TAG, list[position])
//                try {
//                    val inputStream = FileInputStream(file)
//                    val bufferedInputStream = BufferedInputStream(inputStream)
//                    val dataInputStream = DataInputStream(bufferedInputStream)
//                    var i = 0
//                    while (dataInputStream.available() > 0) {
//                        music[i] = dataInputStream.readShort()
//                        i++
//                    }
//                    dataInputStream.close()
//                    val audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
//                    AudioFormat.ENCODING_PCM_16BIT, len*2, AudioTrack.MODE_STREAM)
//                    audioTrack.play()
//                    audioTrack.write(music, 0, len)
//                    audioTrack.stop()
//                } catch (t: Throwable) {
//
//                }
            }
        })
    }
}
