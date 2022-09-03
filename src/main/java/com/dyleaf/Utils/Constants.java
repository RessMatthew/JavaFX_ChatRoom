package com.dyleaf.Utils;


public class Constants {

    //----------------文件类型常量----------------

    public final static String FILETYPE = "file";
    public final static String VOICETYPE = "voice";
    public final static String PHOTOTYPE = "photo";
    public final static String ORDINARYMESSAGE = "odinaryMessage";

    //-------------私聊还是公屏发消息----------------
    public final static boolean SINGLE = true;
    public final static boolean GROUP = false;


    //--------------socket传输map键-----------------
    public final static int SUCCESS = 0x01;
    public final static int FAILED = 0x02;

    public static Integer  COMMAND = 0x10;//16
    public static Integer  TIME = 0x11;//17
    public static Integer  USERNAME = 0x12;//18
    public static Integer  PASSWORD = 0x13;//19
    public static Integer  SPEAKER = 0x14;//20
    public static Integer  RECEIVER = 0x15;//21
    public static Integer  CONTENT= 0x16;//22
    public final static int COM_LOGIN = 0x20;//32
    public final static int COM_SIGNUP = 0x21;//33
    public final static int COM_RESULT = 0x22;//34
    public final static int COM_DESCRIPTION = 0x23;//35
    public final static int COM_LOGOUT =0x24;//36
    public final static int COM_CHATWITH = 0x25;//37
    public final static int COM_GROUP = 0x26;//38
    public final static int COM_CHATALL = 0x27;//39
    public final static int COM_KEEP = 0x28;//40
    public final static int COM_MESSAGEALL = 0X29;//41

    public final static int MESSAGETYPE = 0x30;//48

    public final static int FILECONTENT = 0x31;//49
    public final static int VOICECONTENT = 0x32;//50




}
