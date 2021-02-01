package client.util;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @author baochaoh
 * @title: ByteUtils
 * @projectName netty-server
 * @description: TODO
 * @date 2019/4/1116:00
 */
public class ByteUtils {

    /***
     * byte 合并
     * @param bt1
     * @param bt2
     * @return
     */
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] byteMerger(byte[] bt1, ByteBuf buffer, int readlen){
        byte[] values = new byte[readlen];
        buffer.readBytes(values);
        return byteMerger(bt1,values);
    }



    /**
     * Remove leading 0x00's from a byte array.
     */
    public static byte[] stripLeadingNullBytes(byte[] input) {
        byte[] result = Arrays.copyOf(input, input.length);
        while (result.length > 0 && result[0] == 0x00) {
            result = Arrays.copyOfRange(result, 1, result.length);
        }
        return result;
    }

    static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    /**
     * Convert a byte array into its hex string equivalent.
     */
    public static String toHexString(byte[] data) {
        char[] chars = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            chars[i * 2] = HEX_DIGITS[(data[i] >> 4) & 0xf];
            chars[i * 2 + 1] = HEX_DIGITS[data[i] & 0xf];
        }
        return new String(chars).toLowerCase();
    }

    /**
     * Convert a hex string into its byte array equivalent.
     */
    public static byte[] toByteArray(String data) {
        if (data == null) {
            return new byte[] {};
        }

        if (data.length() == 0) {
            return new byte[] {};
        }

        while (data.length() < 2) {
            data = "0" + data;
        }

        if (data.substring(0, 2).toLowerCase().equals("0x")) {
            data = data.substring(2);
        }
        if (data.length() % 2 == 1) {
            data = "0" + data;
        }

        data = data.toUpperCase();

        byte[] bytes = new byte[data.length() / 2];
        String hexString = new String(HEX_DIGITS);
        for (int i = 0; i < bytes.length; i++) {
            int byteConv = hexString.indexOf(data.charAt(i * 2)) * 0x10;
            byteConv += hexString.indexOf(data.charAt(i * 2 + 1));
            bytes[i] = (byte) (byteConv & 0xFF);
        }

        return bytes;
    }

    /**
     * Reverse the endian-ness of a byte array.
     *
     * @param data Byte array to flip
     * @return Flipped array
     */
    public static byte[] flipEndian(byte[] data) {
        if (data == null) {
            return new byte[0];
        }
        byte[] newData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            newData[data.length - i - 1] = data[i];
        }

        return newData;
    }

    /**
     * Pad a byte array with leading bytes.
     *
     * @param data Data that needs padding
     * @param size The final desired size of the data.
     * @param pad The byte value to use in padding the data.
     * @return A padded array.
     */
    public static byte[] leftPad(byte[] data, int size, byte pad) {
        if (size <= data.length) {
            return data;
        }

        byte[] newData = new byte[size];
        for (int i = 0; i < size; i++) {
            newData[i] = pad;
        }
        for (int i = 0; i < data.length; i++) {
            newData[size - i - 1] = data[data.length - i - 1];
        }

        return newData;
    }

    /**
     * Reads a section of a byte array and returns it as its own byte array, not unlike a substring.
     *
     * @param data Byte array to read from.
     * @param start Starting position of the desired data.
     * @param size Size of the data.
     * @return Byte array containing the desired data.
     */
    public static byte[] readBytes(byte[] data, int start, int size) {
        if (data.length < start + size) {
            return new byte[0];
        }

        byte[] newData = Arrays.copyOfRange(data, start, start + size);

        return newData;
    }

    public static byte[] bytesAppendArray(byte[] firstByte, byte[] appendBytes, int appedLen) {
        byte[] result = new byte[firstByte.length + appedLen];
        System.arraycopy(firstByte, 0, result, 0, firstByte.length);
        System.arraycopy(appendBytes, 0, result, firstByte.length, appedLen);
        return result;
    }

    public static byte[] bytesAppendArray(byte[] firstByte, byte[] appendBytes) {
        return bytesAppendArray(firstByte, appendBytes, appendBytes.length);
    }

}
