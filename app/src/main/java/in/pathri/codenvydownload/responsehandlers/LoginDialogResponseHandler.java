package in.pathri.codenvydownload.responsehandlers;

import java.util.List;

import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.dao.ResponseType;
import in.pathri.codenvydownload.screens.LoginScreen;
import okhttp3.ResponseBody;

/**
 * Created by keerthi on 25-12-2016.
 */
public class LoginDialogResponseHandler extends ApiResponseHandler<CodenvyResponse> {
    private static final String className = LoginDialogResponseHandler.class.getSimpleName();

    public LoginDialogResponseHandler() {
        super(LoginScreen.loginSpinner, ResponseType.POJO);
    }

    @Override
    void updateStatusText(String statusText) {
        LoginScreen.updateLoginStatus(statusText);
    }

    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
        this.updateStatusText("NextStep");
        LoginScreen.acceptLogin();
    }

    @Override
    void nextStep(List<CodenvyResponse> codenvyResponse) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse List" + "::" + "Application Error!!");
    }

    @Override
    void nextStep(ResponseBody responseBody) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }


}
