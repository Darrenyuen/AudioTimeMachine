package com.jarvis.audiotimemachine;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * yuan
 * 2020/6/26
 **/
public class AudioTrackManager {
    private final String TAG = this.getClass().getSimpleName();

    private  static AudioTrackManager instance;

    private AudioTrack audioTrack;

    /*音频缓存区大小*/
    private int mBufferSizeInBytes = 0;
    /*是否正在播放*/
    private boolean mIsPlaying = false;
    private Thread mPlayingThread;
    /*播放文件的路径*/
    private String mFilePath;
    /*读取文件IO流*/
    private DataInputStream mDataInputStream;

    private AudioTrackManager() {
        super();
    }

    public static AudioTrackManager getInstance() {
        if (instance == null) {
            synchronized (AudioTrackManager.class) {
                if (instance == null) {
                    instance = new AudioTrackManager();
                    return instance;
                }
            }
        }
        return instance;
    }

    public void initConfig() {
        if (audioTrack != null) audioTrack.release();
        mBufferSizeInBytes = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, mBufferSizeInBytes, AudioTrack.MODE_STREAM);
    }

    /**
     * 开始播放录音
     */
    public synchronized void play(String filePath){
        Log.e(TAG,"播放状态："+audioTrack.getState());
        if (mIsPlaying) return;
        if (null != audioTrack && audioTrack.getState() == AudioTrack.STATE_INITIALIZED){
            Log.d(TAG, "开始播放");
            audioTrack.play();
        }
        this.mFilePath = filePath;
        mIsPlaying = true;
        mPlayingThread = new Thread(new PlayingRunnable(),"PlayingThread");
        mPlayingThread.start();
    }

    /**
     * 停止播放
     */
    private void stop(){
        try {
            if (audioTrack != null){

                mIsPlaying = false;

                //首先停止播放
                audioTrack.stop();

                //关闭线程
                try {
                    if (mPlayingThread != null){
                        mPlayingThread.join();
                        mPlayingThread = null;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //释放资源
                releaseAudioTrack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    private void releaseAudioTrack(){
        if (audioTrack.getState() == AudioRecord.STATE_INITIALIZED) {
            audioTrack.release();
            audioTrack = null;
        }
    }


    class PlayingRunnable implements Runnable{
        @Override
        public void run() {
            try {
                InputStream fileInputStream = new FileInputStream(new File(mFilePath));
                mDataInputStream = new DataInputStream(fileInputStream);
                byte[] audioDataArray = new byte[mBufferSizeInBytes];
                int readLength = 0;
                while (mDataInputStream.available() > 0){
                    Log.d(TAG, "音频播放中");
                    readLength = mDataInputStream.read(audioDataArray);
                    if (readLength > 0) {
                        audioTrack.write(audioDataArray,0, readLength);
                    }
                }
                stop();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
