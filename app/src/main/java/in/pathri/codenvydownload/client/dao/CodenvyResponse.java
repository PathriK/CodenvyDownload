package in.pathri.codenvydownload.client.dao;

/**
 * Created by keerthi on 25-12-2016.
 */
public class CodenvyResponse {
    private WorkspaceDetails config;
    private String id;

    public WorkspaceDetails getWorkspaceDetails() {
        config.setId(id);
        return config;
    }
}
