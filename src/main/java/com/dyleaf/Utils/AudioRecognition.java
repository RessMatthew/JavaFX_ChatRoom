package com.dyleaf.Utils;

import com.baidu.aip.speech.AipSpeech;
import com.dyleaf.Test.AudioRecorder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @Description
 * @Date 2022/9/2 14:59
 * @Author RessMatthew
 * @Version 1.0
 **/

public class AudioRecognition {
    //设置APPID/AK/SK
    public static final String APP_ID = "26694746";
    public static final String API_KEY = "agxGPLACcXAPk9OoBmhrBHVI";
    public static final String SECRET_KEY = "paDXxXbGRlL7BUcT6ughBq1bppiLaP1G";

    public static String recognise(){
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);// 初始化一个AipSpeech
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        JSONObject res = client.asr("test.wav", "wav", 16000, null);
        System.out.println("..................语音识别response...................");
        System.out.println(res.toString(2));
        System.out.println("..................识别信息...................");
        JSONArray result = res.getJSONArray("result");
        String content = (String) result.get(0);
        System.out.println(result.get(0));
        return content;
    }

    public static void main(String[] args) {
        //测试播放
        com.dyleaf.Test.AudioRecorder audioRecorder = new AudioRecorder("test.wav");
        audioRecorder.play("temp.wav");

        //测试识别
        //recognise();

        /*
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        // System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.asr("test.wav", "wav", 16000, null);
        System.out.println(res.toString(2));
        */
    }
}
