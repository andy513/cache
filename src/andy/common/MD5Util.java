package andy.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;

import com.alibaba.fastjson.JSONObject;

import Decoder.BASE64Encoder;

/**
 * @author Andy<andy_513@163.com>
 */
public class MD5Util {
	private static final char[] cs = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','!','@','#','$','%','^','&','*','(',')','<','>','?','{','}','[',']',';',':',',','-','=','+'};
	public static void main(String[] args) throws Exception {
		/*System.out.println(Integer.parseInt("7f", 16));
		System.out.println((char)127);*/
		
		String str = encryption("20121221");
		System.out.println(MD5Util.MD5("20121221"));
		System.out.println(MD5Util.MD5("20121221").equals(str));
		System.out.println(MD5Util.MD5("加密"));
		 
		/*long time = System.currentTimeMillis();
		int min = 0;
		int max = 0;
		Map<Integer, Character> map = new HashMap<Integer, Character>();
		for (int i = 0; i < 10000; i++) {
//			System.out.println(new String(getSaltOfASCII(50)));
//			int c = (ThreadLocalRandom.current().nextInt(33,126));
			int c = (ThreadLocalRandom.current().nextInt('~' - '!') + '!') & 0x007f;
			if(min == 0){
				min = c;
			}
			if (c < min) {
				min = c;
			} else if (c > max) {
				max = c;
			}
			map.put(c, (char) c);
//			System.out.println(c+"\t"+(char)c);
			String str = "";
			for (int j = 0; j < 8; j++) {
				str += cs[ThreadLocalRandom.current().nextInt(cs.length)];
			}
			extracted(str.getBytes());
		}
		for (int i = 0; i < 250; i++) {
			System.out.println(i + "\t" + (char) i);
		}*/
		/*Collections.checkedMap(map, Integer.class, Character.class);
		System.out.println(JSONObject.toJSONString(map));
		System.out.println(min + "(" + (char) min + ")" + "\t" + max + "(" + (char) max + ")");
		System.out.println((byte)'~');
		System.out.println((byte)'!');
		System.out.println(cs.length);*/
//			extracted(getSaltOfASCII(4));
		// System.out.println(newstr);
	}

	private static void extracted(byte[] pwds) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		String msg = "20121221";
		// byte[] pwds = "n^kd".getBytes();
		/*System.out.println(new String(pwds));
		System.out.println(JSONObject.toJSONString(pwds));*/
		String newstr = extracted(md5, base64en, msg, pwds);
		byte[] b = newstr.substring(0, pwds.length).getBytes();
		String pwd = extracted(md5, base64en, msg, b);
		if(!pwd.equals(newstr)){
			System.out.println(newstr + "\t" + pwd + "\t");
		}
	}

	private static byte[] getSaltOfASCII(int len) {
		byte[] salt = new byte[len];
		for (int i = 0; i < len; i++) {
			// salt[i] = 'A';
			salt[i] = (byte) ((ThreadLocalRandom.current().nextInt('~' - '!') + '!') & 0x007f);
		}
		return salt;
	}

	private static String extracted(MessageDigest md5, BASE64Encoder base64en, String msg, byte[] b) {
		String pwd = "";
		md5.reset();
		md5.update(b);
		md5.update(msg.getBytes());
		for (int i = 0; i < b.length; i++) {
			pwd += (char) b[i];
		}
		pwd += base64en.encode(md5.digest());
		return pwd;
	}

	public static String encryption(String plain) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plain.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			re_md5 = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5.toUpperCase();

	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
