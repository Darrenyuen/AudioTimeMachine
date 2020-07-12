package com.jarvis.audiotimemachine.recorder;

/**
 * yuan
 * 2020/7/12
 **/
public interface Recorder {
    void initConfig();
    void start(String filePath);
    void stop();
}
