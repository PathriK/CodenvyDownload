package in.pathri.codenvydownload.responsehandlers;

import java.util.List;

import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.ResponseType;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import okhttp3.ResponseBody;

/**
 * Created by keerthi on 15-01-2017.
 */
public class VoidResponseHandler extends ApiResponseHandler<ResponseBody> {
    private static final String className = VoidResponseHandler.class.getSimpleName();

    public VoidResponseHandler() {
        super(SpinnerType.BUILD_LOGIN, ResponseType.BINARY);
    }


    @Override
    void updateStatusText(String statusText) {
        BuildScreen.updateStatusText(StatusTextType.STATUS_MSG, statusText);
    }

    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
//        this.updateStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse" + "::" + "Application Error!!");
    }

    @Override
    void nextStep(List<CodenvyResponse> codenvyResponse) {
//        this.updateStatusText(className + "::" + "nextStep" + "::" + "List CodenvyResponse" + "::" + "Application Error!!");
    }

    @Override
    void nextStep(ResponseBody responseBody) {
//        this.updateStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }

}
