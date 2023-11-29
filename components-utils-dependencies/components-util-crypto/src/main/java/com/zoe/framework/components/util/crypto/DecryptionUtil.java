package com.zoe.framework.components.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @description: 解密算法
 * @author: 蒋时华
 * @date: 2018/6/8
 */
public class DecryptionUtil {

    private final static String DES = "DES";

    private final static String ENCODE = "utf-8";

    /**
     * Description 根据键值进行解密
     *
     * @param data 待解密数据
     * @param key  加密key
     * @return
     * @throws Exception
     */
    public static String desDecrypt(String data, String key) {
        String result = null;
        try {

            if (data == null) {
                return null;
            }
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] buf = decoder.decode(data);
            byte[] bt = desDecrypt(buf, key.getBytes(ENCODE));
            result = new String(bt, ENCODE);
        } catch (Exception e) {
            System.out.println(data + "<====>" + key);
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] desDecrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

}
