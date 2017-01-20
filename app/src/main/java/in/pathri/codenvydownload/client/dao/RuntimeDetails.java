package in.pathri.codenvydownload.client.dao;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by keerthi on 15-01-2017.
 */
public class RuntimeDetails {
    private List<MachineDetails> machines;
    private JsonObject servers;
    private JsonObject envVariables;

    public List<MachineDetails> getMachies() {
        return machines;
    }

    public MachineDetails getMachine(int i) {
        return machines.get(i);
    }

    public JsonObject getServerDetail(String searchKey, String searchValue) {
        JsonObject servers = this.servers;
        for (Map.Entry<String, JsonElement> server : servers.entrySet()) {
            String ref = server.getValue().getAsJsonObject().get(searchKey).getAsString();
            if (ref.equalsIgnoreCase(searchValue)) {
                return server.getValue().getAsJsonObject();
            }
        }
        return null;
    }

    public String getEnvVariable(String key) {
        return this.envVariables.get(key).getAsString();
    }

}
