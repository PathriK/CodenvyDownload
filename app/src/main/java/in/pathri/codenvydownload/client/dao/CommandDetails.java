package in.pathri.codenvydownload.client.dao;

/**
 * Created by keerthi on 24-12-2016.
 */
public class CommandDetails {
    public String commandLine;
    public String name;
    public String type;

    public CommandDetails(String newCommandLine, String name, String type) {
        this.name = name;
        this.commandLine = newCommandLine;
        this.type = type;
    }

    @Override
    public String toString() {
        return "{name:" + this.name + ",commandLine:" + this.commandLine + "}";
    }

    public CommandDetails getInstance(String project) {
        String newCommandLine = commandLine.replace("${current.project.path}", "/projects/" + project);
        return new CommandDetails(newCommandLine, name, type);
    }
}
