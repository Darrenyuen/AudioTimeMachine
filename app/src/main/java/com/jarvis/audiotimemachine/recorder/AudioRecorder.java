package com.jarvis.audiotimemachine.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * yuan
 * 2020/7/12
 **/
public class AudioRecorder implements Recorder {

    private static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static int SAMPLE_RATE_IN_HZ = 44100;
    private static int CHANNEL_CONFIGURATION = AudioFormat.CHANNEL_IN_STEREO;
    private static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private int mBufferSizeInBytes = 0;
    private AudioRecord mAudioRecord;

    private Thread recordThread;
    private OutputStream outputStream;

    private String filePath;

    @Override
    public void initConfig() {
        if (mAudioRecord != null) mAudioRecord.release();
        try {
            mBufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIGURATION, AUDIO_FORMAT);
            mAudioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIGURATION, AUDIO_FORMAT, mBufferSizeInBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(String filePath) {
        mAudioRecord.startRecording();
        this.filePath = filePath;
        recordThread = new Thread(new RecordRunnable(), "RecordThread");
        recordThread.start();
    }

    @Override
    public void stop() {
        mAudioRecord.startRecording();
        release();
    }

    private void release() {
        mAudioRecord.release();
        mAudioRecord = null;
    }


    class RecordRunnable implements Runnable {
        @Override
        public void run() {
            try {
                //将录得的数据以字节流的形式写入文件中
                outputStream = new FileOutputStream(filePath);
                byte[] audioDataArray = new byte[mBufferSizeInBytes];
                while (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    int end = mAudioRecord.read(audioDataArray, 0, audioDataArray.length);
                    //有几种读取录音数据的方法
                    outputStream.write(audioDataArray, 0, end);
                    outputStream.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
