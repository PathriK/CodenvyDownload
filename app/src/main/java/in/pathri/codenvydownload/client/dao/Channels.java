package in.pathri.codenvydownload.client.dao;

/**
 * Created by keerthi on 15-01-2017.
 */

public enum Channels {
    WORKSPACE_STATUS("WorkspaceStatus", "workspace:_PARAM_:machines_statuses", CodenvyResponseWS.class),
    PROCESS_OUTPUT("processOutput", "process:output:_PARAM_", CodenvyResponseWS.class),
    PROCESS_STATUS("processStatus", "machine:process:_PARAM_", CodenvyResponseWS.class);

    String channelName;
    String channel;
    Class responseClass;

    Channels(String channelName, String channel, Class responseClass) {
        this.channelName = channelName;
        this.channel = channel;
        this.responseClass = responseClass;
    }

    public String getChannel(String param) {
        return this.channel.replace("_PARAM_", param);
    }

    public Class getResponseClass() {
        return this.responseClass;
    }
}
