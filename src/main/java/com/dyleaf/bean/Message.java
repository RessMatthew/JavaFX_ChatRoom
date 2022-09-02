package com.dyleaf.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *@Author com.dyleaf [com.dyleaf.github.io]
 *@ceeate 2017/11/30
 * @description
 */
public class Message implements Serializable{

    private String content = null;
    private String speaker = null;
    private String timer = null;
    private ArrayList<String>imageList = null;

    //---------------文件传输部分-----------------------

    private String type = null;

    private String fileContent = null;

    //---------------音频传输部分-----------------------
    private String voiceContent = null;

    //------------------------------------------------

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    //---------------文件传输部分-----------------------

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    //------------------------------------------------

    //---------------音频传输部分-----------------------

    public String getVoiceContent() {
        return voiceContent;
    }

    public void setVoiceContent(String voiceContent) {
        this.voiceContent = voiceContent;
    }
    //------------------------------------------------

}
