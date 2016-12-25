package in.pathri.codenvydownload.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.CommandDetails;
import in.pathri.codenvydownload.client.dao.ProjectDetails;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.SetupData;
import in.pathri.codenvydownload.dao.SummaryTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.screens.SetupScreen;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 24-12-2016.
 */

public class MainService {
    private static final String className = MainService.class.getSimpleName();

    public static void BuildScreenOnPause(Context context) {
        AppData.onPause(context);
    }

    public static void BuildScreenOnResume(Context context) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String username = myPrefs.getString(SetupScreen.USER_NAME, "");
        String workspaceID = myPrefs.getString(SetupScreen.WORKSPACE, "");
        String workspaceName = myPrefs.getString(SetupScreen.WORKSPACE_NAME, "");
        String project = myPrefs.getString(SetupScreen.PROJECT, "");
        String command = myPrefs.getString(SetupScreen.COMMAND, "");

        AppData.onResume(context);

        if (command != "") {
            command = " ; Command: " + command;
        }

//        CodenvyClient.apiInit();

        String statusMsg;
        if (username == "") {
            statusMsg = "Please Login and Select a workspace,project in settings page";
        } else if (workspaceID == "" || project == "") {
            statusMsg = "Please Select a workspace,project in settings page";
        } else if (command == "") {
            statusMsg = "Please Select a command in settings page";
        } else {
            statusMsg = "Project: " + workspaceName + "/" + project + command;
        }
        BuildScreen.updateCurrentState(statusMsg);
    }

    public static void doBuildLogin() {
        AppData.clearBuildData();
//        CodenvyClient.postBuildLogin();
    }

    public static void SetupScreenOnResume(Context context, SharedPreferences sharedPreferences) {
        AppData.onResume(context);
        SetupData.onResume(context);
        String temp = sharedPreferences.getString(SetupScreen.USER_NAME, "");
        if (!temp.equals("")) {
            SetupScreen.SetupFragment.updateSummary(SummaryTextType.USER_CRED, "Logged In User: " + temp);
            if (SetupData.getWorkspaceIdMap().isEmpty()) {
                CodenvyClient.getWorkspaceDetails();
            } else {
                SetupScreen.SetupFragment.updateWorspacePreference();
                SetupScreen.SetupFragment.updateProjectPreference();
                SetupScreen.SetupFragment.updateCommandPreference();
            }
        }
    }

    public static void SetupScreenOnPause(Context context) {
        AppData.onPause(context);
        SetupData.onPause(context);
    }

    public static void refreshDetails() {
        CodenvyClient.getWorkspaceDetails();
    }

    public static void updateWorspacePreference(String[] namesArr, String[] idsArr) {
        if (namesArr.length > 0) {
            SetupData.setWorkspaceEntries(namesArr);
            SetupData.setWorkspaceValues(idsArr);
        } else {
            SetupScreen.SetupFragment.updateSummary(SummaryTextType.WORKSPACE, "No Workspace Available for this User");
        }
        SetupScreen.SetupFragment.updateWorspacePreference();
        getProjectLists();
        SetupScreen.SetupFragment.updateProjectPreference();
    }

    public static void getProjectLists() {
        String[] ids = SetupData.getWorkspaceValues();
        SetupData.setProjectWids(ids);
        SetupData.setCommadWids(ids);
        SetupScreen.projectProgressDialog.show();
        for (String wid : ids) {
            getProjectDetails(wid);
            getCommandDetails(wid);
        }
    }

    public static void getProjectDetails(String wid) {
        CustomLogger.d(className, "getProjectIdMap", "wid", wid);
        List<String> names = new ArrayList<String>();
        CodenvyResponse workspaceDetails = SetupData.getWorkspaceDetailsMap().get(wid);
        Iterator<ProjectDetails> iterator = workspaceDetails.getWorkspaceDetails().projects.iterator();
        while (iterator.hasNext()) {
            ProjectDetails projectDetails = iterator.next();
            String name = projectDetails.name;
            names.add(name);
        }
        final String[] namesArr = names.toArray(new String[names.size()]);
        SetupData.addProjectMap(wid, namesArr);
    }

    public static void getCommandDetails(String wid) {
        CustomLogger.d(className, "getCommandDetails", "wid", wid);
        List<String> names = new ArrayList<String>();
        CodenvyResponse workspaceDetails = SetupData.getWorkspaceDetailsMap().get(wid);
        Iterator<CommandDetails> iterator = workspaceDetails.getWorkspaceDetails().commands.iterator();
        while (iterator.hasNext()) {
            CommandDetails commandDetails = iterator.next();
            String name = commandDetails.name;
            names.add(name);
            AppData.addCommandMap(wid, name, commandDetails);
        }
        final String[] namesArr = names.toArray(new String[names.size()]);
        SetupData.addCommandMap(wid, namesArr);
    }


    public static void installAPK(String fileRelPath) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setDataAndType(Uri.fromFile(new File(fileRelPath)), "application/vnd.android.package-archive");
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BuildScreen.context.startActivity(promptInstall);
    }
}
