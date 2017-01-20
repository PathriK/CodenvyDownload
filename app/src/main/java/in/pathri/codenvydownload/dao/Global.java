package in.pathri.codenvydownload.dao;

/**
 * Created by keerthi on 23-12-2016.
 */
public class Global {
    public static final String API_BASE_URL = "https://codenvy.io/api/";
    public static final String WS_BASE_URL = "wss://codenvy.io/api/ws";
    public static final int TIMEOUT = 5000;
    public static String CHANNEL_HEADER_NAME = "x-everrest-websocket-message-type";
    public static String CHANNEL_HEADER_VALUE = "subscribe-channel";
}
