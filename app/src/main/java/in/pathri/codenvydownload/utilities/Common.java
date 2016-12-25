package in.pathri.codenvydownload.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by keerthi on 24-12-2016.
 */

public class Common {
    public static String getStackTraceString(Throwable t) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        String s = writer.toString();
        return s;
    }
}
