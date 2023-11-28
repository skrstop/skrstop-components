package com.jphoebe.framework.components.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author 蒋时华
 * @date 2017/12/14
 */
public class DesUtil {

    private final static String DES = "DES";
    private final static String ENCODE = "utf-8";
    private final static String defaultKey = "y90dJXdg";

    /**
     * 使用 默认key 加密
     *
     * @return String
     */
    public static String encrypt(String data) throws Exception {
        return encrypt(data, defaultKey);
    }

    /**
     * 使用 默认key 解密
     *
     * @return String
     */
    public static String decrypt(String data) throws Exception {
        return decrypt(data, defaultKey);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), key.getBytes(ENCODE));
        Base64.Encoder encoder = Base64.getEncoder();
        String str = encoder.encodeToString(bt);
        return str;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        if (data == null) {
            return null;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] buf = decoder.decode(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secureKey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
        return cipher.doFinal(data);
    }

    /**
     * 将二进制转化为16进制字符串
     *
     * @param b 二进制字节数组
     * @return String
     */
    public static String byte2hex(byte[] b) {
        if (null == b) {
            return null;
        }
        StringBuffer hs = new StringBuffer();
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

    /**
     * 十六进制字符串转化为2进制
     *
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) throws Exception {
        if (null == hex || hex.length() % 2 != 0) {
            throw new Exception("string to hex is null or invalid length!");
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

}
