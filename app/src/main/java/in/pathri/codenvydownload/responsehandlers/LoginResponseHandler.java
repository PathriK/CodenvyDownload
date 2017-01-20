package in.pathri.codenvydownload.responsehandlers;

import java.util.List;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.ResponseType;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import okhttp3.ResponseBody;

/**
 * Created by keerthi on 08-01-2017.
 */

public class LoginResponseHandler extends ApiResponseHandler<CodenvyResponse> {
    private static final String className = LoginResponseHandler.class.getSimpleName();

    public LoginResponseHandler() {
        super(SpinnerType.BUILD_LOGIN, ResponseType.POJO);
    }

    @Override
    void updateStatusText(String statusText) {
        BuildScreen.updateStatusText(StatusTextType.LOGIN_STATUS, statusText);
    }

    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
        this.extractCookie();
        CodenvyClient.buildProj();
    }

    @Override
    void nextStep(List<CodenvyResponse> codenvyResponse) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "List CodenvyResponse" + "::" + "Application Error!!");
    }

    @Override
    void nextStep(ResponseBody responseBody) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }

}
