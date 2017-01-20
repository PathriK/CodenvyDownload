package in.pathri.codenvydownload.client;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import in.pathri.codenvydownload.client.dao.Channels;
import in.pathri.codenvydownload.client.dao.CodenvyResponseWS;
import in.pathri.codenvydownload.dao.Global;
import in.pathri.codenvydownload.responsehandlers.WSResponseHandler;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 25-12-2016.
 */
public class CodenvyWSClient extends WebSocketAdapter {
    private static final String className = CodenvyWSClient.class.getSimpleName();
    private static Table<String, Channels, CodenvyWSClient> clientChannelMap = HashBasedTable.create();
    private static String cookieString = "";
    private WebSocket ws;
    private Channels channel;
    private WSResponseHandler responseHandler;
    private String channelMsg = "";

    public CodenvyWSClient(String wid, Channels channel) throws IOException {
        CustomLogger.i(className, "CodenvyWSClient", "Inside Constructor");
        System.out.println("WS Creation::Cookie::" + cookieString);
        this.ws = new WebSocketFactory()
                .setConnectionTimeout(Global.TIMEOUT)
                .createSocket(Global.WS_BASE_URL)
                .addListener(this)
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .addHeader("Cookie", cookieString);
        this.channel = channel;
    }

    public static CodenvyWSClient getInstance(String wid, Channels channel) {
        CustomLogger.d(className, "getInstance", "wid|channel", wid + "|" + channel);
        if (!clientChannelMap.contains(wid, channel)) {
            CustomLogger.i(className, "CodenvyWSClient", "Creating new instance for " + channel.name());
            try {
                clientChannelMap.put(wid, channel, new CodenvyWSClient(wid, channel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CodenvyWSClient tempClient = clientChannelMap.get(wid, channel);
        tempClient.connect();
        return tempClient;
    }

    public static void updateCookie(List<String> cookies) {
        cookieString = Joiner.on(";").join(cookies);
    }

    private void connect() {
        CustomLogger.i(className, "connect", "Into Connect");
        if (!ws.isOpen()) {
            ws.connectAsynchronously();
        } else {
            connectChannel();
        }
    }

    public void initChannel(String param, WSResponseHandler responseHandler) {
        CustomLogger.d(className, "initChannel", "param|responseHandler", param + "|" + responseHandler.getClass().getName());
        this.responseHandler = responseHandler;

        JSONObject headersObj = new JSONObject();
        try {
            headersObj.putOpt("name", Global.CHANNEL_HEADER_NAME);
            headersObj.putOpt("value", Global.CHANNEL_HEADER_VALUE);

            JSONArray headersArr = new JSONArray();
            headersArr.put(headersObj);

            JSONObject bodyObj = new JSONObject();
            bodyObj.putOpt("channel", channel.getChannel(param));

            JSONObject msgObj = new JSONObject();
            msgObj.putOpt("method", "POST");
            msgObj.putOpt("headers", headersArr);
            msgObj.putOpt("body", bodyObj.toString());
            this.channelMsg = msgObj.toString();
            System.out.println("Message: " + channelMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void connectChannel() {
        ws.sendText(channelMsg);
    }

    public void closeChannel() {
        CustomLogger.i(className, "closeChannel", "Into closeChannel");
        if (ws.isOpen()) {
            ws.disconnect();
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        CustomLogger.i(className, "onConnected", "Into onConnected");
        connectChannel();
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        CustomLogger.d(className, "onDisconnected", "closedByServer|serverCloseFrame|clientCloseFrame", closedByServer + "|" + serverCloseFrame + "|" + clientCloseFrame);
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        CustomLogger.d(className, "onTextMessage", "text|responseHandler", text + "|" + responseHandler.getClass().getSimpleName());

        Gson gson = new Gson();
        CodenvyResponseWS response = (CodenvyResponseWS) gson.fromJson(text, channel.getResponseClass());
        CustomLogger.d(className, "onTextMessage", "statusCode", "" + response.getStatusCode());
        if (response.getStatusCode() == 0) {
            this.responseHandler.nextStep(response);
        } else if (response.getStatusCode() == 200) {
            this.responseHandler.onConnect();
        }

    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        CustomLogger.d(className, "onBinaryMessage", "binary", binary.toString());
        System.out.println("onBinaryMessage");
    }

    ///Error Methods
    private void errorLogger(String errorName, Throwable cause) {
        CustomLogger.d(className, "errorLogger", errorName, cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        errorLogger("onError", exception);
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        errorLogger("onError", cause);
    }


    @Override
    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        errorLogger("onFrameError", cause);
    }


    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
        errorLogger("onMessageError", cause);
    }


    @Override
    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
        errorLogger("onMessageDecompressionError", cause);
    }


    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
        errorLogger("onTextMessageError", cause);
    }


    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        errorLogger("onSendError", cause);
    }


    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        errorLogger("onUnexpectedError", cause);
    }


    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        errorLogger("handleCallbackError", cause);
    }
}
