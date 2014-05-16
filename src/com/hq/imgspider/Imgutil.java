package com.hq.imgspider;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Imgutil {

	public static int SINGLE_THREAD_COUNT = 5;//单张图片的下载线程数
	
	public static int ALL_THEAD_COUNT = 2;//宗体大线程数
	
	public static String  SAVE_DIR = "D:/downloadimage/";
	
	public static int BUFFER_SIZE = 2048;
	
	public static int SMALL_SLEEP = 100;
	
	
	public static void setSavedir(String save_dir){
		SAVE_DIR = save_dir;
	}
	
	
	public static   String getMD5(String str) {  
        MessageDigest messageDigest = null;  
  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }  
	
}
