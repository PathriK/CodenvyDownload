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
public class ArtifactIdResponseHandler extends ApiResponseHandler<List<CodenvyResponse>> {
    private static final String className = ArtifactIdResponseHandler.class.getSimpleName();

    //    CodenvyBetaClientAdapter clientImpl;
    public ArtifactIdResponseHandler() {
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
    void nextStep(List<CodenvyResponse> codenvyResponse) {
        final List<CodenvyResponse> currentResponse = codenvyResponse;
        CodenvyResponse currentMachine = currentResponse.get(0);
        AppData.setArtifactId(currentMachine.getAttribute("maven.artifactId"));
        AppData.setArtifactExt(currentMachine.getAttribute("maven.packaging"));
        CodenvyClient.checkCompletion();
    }

    @Override
    void nextStep(ResponseBody responseBody) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }


    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse" + "::" + "Application Error!!");
    }


}
