package com.dyleaf.Utils;

import com.dyleaf.Dao.DbUtils;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvatarUtil {

    public HashMap<String ,String>usernameAndImage;

    public static String getUserAvatarByUsername(String username){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String sql="select image from ChatUser where name=?";
        String profile=null;//用户头像
        try{
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(   1,username);
            result = ps.executeQuery();
            if (result.next())
                profile=result.getString("image");
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbUtils.close(result, ps, conn);
        }
        return profile;
    };

    public static String imageUrlToBase64(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;

        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();

            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();

            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用输入流从buffer里把数据读取出来
            while( (len = is.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }

            // 对字节数组Base64编码
            return encode(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
                if(outStream != null) {
                    outStream.close();
                }
                if(httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 图片转字符串
     * @param image 图片Buffer
     * @return Base64编码
     */
    public static String encode(byte[] image){
        BASE64Encoder decoder = new BASE64Encoder();
        return replaceEnter(decoder.encode(image));
    }

    /**
     * 字符替换
     * @param str 字符串
     * @return 替换后的字符串
     */
    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }


}
