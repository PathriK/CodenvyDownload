package in.pathri.codenvydownload.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.pathri.codenvydownload.R;
import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.LoginData;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 24-12-2016.
 */

public class LoginScreen extends DialogPreference {
    private static final String className = LoginScreen.class.getSimpleName();
    public static ProgressBar loginSpinner;
    private static TextView loginStatus;
    private static AlertDialog d;
    private static Handler statusHandler;
    private static SharedPreferences settings;
    private static String username, password;
    private static boolean isLoggedIn;
    private Context context;
    private EditText loginField, passwordField;

    public LoginScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.login_view);
        this.context = context;
        statusHandler = new Handler();
    }

    public static void acceptLogin() {

        final SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.putString("password", password);

        isLoggedIn = true;
        statusHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                d.dismiss();
                editor.commit();
            }
        }, 1000);
    }

    public static void updateLoginStatus(String statusText) {
        CustomLogger.d(className, "updateLoginStatus", "statusText", statusText);
        loginStatus.setText(statusText);
    }

    public static void showSpinner(SpinnerType spinnerType) {
        switch (spinnerType) {
            case LOGIN_DIALOG:
                loginSpinner.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static void hideSpinner(SpinnerType spinnerType) {
        switch (spinnerType) {
            case LOGIN_DIALOG:
                loginSpinner.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(R.string.login_title);
        builder.setPositiveButton("Login", this);
        builder.setNegativeButton("Cancel", this);
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        isLoggedIn = false;
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);    //Call show on default first so we can override the handlers

        d = (AlertDialog) getDialog();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginStatus = (TextView) d.findViewById(R.id.login_dialog_status);
                loginSpinner = (ProgressBar) d.findViewById(R.id.login_dialog_progress);
                loginField = (EditText) d.findViewById(R.id.login_text);
                passwordField = (EditText) d.findViewById(R.id.password_text);
                username = loginField.getText().toString();
                password = passwordField.getText().toString();

                LoginData loginData = new LoginData(username, password);
                AppData.setLoginData(loginData);
                CodenvyClient.postSettingsLogin();
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        CustomLogger.i(className, "onDialogClosed", "closing");
        persistBoolean(isLoggedIn);
    }
}