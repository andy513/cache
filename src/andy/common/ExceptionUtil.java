package andy.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Andy<andy_513@163.com>
 */
public final class ExceptionUtil {
	
	private static final Logger log = LogManager.getLogger(ExceptionUtil.class);
	
	public static final void printStackTrace(Throwable e) {
		StringBuilder sbr = new StringBuilder();
		for (StackTraceElement ste : e.getStackTrace()) {
			sbr.append("\tat " + ste.getClassName() + "." + ste.getMethodName() + "(");
			String fileName = ste.getFileName();
			sbr.append(fileName == null ? "Unknown Source" : (fileName + ":" + ste.getLineNumber())).append(")\n");
		}
		sbr.deleteCharAt(sbr.lastIndexOf("\n"));
		log.error("\t" + e.getMessage() + "\n" + e.getClass().getName() + "\n" + sbr.toString());
	}
}
