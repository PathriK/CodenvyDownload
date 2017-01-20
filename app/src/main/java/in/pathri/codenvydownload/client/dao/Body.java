package in.pathri.codenvydownload.client.dao;

/**
 * Created by keerthi on 15-01-2017.
 */

public class Body {
    private String eventType;
    private String machineId;
    private String channel;

    public Body(String eventType, String machineId, String channel) {
        this.channel = channel;
        this.eventType = eventType;
        this.machineId = machineId;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "{channel:" + this.channel + ",eventType:" + this.eventType + ",machineId:" + this.machineId + "}";
    }
}
