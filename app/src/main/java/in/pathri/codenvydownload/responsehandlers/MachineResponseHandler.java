package in.pathri.codenvydownload.responsehandlers;

import java.util.List;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.ResponseType;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import okhttp3.ResponseBody;

/**
 * Created by keerthi on 15-01-2017.
 */
public class MachineResponseHandler extends ApiResponseHandler<List<CodenvyResponse>> {
    private static final String className = MachineResponseHandler.class.getSimpleName();

    //    CodenvyBetaClientAdapter clientImpl;
    public MachineResponseHandler() {
//        super(HomePageActivity.buildSpinner,CodenvyResponse.class);
//        super.responseType = ResponseType.ARRAY;
//        this.clientImpl = clientImpl;
        super(SpinnerType.BUILD_TRIGGER, ResponseType.ARRAY);
    }


    @Override
    void updateStatusText(String statusText) {
        BuildScreen.updateStatusText(StatusTextType.TRIGGER_STATUS, className + "::" + statusText);
    }

    @Override
    public void nextStep(List<CodenvyResponse> codenvyResponse) {
        final List<CodenvyResponse> currentResponseList = codenvyResponse;
        final CodenvyResponse currentResponse = currentResponseList.get(0);
        String machineToken = currentResponse.getMachineToken();
        String projectURL = currentResponse.getProjectURL();
        AppData.setMachineToken(machineToken);
        AppData.setProjectURL(projectURL);
        CodenvyClient.getArtifactId();
    }

    @Override
    void nextStep(ResponseBody responseBody) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }


    @Override
    public void nextStep(CodenvyResponse codenvyResponses) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse" + "::" + "Application Error!!");
    }

}
