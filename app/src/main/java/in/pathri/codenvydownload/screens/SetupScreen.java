package in.pathri.codenvydownload.screens;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import in.pathri.codenvydownload.R;
import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.SetupData;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.dao.SummaryTextType;
import in.pathri.codenvydownload.services.MainService;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 24-12-2016.
 */
public class SetupScreen extends PreferenceActivity {
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String WORKSPACE = "workspace";
    public static final String PROJECT = "project";
    public static final String WORKSPACE_NAME = "workspace_name";
    public static final String COMMAND = "command";
    private static final String className = SetupScreen.class.getSimpleName();
    public static SetupFragment setupFragment;
    public static CustomProgressDialog workspaceProgressDialog, projectProgressDialog;
    private static Context context = BuildScreen.context;
    private static Gson gson;
    private static ListPreference workspacePrefs, projectPrefs, commandPrefs;
    private static Preference userCred;
    private static SharedPreferences sharedPreferences;

    public static void showSpinner(SpinnerType spinnerType) {
        switch (spinnerType) {
            case WORKSPACE_REFRESH:
                workspaceProgressDialog.show();
                break;
        }
    }

    public static void hideSpinner(SpinnerType spinnerType) {
        switch (spinnerType) {
            case WORKSPACE_REFRESH:
                workspaceProgressDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragment = new SetupFragment();
        gson = new Gson();
        getFragmentManager().beginTransaction().replace(android.R.id.content, setupFragment).commit();
    }

    public static class SetupFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        public static void updateWorspacePreference() {
            workspacePrefs.setEntries(SetupData.getWorkspaceEntries());
            workspacePrefs.setEntryValues(SetupData.getWorkspaceValues());
            workspacePrefs.setDefaultValue(0);
            CharSequence tempEntry = workspacePrefs.getEntry();
            if (tempEntry == null) {
                tempEntry = "Please Select a Value";
            }
            updateSummary(SummaryTextType.WORKSPACE, tempEntry.toString());
            workspacePrefs.setEnabled(true);
        }

        public static void updateProjectPreference() {
            String temp = sharedPreferences.getString(WORKSPACE, "");
            if (!temp.equals("")) {
                String[] projectsList = SetupData.getProjectIdMap().get(temp);
                if (projectsList != null && projectsList.length != 0) {
                    projectPrefs.setEntries(projectsList);
                    projectPrefs.setEntryValues(projectsList);
                    projectPrefs.setEnabled(true);
                    temp = sharedPreferences.getString(PROJECT, "");
                    if (temp.equals("")) {
                        updateSummary(SummaryTextType.PROJECT, "Please Select a Value");
                        projectPrefs.setDefaultValue(0);
                    } else {
                        updateSummary(SummaryTextType.PROJECT, temp);
                    }
                } else {
                    updateSummary(SummaryTextType.PROJECT, "No Project Available for the selected Workspace");
                    projectPrefs.setEnabled(false);
                }
            } else {
                updateSummary(SummaryTextType.PROJECT, "No Project");
                projectPrefs.setEnabled(false);
            }
        }

        public static void updateCommandPreference() {
            String temp = sharedPreferences.getString(WORKSPACE, "");
            if (!temp.equals("")) {
                CustomLogger.i(className, "updateCommandPreference", "Inside Wid check");
                String[] commandsList = SetupData.getCommandDetails().get(temp);
                if (commandsList != null && commandsList.length != 0) {
                    CustomLogger.i(className, "updateCommandPreference", "Command list non empty");
                    commandPrefs.setEntries(commandsList);
                    commandPrefs.setEntryValues(commandsList);
                    commandPrefs.setEnabled(true);
                    temp = sharedPreferences.getString(COMMAND, "");
                    if (temp.equals("")) {
                        updateSummary(SummaryTextType.COMMAND, "Please Select a Value");
                        commandPrefs.setDefaultValue(0);
                    } else {
                        updateSummary(SummaryTextType.COMMAND, temp);
                    }
                } else {
                    CustomLogger.i(className, "updateCommandPreference", "Command list empty");
                    updateSummary(SummaryTextType.COMMAND, "No Command Available for the selected Workspace");
                    commandPrefs.setEnabled(false);
                }
            } else {
                CustomLogger.i(className, "updateCommandPreference", "Command wid empty");
                updateSummary(SummaryTextType.COMMAND, "No Command");
                commandPrefs.setEnabled(false);
            }
        }

        public static void updateSummary(SummaryTextType summaryTextType, String summary) {
            switch (summaryTextType) {
                case USER_CRED:
                    userCred.setSummary(summary);
                    break;
                case WORKSPACE:
                    workspacePrefs.setSummary(summary);
                    break;
                case PROJECT:
                    projectPrefs.setSummary(summary);
                    break;
                case COMMAND:
                    commandPrefs.setSummary(summary);
                    break;
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            userCred = findPreference("is_logged_in");
            workspacePrefs = (ListPreference) findPreference("workspace");
            projectPrefs = (ListPreference) findPreference("project");
            commandPrefs = (ListPreference) findPreference("command");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

            Button btn = new Button(getActivity().getApplicationContext());
            btn.setText("Refresh");

            v.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if ("-NONE-".equals(sharedPreferences.getString("username", "-NONE-"))) {
                        toastThis("Please Login before refresh");
                    } else {
                        MainService.refreshDetails();
                    }
                }
            });

            return v;
        }

        @Override
        public void onAttach(Activity activity) {
            CustomLogger.i(className, "onAttach", "into Function");
            workspaceProgressDialog = new CustomProgressDialog(getActivity(), "Workspace List Refresh", "Please Wait...");
            projectProgressDialog = new CustomProgressDialog(getActivity(), "Project List Refresh", "Please Wait...");
            super.onAttach(activity);
        }

        @Override
        public void onResume() {
            super.onResume();
            CustomLogger.i(className, "onResume", "into onResume");
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            MainService.SetupScreenOnResume(context, sharedPreferences);
        }

        @Override
        public void onPause() {
            super.onPause();
            CustomLogger.i(className, "onPause", "into onPause");
            // Set up a listener whenever a key changes
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            MainService.SetupScreenOnPause(context);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(USER_NAME)) {
                userCred.setSummary("Logged In User: " + sharedPreferences.getString(USER_NAME, "-NONE-"));
                CodenvyClient.getWorkspaceDetails();
            } else if (key.equals(WORKSPACE)) {
                workspacePrefs.setSummary(workspacePrefs.getEntry());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String workspaceName = workspacePrefs.getEntry().toString();
                editor.putString(WORKSPACE_NAME, workspaceName);
                editor.remove(PROJECT);
                editor.remove(COMMAND);
                editor.commit();
                AppData.setWorkspaceName(workspaceName);
                AppData.setWorkspaceId(workspacePrefs.getValue());
                updateProjectPreference();
                updateCommandPreference();
            } else if (key.equals(PROJECT)) {
                projectPrefs.setSummary(projectPrefs.getEntry());
                AppData.setProject(projectPrefs.getEntry().toString());
            } else if (key.equals(COMMAND)) {
                commandPrefs.setSummary(commandPrefs.getEntry());
                AppData.setCommand(commandPrefs.getEntry().toString());
            }
        }

        private void toastThis(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
