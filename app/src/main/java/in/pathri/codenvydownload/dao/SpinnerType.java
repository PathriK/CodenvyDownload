package in.pathri.codenvydownload.dao;


import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.screens.LoginScreen;
import in.pathri.codenvydownload.screens.SetupScreen;

/**
 * Created by keerthi on 17-01-2017.
 */

public enum SpinnerType {
    LOGIN_DIALOG("LoginScreen"),
    WORKSPACE_REFRESH("SetupScreen"),
    BUILD_LOGIN("BuildScreen"),
    BUILD_TRIGGER("BuildScreen"),
    BUILD_BUILD("BuildScreen"),
    BUILD_DOWNLOAD("BuildScreen");

    private String baseClass;

    SpinnerType(String baseClass) {
        this.baseClass = baseClass;
    }

    public void showSpinner() {
        switch (this.baseClass) {
            case "BuildScreen":
                BuildScreen.showSpinner(this);
                break;
            case "LoginScreen":
                LoginScreen.showSpinner(this);
                break;
            case "SetupScreen":
                SetupScreen.showSpinner(this);
                break;
        }
    }

    public void hideSpinner() {
        switch (this.baseClass) {
            case "BuildScreen":
                BuildScreen.hideSpinner(this);
                break;
            case "LoginScreen":
                LoginScreen.hideSpinner(this);
                break;
            case "SetupScreen":
                SetupScreen.hideSpinner(this);
                break;
        }
    }

}
