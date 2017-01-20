package in.pathri.codenvydownload.dao;

import android.content.Context;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import in.pathri.codenvydownload.client.dao.BuildResult;
import in.pathri.codenvydownload.client.dao.BuildStatus;
import in.pathri.codenvydownload.client.dao.CommandDetails;
import in.pathri.codenvydownload.utilities.CustomLogger;
import in.pathri.codenvydownload.utilities.TableGsonAdapter;

/**
 * Created by keerthi on 24-12-2016.
 */
public class AppData {

    private static final String className = AppData.class.getSimpleName();

    private static LoginData loginData;
    private static String workspaceName;
    private static String workspaceId;
    private static String project;
    private static CommandDetails command;
    private static String buildTaskId;
    private static String apkUrl;
    private static String apkPath;
    private static String machineId;
    private static String guidString;
    private static BuildResult buildResult;
    private static BuildStatus buildStatus;
    private static List<String> buildOutput;
    private static Table<String, String, CommandDetails> commandDetailsMap = HashBasedTable.create();
    private static String machineToken;
    private static String projectURL;
    private static String artifactId;
    private static String artifactExt;

    public static LoginData getLoginData() {
        CustomLogger.d(className, "getLoginData", "loginData", loginData.toString());
        return loginData;
    }

    //Setters
    public static void setLoginData(LoginData loginData) {
        AppData.loginData = loginData;
    }

    public static String getWorkspaceName() {
        CustomLogger.d(className, "getWorkspaceName", "workspaceName", workspaceName);
        return workspaceName;
    }

    public static void setWorkspaceName(String workspaceName) {
        AppData.workspaceName = workspaceName;
    }

    public static String getWorkspaceId() {
        CustomLogger.d(className, "getWorkspaceId", "workspaceId", workspaceId);
        return workspaceId;
    }

    public static void setWorkspaceId(String workspaceId) {
        AppData.workspaceId = workspaceId;
    }

    public static String getProject() {
        CustomLogger.d(className, "getProject", "project", project);
        return project;
    }

    public static void setProject(String project) {
        AppData.project = project;
    }

    public static CommandDetails getCommand() {
        if (command != null) {
            CustomLogger.d(className, "getCommand", "command", "RETURNING INSTANCE");
            return command.getInstance(project);
        } else {
            CustomLogger.d(className, "getCommand", "command", "RETURNING EMPTY COMMAND");
            return new CommandDetails("", "", "");
        }
    }

    public static void setCommand(String command) {
        AppData.command = commandDetailsMap.get(workspaceId, command);
        CustomLogger.d(className, "setCommand", "command|commandDetail", command + "|" + AppData.command);
    }

    public static String getBuildTaskId() {
        CustomLogger.d(className, "getBuildTaskId", "buildTaskId", buildTaskId);
        return buildTaskId;
    }

    public static void setBuildTaskId(String buildTaskId) {
        AppData.buildTaskId = buildTaskId;
    }

    public static String getApkUrl() {
        CustomLogger.d(className, "getApkUrl", "apkUrl", apkUrl);
        return apkUrl;
    }

    public static void setApkUrl(String apkUrl) {
        AppData.apkUrl = apkUrl;
    }

    public static String getApkPath() {
        CustomLogger.d(className, "getApkPath", "apkPath", apkPath);
        return apkPath;
    }

    public static void setApkPath(String apkPath) {
        AppData.apkPath = apkPath;
    }

    public static String getApkFileName() {
        return "CodenvyDownload-" + buildTaskId + ".apk";
    }

    public static String getMachineId() {
        return machineId;
    }

    public static void setMachineId(String machineId) {
        AppData.machineId = machineId;
    }

    public static String getGUID() {
//	  return guidString;
        return "491d48b8-b1ae-4f8b-b5b8-348c7fdec50e".toUpperCase();
    }

    public static BuildResult getBuildResult() {
        CustomLogger.d(className, "getBuildResult", "buildResult", buildResult.toString());
        return buildResult;
    }

    public static void setBuildResult(BuildResult buildResult) {
        AppData.buildResult = buildResult;
    }

    public static BuildStatus getBuildStatus() {
        return buildStatus;
    }

    public static void setBuildStatus(BuildStatus buildStatus) {
        AppData.buildStatus = buildStatus;
    }

    public static String getBuildOutput() {
        String output = Joiner.on(System.getProperty("line.separator")).join(buildOutput);
        CustomLogger.d(className, "getBuildOutput", "output", output);
        return output;
    }

    public static void addBuildOutput(String line) {
        CustomLogger.d(className, "addBuildOutput", "line", line);
        AppData.buildOutput.add(line);
    }

    public static void addCommandMap(String wid, String commandName, CommandDetails command) {
        CustomLogger.d(className, "addCommandMap", "wid::commandName::command=", wid + "::" + commandName + "::" + command);
        try {
            commandDetailsMap.put(wid, commandName, command);
            CustomLogger.d(className, "addCommandMap", "commandDetailsMap", commandDetailsMap.toString());
        } catch (Exception e) {
            CustomLogger.e(className, "addCommandMap", "error::", e);
        }

    }

    //Generators
    public static void generateGUID() {
        UUID uuid = UUID.randomUUID();
        AppData.guidString = uuid.toString();
    }

    public static void clearAll() {
        CustomLogger.i(className, "ClearAll", "Into Function");
        workspaceName = workspaceId = project = "";
        loginData = new LoginData("", "");
        command = new CommandDetails("", "", "");
        commandDetailsMap.clear();
        commandDetailsMap = HashBasedTable.create();
        clearBuildData();
    }

    public static void clearBuildData() {
        CustomLogger.i(className, "clearBuildData", "Into Function");
        buildTaskId = apkUrl = apkPath = machineId = guidString = machineToken = projectURL = artifactId = artifactExt = "";
        if (buildOutput != null) {
            buildOutput.clear();
        } else {
            buildOutput = new ArrayList<String>();
        }
        buildResult = BuildResult.NOT_SET;
        buildStatus = BuildStatus.NOT_STARTED;
    }

    public static String getMachineToken() {
        return machineToken;
    }

    public static void setMachineToken(String machineToken) {
        AppData.machineToken = machineToken;
    }

    public static String getProjectURL() {
        return projectURL;
    }

    public static void setProjectURL(String projectURL) {
        AppData.projectURL = projectURL;
    }

    public static String getArtifactId() {
        return artifactId;
    }

    public static void setArtifactId(String artifactId) {
        AppData.artifactId = artifactId;
    }

    public static String getArtifactExt() {
        return artifactExt;
    }

    public static void setArtifactExt(String artifactExt) {
        AppData.artifactExt = artifactExt;
    }

    public static void onPause(Context context) {
        CustomLogger.i(className, "onPause", "into onPause");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        gsonBuilder.registerTypeAdapter(Table.class, new TableGsonAdapter());
        Gson gson = gsonBuilder.create();
        TinyDB tinydb = new TinyDB(context);
        tinydb.putObject("AppData", new AppData(), gson);
    }

    public static void onResume(Context context) {
        CustomLogger.i(className, "onResume", "into onResume");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        gsonBuilder.registerTypeAdapter(Table.class, new TableGsonAdapter());
        Gson gson = gsonBuilder.create();
        TinyDB tinydb = new TinyDB(context);
        try {
            if (tinydb.contains("AppData")) {
                tinydb.getObject("AppData", AppData.class, gson);
            } else {
                CustomLogger.i(className, "onResume", "AppData Key not present");
            }

        } catch (Exception e) {
            CustomLogger.e(className, "onResume", "AppData Restore", e);
        }

    }

}
