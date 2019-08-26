package com.example.cardscannertwo.mifare;

/**
 * Created by Administrator on 2018/7/26.
 */

public class StringUtils {
    /*public static String bytes2HexString(byte[] b, int size) {
        String ret = "";

        try {
            for (int i = 0; i < size; i++) {
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                //   ret += hex.toUpperCase();
                ret = ret+("0x"+hex.toUpperCase()+",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static String bytes2HexString2(byte[] b, int size) {
        StringBuilder stringBuilder=new StringBuilder();

        try {
            for (int i = 0; i < size; i++) {
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    stringBuilder.append("0");
                    //   hex = "0" + hex;
                }
                stringBuilder.append(hex);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }*/



    /*
    byte[] --> 16 hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

/*
  16 hex string--->byte[]
 */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }



}
