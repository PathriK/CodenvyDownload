package in.pathri.codenvydownload.dao;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mukesh.tinydb.TinyDB;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.screens.SetupScreen;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 24-12-2016.
 */

public class SetupData {
    private static final String className = AppData.class.getSimpleName();

    private static Map<String, String> workspaceIdMap = new HashMap<String, String>();
    private static String[] workspaceEntries;
    private static String[] workspaceValues;
    private static Map<String, String[]> projectIdMap = new HashMap<String, String[]>();

    private static LinkedList<String> projectWids;
    private static LinkedList<String> commadWids;
    private static Map<String, CodenvyResponse> workspaceDetailsMap;
    private static Map<String, String[]> commandDetails = new HashMap<String, String[]>();

    public static Map<String, CodenvyResponse> getWorkspaceDetailsMap() {
        return workspaceDetailsMap;
    }

    public static void setWorkspaceDetailsMap(Map<String, CodenvyResponse> workspaceDetailsMap) {
        SetupData.workspaceDetailsMap = workspaceDetailsMap;
    }

    public static void setProjectWids(String[] ids) {
        SetupData.projectWids = new LinkedList<String>(Arrays.asList(ids));
    }

    public static void setCommadWids(String[] ids) {
        SetupData.commadWids = new LinkedList<String>(Arrays.asList(ids));
    }

    public static boolean removeProjectWid(String wid) {
        SetupData.projectWids.remove(wid);
        return SetupData.projectWids.isEmpty();
    }

    public static boolean removeCommandWid(String wid) {
        SetupData.commadWids.remove(wid);
        return SetupData.commadWids.isEmpty();
    }

    public static Map<String, String[]> getCommandDetails() {
        return commandDetails;
    }

    public static void setCommandDetails(Map<String, String[]> commandDetails) {
        SetupData.commandDetails = commandDetails;
    }

    public static Map<String, String> getWorkspaceIdMap() {
        return workspaceIdMap;
    }

    public static void setWorkspaceIdMap(Map<String, String> workspaceIdMap) {
        SetupData.workspaceIdMap = workspaceIdMap;
    }

    public static String[] getWorkspaceEntries() {
        return workspaceEntries;
    }

    public static void setWorkspaceEntries(String[] workspaceEntries) {
        SetupData.workspaceEntries = workspaceEntries;
    }

    public static String[] getWorkspaceValues() {
        return workspaceValues;
    }

    public static void setWorkspaceValues(String[] workspaceValues) {
        SetupData.workspaceValues = workspaceValues;
    }

    public static Map<String, String[]> getProjectIdMap() {
        return projectIdMap;
    }

    public static void setProjectIdMap(Map<String, String[]> projectIdMap) {
        SetupData.projectIdMap = projectIdMap;
    }

    public static void addProjectMap(String wid, String[] projectsArr) {
        projectIdMap.put(wid, projectsArr);
        if (removeProjectWid(wid)) {
            SetupScreen.projectProgressDialog.dismiss();
            SetupScreen.SetupFragment.updateProjectPreference();
        }
    }

    public static void addCommandMap(String wid, String[] commandArr) {
        CustomLogger.i(className, "addCommandMap", "into fnf");
        commandDetails.put(wid, commandArr);
        if (removeCommandWid(wid)) {
            SetupScreen.SetupFragment.updateCommandPreference();
        }
    }

    public static void addWorspaceMap(String wid, String name) {
        CustomLogger.i(className, "addWorspaceMap", wid + "::" + name);
        workspaceIdMap.put(wid, name);
    }

    public static void onPause(Context context) {
        CustomLogger.i(className, "onPause", "into onPause");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        Gson gson = gsonBuilder.create();
        TinyDB tinydb = new TinyDB(context);
        tinydb.putObject("SetupData", new SetupData(), gson);
    }

    public static void onResume(Context context) {
        CustomLogger.i(className, "onResume", "into onResume");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        Gson gson = gsonBuilder.create();
        TinyDB tinydb = new TinyDB(context);
        try {
            if (tinydb.contains("SetupData")) {
                tinydb.getObject("SetupData", SetupData.class, gson);
            } else {
                CustomLogger.i(className, "onResume", "SetupData Key not present");
            }

        } catch (Exception e) {
            CustomLogger.e(className, "onResume", "SetupData Restore", e);
        }

    }
}
