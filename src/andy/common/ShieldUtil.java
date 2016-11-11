package andy.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 关键字屏蔽
 * @author Andy<andy_513@163.com>
 */
public final class ShieldUtil {
	
	private static final List<String> lists = new ArrayList<String>();
	
	static {
		String string = "config/敏感词库.txt";
		File file = new File(string);
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			InputStreamReader read;
			try {
				read = new InputStreamReader(new FileInputStream(file), "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					System.out.println(lineTxt);
					lists.add(lineTxt);
				}
				read.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // 考虑到编码格式
		}else{
			System.out.println("没有关键字文件");
		}
	}
	
	public static final String validate(String msg) {
		for (int i = 0; i < lists.size(); i++) {
			String value = lists.get(i);
			if (msg.indexOf(value) == -1) continue;
			msg = msg.replaceAll(value, "*");
		}
		return msg;
	}

}
