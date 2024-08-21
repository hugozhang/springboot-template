package com.winning.hmap.portal.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 工具类   和医院端一致  相同加密过程  代码来自polaris和医健   因为依赖比较多  单独拿下来
 *
 * @author: hugo.zxh
 * @date: 2022/10/18 10:59
 * @description:
 */
@Slf4j
public class EncryptUtils {

    private static String strDefaultKey = "winning";

    private Cipher encryptCipher = null;

    private Cipher decryptCipher = null;

    public static final  String UTF_CODE = "UTF-8";

    private static final String transformation = "DES";

    public static final String START_WITH = "{DES}";

    private static EncryptUtils instance = null;

    public enum Algorithm {
        SHA,//SHA算法
        MD5;//MD5算法
        public String getPrefix() {
            return "{" + name() + "}";
        }
    }

    /**
     * 获取唯一进程限制的实例
     * @Title: getInstance
     * @return  唯一进程实例
     */
    public static EncryptUtils getInstance() {
        if(instance == null) {
            try {
                instance = new EncryptUtils();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        return instance;
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB)  {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws UnsupportedEncodingException
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     *
     */
    public static byte[] hexStr2ByteArr(String strIn) throws UnsupportedEncodingException  {
        byte[] arrB = strIn.getBytes(UTF_CODE);
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 默认构造方法，使用默认密钥
     *
     * @throws Exception
     */
    public EncryptUtils() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        this(strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     *
     * @param strKey
     *            指定的密钥
     * @throws Exception
     */
    public EncryptUtils(String strKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, NoSuchPaddingException {
        Key key = getKey(strKey.getBytes(UTF_CODE));

        encryptCipher = Cipher.getInstance(transformation);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance(transformation);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes(UTF_CODE)));
    }

    /**
     * 加密字符串
     *
     * @param startsWith 加密具有特殊前缀的字符串
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String startsWith, String strIn) throws Exception {
        return startsWith + byteArr2HexStr(encrypt(strIn.getBytes(UTF_CODE)));
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)),UTF_CODE);
    }

    /**
     * 解密字符串
     *
     * @param startsWith 解密具有特殊前缀的字符串
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String startsWith, String strIn) throws Exception {
        if(strIn.startsWith(startsWith)){
            return new String(decrypt(hexStr2ByteArr(strIn.substring(startsWith.length()))),UTF_CODE);
        } else {
            return strIn;
        }
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private Key getKey(byte[] arrBTmp) {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new SecretKeySpec(arrB, transformation);

        return key;
    }

    /**
     * 使用SHA加密算法加密字符串
     * @param s
     * @param algorithm 加密算法
     * @return
     * @throws Exception
     */
    public static String encrypt(String s, Enum<Algorithm> algorithm) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance(algorithm.name());
            byte[] obj = s.getBytes(UTF_CODE);
            sha.update(obj);
            return "{" + algorithm + "}" + byte2hex(sha.digest());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 二进制转换成十六进制的字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) { // 二进制转字符串
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < b.length; n++) {
            String tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0").append(tmp);
            } else {
                sb.append(tmp);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 将16进制的String转换成byte[]
     *
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        int length = hex.length();
        byte[] result = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            result[i / 2] = Integer.valueOf(hex.substring(i, i + 2), 16).byteValue();
        }
        return result;
    }

    /**
     * Main主函数输出加密结果
     *
     * @param args
     * @return
     */
    public static void main(String[] args) throws Exception {
//        String value = System.getProperty("value");
//        if (StringUtils.isEmpty(value)) {
//            System.out.println("请输入需要加密的字符串 -Dvalue=xxxxxxx");
//            return;
//        }

        int gen4thNumber = NumberUtils.gen4thNumber();
        EncryptUtils encryptUtils = new EncryptUtils(gen4thNumber + "");//用随机数作为秘钥二次加密
        String encryptMac = encryptUtils.encrypt("00-50-56-9B-14-B1");
        EncryptUtils instance = EncryptUtils.getInstance();
        String encrypt2Mac = instance.encrypt(encryptMac + "-" + gen4thNumber);
        System.out.println(encrypt2Mac);
        System.out.println(instance.encrypt("89NPT-polaris-2025-12-01-polaris-" + encrypt2Mac));

    }

}
