package in.pathri.codenvydownload.client.dao;

/**
 * Created by keerthi on 15-01-2017.
 */

public class CodenvyResponseWS {
    private int responseCode;
    private String body;
    private Body messageObject;


    public CodenvyResponseWS setMessageObject(Body messageObject) {
        this.messageObject = messageObject;
        return this;
    }

    public String getStatus() {
        return this.messageObject.getEventType();
    }

    public int getStatusCode() {
        return this.responseCode;
    }

    public String getMessage() {
        return this.body;
    }


    public String getMachineId() {
        return messageObject.getMachineId();
    }

    @Override
    public String toString() {
        return "{responseCode:" + this.responseCode + ",body:" + this.body.toString() + "}";
    }

}
