package com.dyleaf.Utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description TODO 用于编码解码.wav文件
 * @Date 2022/9/2 12:29
 * @Author RessMatthew
 * @Version 1.0
 **/

public class AudioBase64Util {
    /**
     * TODO .wav转为base64编码字符串
     */
    public static String wavToStr(String filepath) throws IOException {
        File audioFile = new File(filepath);
        byte[] bytes = FileUtils.readFileToByteArray(audioFile);
        String encoded = Base64.encodeBase64String(bytes);
        return encoded;
    }

    /**
     * TODO base64编码字符串转为.wav
     */
    public static void strToWar(String encoded){
        byte[] decoded = Base64.decodeBase64(encoded);
        try
        {
            File file2 = new File("test.wav");
            FileOutputStream os = new FileOutputStream(file2, true);
            os.write(decoded);
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
