package com.dyleaf.Utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;


/**
 * @Description
 * @Date 2022/9/3 16:11
 * @Author RessMatthew
 * @Version 1.0
 **/

public class PhotoBase64Util {
    //将文件转成byte数组，可使用多种方法
    public static byte[] getFileByte(String filePath) throws IOException {
        byte[] bs=null;
        InputStream in=null;
        File file=new File(filePath);
        try {
            in=new FileInputStream(file);
            bs=new byte[(int)file.length()];
            in.read(bs);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        in.close();
        return bs;
    }

    /** byte[] 转 base64编码 **/
    public static String getBase64EncodeString(byte[] bytes){
        BASE64Encoder base=new BASE64Encoder();
        return base.encode(bytes);
    }

    /** base64编码 转 byte[] **/
    public static byte[] getBytes(String base64Encode) throws IOException {
        BASE64Decoder decoder =new BASE64Decoder();
        //解码
        byte[] bytes = decoder.decodeBuffer(base64Encode);
        return bytes;
    }

    //将file转成base64EncodeString
    public static String fileToBase64EncodeString(File file) throws IOException {
        byte[] bs=null;
        InputStream in=null;
        try {
            in=new FileInputStream(file);
            bs=new byte[(int)file.length()];
            in.read(bs);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        in.close();
        String base64EncodeString = getBase64EncodeString(bs);
        return base64EncodeString;
    }

    /**解码base64
     * @throws IOException **/
    public static void createImage(String base64String,String imgPath) throws IOException{
        BASE64Decoder decoder =new BASE64Decoder();
        //解码
        byte[] bytes = decoder.decodeBuffer(base64String);
        //检查调整异常数据
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        OutputStream out=new FileOutputStream(imgPath);
        //生成图片
        out.write(bytes);
        out.flush();
        out.close();
    }
}
