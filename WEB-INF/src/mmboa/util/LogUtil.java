package mmboa.util;

import org.apache.log4j.Logger;

/**
 * @author bomb
 *  
 */
public class LogUtil {
    public static Logger accessLogger = Logger.getLogger("access.Log");

    public static void logAccess(String str) {
        if (str == null) {
            return;
        }
        accessLogger.error(str);
    }
}
