package andy.common;

import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Decoder.BASE64Encoder;

/**
 * @author Andy<andy_513@163.com>
 */
public class MD5Utils {
	private static final MessageDigest md;
	private static final BASE64Encoder b64Encoder;

	static {
		try {
			md = MessageDigest.getInstance("MD5", "SUN");
			b64Encoder = new BASE64Encoder();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
//		t2();
		t1();
	}

	private static void t1() {
		String pwd = "123456";
		String pwd1 = null;// PasswdEncryption.toPasswd(pwd);

		for (int i = 0; i < 5; i++) {
			pwd1 = toPasswd(pwd);
			System.out.println(pwd1);
		}
	}

	private static void t2() {
		String[] userPwds = { "123456", "123456", "123456", "123456", "123456"};

		// 通过t1()得到
		String[] dbPwds = { "!bNbm/ovOrRoYGDNnlfuQ3WA==", "%'2+0rO8UTKiUB1pZqbwB3ig==", "}]94JxdKa0VfjNdXV8yHfr7A==", "nF1vnclCeTkpT4/Bz57T7FTQ==",
				"uHnukNilkuqF4V1DOrJzxFZg=="};

		String msg = "";

		for (int i = 0, end = dbPwds.length; i < end; i++) {
			msg = "user input passwd:" + userPwds[i];
			msg += ",db store passwd:" + dbPwds[i];
			msg += ",check passwd:" + checkPasswd(userPwds[i], dbPwds[i]);
			System.out.println(msg);
		}
	}

	/**
	 * 检查密码
	 * 
	 * @param inputPasswd
	 *            用户输入的密码
	 * @param storePasswd
	 *            已存储的密码
	 * @return true:通过检查,false:未通过
	 */
	synchronized public static boolean checkPasswd(String inputPasswd, String storePasswd) {
		boolean ok = false;

		try {
			byte[] saltBys = storePasswd.substring(0, 2).getBytes("UTF-8");
			String inPwd = toPasswd(inputPasswd, saltBys);
			ok = inPwd.equals(storePasswd);
		} catch (Exception ex) {
			ex.printStackTrace();
			ok = false;
		}

		return ok;
	}

	/**
	 * 将客户输入的密码加密
	 * 
	 * @param inputPasswd
	 * @return
	 */
	synchronized public static String toPasswd(String inputPasswd) {
		byte[] salt = getSaltOfASCII(2);
		return toPasswd(inputPasswd, salt);
	}

	/**
	 * 将客户输入的密码加密
	 * 
	 * @param inputPasswd
	 *            客户输入的密码
	 * @param salt
	 *            盐
	 * @return 加密后的字符串
	 */
	synchronized private static String toPasswd(String inputPasswd, byte[] salt) {
		String pwd = "";
		try {
			md.reset();
			md.update(salt);
			md.update(inputPasswd.getBytes("UTF-8"));
			byte[] bys = md.digest();
			pwd += (char) salt[0];
			pwd += (char) salt[1];
			pwd += b64Encoder.encode(bys);
		} catch (Exception ex) {
			ex.printStackTrace();
			pwd = "";
		}
		return pwd;
	}

	/**
	 * 返回指定长度的盐(ASCII码)
	 * 
	 * @param len
	 *            长度
	 * @return
	 */
	private static byte[] getSaltOfASCII(int len) {
		byte[] salt = new byte[len];
//		Random rand = new Random();
		for (int i = 0; i < len; i++) {
			// salt[i] = 'A';
			salt[i] = (byte) ((ThreadLocalRandom.current().nextInt('~' - '!') + '!') & 0x007f);
		}
		return salt;
	}
}
