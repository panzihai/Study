package xsh.base64;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64加解密
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 11:11
 */
public class Base64Utils {

    private final static String SECRET_KEY = "xsh-";

    public static String Base64Encode(String value,String codeType) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(value.getBytes(codeType));
    }

    public static String Base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static String Base64Encode(String value) throws UnsupportedEncodingException {
        return Base64Encode(SECRET_KEY + value,"utf-8");
    }

    public static  byte[] Base64DecodeToByte(String value) {
        byte[] base64decodedBytes = Base64.getDecoder().decode(value);
        return base64decodedBytes;
    }

    public static  String Base64Decode(String value,String codeType) throws UnsupportedEncodingException {
        byte[] base64decodedBytes = Base64DecodeToByte(value);
        return new String(base64decodedBytes,codeType);
    }

    public static  String Base64Decode(String value) throws UnsupportedEncodingException {
        return Base64Decode(value,"utf-8");
    }
}
