package com.jarvis.audiotimemachine;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * yuan
 * 2020/6/25
 **/
public class AudioRecorder {

    private final String TAG = this.getClass().getSimpleName();

    //音频输入-麦克风
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    //采样频率
    private final static int AUDIO_SAMPLE_RATE = 16000;
    //声道-单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    //编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    //缓冲区大小-字节
    private int bufferSizeInBytes = 0;

    private AudioRecord audioRecord;

    private Status status = Status.STATUS_NO_READY;

    private String fileName;

    private List<String> fileNames = new ArrayList<>();

    private static AudioRecorder instance;

    private RecordListener mRecordListener;

    private RecordThread mRecordThread;

    private AudioRecorder() {
        super();
    }

    public static AudioRecorder getInstance() {
        if (instance == null) {
            instance = new AudioRecorder();
        }
        return instance;
    }

    public void createRecorder(String fileName, int audioSource, int sampleRateInHz, int channelConfig, int audioFormat) {
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        this.fileName = fileName;
        status = Status.STATUS_READY;
    }

    public void createDefaultRecorder() {
        bufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
        Log.d(TAG, "" + bufferSizeInBytes);
        audioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, bufferSizeInBytes);
//        this.fileName = fileName;
        status = Status.STATUS_READY;
    }

    public void startRecord() {
        if (status == Status.STATUS_NO_READY) throw new IllegalStateException("录音器尚未初始化");
        if (status == Status.STATUS_START) throw new IllegalStateException("正在录音");
        audioRecord.startRecording();
        mRecordThread = new RecordThread("RecordThread");
        if (mRecordListener != null) mRecordListener.onStopRecord();
    }

    public void pauseRecord() {
        if (status != Status.STATUS_START) throw new IllegalStateException("没有在录音");
        else {
            audioRecord.stop();
            status = Status.STATUS_PAUSE;
        }
    }

    public void stopRecord() {
        if (status == Status.STATUS_NO_READY || status == Status.STATUS_READY) throw new IllegalStateException("录音尚未开始");
        else {
            audioRecord.stop();
            status = Status.STATUS_STOP;
            try {
                mRecordThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (mRecordListener != null) mRecordListener.onStopRecord();
                audioRecord.release();
            }
        }
        mRecordListener = null;
    }

    public void cancel() {
        fileNames.clear();
        fileName = null;
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
        status = Status.STATUS_NO_READY;
    }

    public Status getStatus() {
        return status;
    }

    public void setRecordListener(RecordListener recordListener) {
        this.mRecordListener = recordListener;
    }

    //录音对象的状态
    public enum Status {
        STATUS_NO_READY,
        STATUS_READY,
        STATUS_START,
        STATUS_PAUSE,
        STATUS_STOP
    }

    class RecordThread extends Thread {
        public RecordThread(@NonNull String name) {
            super(name);
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

            if (getStatus() == Status.STATUS_NO_READY) return;

            byte[] buffer = new byte[bufferSizeInBytes];

            while (getStatus() == Status.STATUS_START) {
                int len = audioRecord.read(buffer, 0, buffer.length);
                if (len != bufferSizeInBytes) return;
                if (mRecordListener != null) {
                    mRecordListener.onRecordData(buffer);
                }
            }
        }
    }

    interface RecordListener {
        void onStartRecord();
        void onRecordData(byte[] bytes);
        void onStopRecord();
    }
}
