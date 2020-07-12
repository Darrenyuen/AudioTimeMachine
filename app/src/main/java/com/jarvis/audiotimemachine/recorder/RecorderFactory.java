package com.jarvis.audiotimemachine.recorder;

/**
 * yuan
 * 2020/7/12
 **/
public class RecorderFactory {
    public Recorder getRecorder(String type) {
        if (type == null || type.isEmpty())
            return null;
        else return new AudioRecorder();
    }
}
