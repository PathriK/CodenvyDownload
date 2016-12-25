package in.pathri.codenvydownload.client.dao;

import java.util.List;

/**
 * Created by keerthi on 25-12-2016.
 */

public class WorkspaceDetails {
    public String name;
    public String id;
    public List<ProjectDetails> projects;
    public List<CommandDetails> commands;

    public void setId(String id) {
        this.id = id;
    }
}
