package com.angramainyu.logintest;
import android.util.Base64;

/**
 * 使用Base64来保存和获取密码数据
 */
public class Base64Utils {


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) {
        int decodetime = 5;//压缩和解压的次数，防止被简单破解
        byte[] bt;
        key = key.trim().replace(" ", "");//去掉空格
        while (decodetime > 0) {
            bt = Base64.decode(key,Base64.DEFAULT);
            key = new String(bt);
            decodetime--;
        }

        return key;
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        int decodetime = 5;//压缩和解压的次数，防止被简单破解
        byte[] bt = null;
        key = key.trim().replace(" ", "");//去掉空格
        while (decodetime > 0) {
            bt = key.getBytes();
            key = new String(Base64.decode(bt,Base64.DEFAULT));
            decodetime--;
        }

        return key;
    }
}
