package in.pathri.codenvydownload.utilities;

import android.util.Log;

/**
 * Created by keerthi on 23-12-2016.
 */
public class CustomLogger {

    public static void d(String className, String methodName, String tag, String data) {
        Log.d(className + "::" + methodName, tag + "::" + data);
    }

    public static void e(String className, String methodName, String message, Throwable error) {
        Log.e(className + "::" + methodName, message + "::" + error.getLocalizedMessage());
    }

    public static void i(String className, String methodName, String message) {
        Log.i(className + "::" + methodName, message);
    }

}
