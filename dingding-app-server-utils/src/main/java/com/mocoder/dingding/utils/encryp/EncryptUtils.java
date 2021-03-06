package com.mocoder.dingding.utils.encryp;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by yangshuai3 on 2016/2/18.
 * mail:yangshuai3@jd.com
 */
public class EncryptUtils {

    private static final BASE64Encoder base64Encoder = new BASE64Encoder();
    private static final BASE64Decoder base64Decoder = new BASE64Decoder();


    public static String md5(String sourceStr) {
        return DigestUtils.md5Hex(sourceStr);
    }

    public static String sha1(String src) {
        return DigestUtils.sha1Hex(src);
    }

    public static String base64Encode(String src) {
    return base64Encoder.encode(StringUtils.getBytesUtf8(src));
    }

    public static String base64Decode(String src) throws IOException {
        return StringUtils.newStringUtf8(base64Decoder.decodeBuffer(src));
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后字符串
     * @throws Exception
     */
    public static String desEncode(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(StringUtils.getBytesUtf8(key));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(StringUtils.getBytesUtf8("12345678"));
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(StringUtils.getBytesUtf8(data));
            return Hex.encodeHexString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("des加密失败", e);
        }
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字符串
     * @throws Exception 异常
     */
    public static String desDecode(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(StringUtils.getBytesUtf8(key));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(StringUtils.getBytesUtf8("12345678"));
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = Hex.decodeHex(data.toCharArray());
            return StringUtils.newStringUtf8(cipher.doFinal(bytes));
        } catch (Exception e) {
            throw new RuntimeException("des解密失败", e);
        }
    }

    public static void main(String[] args) throws DecoderException {
        String str = base64Encode("[{\"mobile\":\"1565230哈哈60\",\"age\":3]}");
        System.out.println(str);
        try {
            System.out.println(base64Decode(str));
        } catch (IOException e) {
        }
        String str2 = desEncode("123123123","[{\"mobile\":\"1565230哈哈60\",\"age\":3]}");
        System.out.println(str2);
        System.out.println(desDecode("123123123",str2));
    }
}
