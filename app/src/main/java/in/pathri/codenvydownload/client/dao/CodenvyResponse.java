package in.pathri.codenvydownload.client.dao;

import com.google.gson.JsonObject;

/**
 * Created by keerthi on 25-12-2016.
 */
public class CodenvyResponse {
    private WorkspaceDetails config;
    private String id;
    private String status;
    private RuntimeDetails runtime;
    private JsonObject attributes;

    public WorkspaceDetails getWorkspaceDetails() {
        config.setId(id);
        return config;
    }

    public String getStatus() {
        return this.status;
    }

    public String getMachineId() {
        return this.runtime.getMachine(0).getId();
    }

    public String getMachineToken() {
        return this.runtime.getEnvVariable("USER_TOKEN");
    }

    public String getProjectURL() {
        JsonObject server = this.runtime.getServerDetail("ref", "wsagent");
        if (server != null) {
            return server.get("url").getAsString();
        }
        return "";
    }

    public String getAttribute(String key) {
        return attributes.get(key).getAsString();
    }
}
