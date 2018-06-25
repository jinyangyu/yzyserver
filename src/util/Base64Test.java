package util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Test {

    private static final String UTF_8 = "UTF-8";

    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
        		e.printStackTrace();
        }

        return null;
    }

    /**
     * 对给定的字符串进行base64加密操作
     */
    public static String encodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.encodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
        		e.printStackTrace();
        }

        return null;
    }
	
	public static void main(String[] args) {
		String name = "我姓👉👉👉王";
		String encodedName = encodeData(name);
		System.out.println("name:" + name);
		System.out.println("encoded Name:" + encodedName);
		
		String realName = decodeData(encodedName);
		System.out.println("real Name:" + realName);
		
		System.out.println("real Name:" + Base64.isBase64("我姓王"));
	}
}
