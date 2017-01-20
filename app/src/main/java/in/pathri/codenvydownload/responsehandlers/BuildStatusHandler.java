package in.pathri.codenvydownload.responsehandlers;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.BuildStatus;
import in.pathri.codenvydownload.client.dao.CodenvyResponseWS;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.utilities.CustomLogger;
import in.pathri.codenvydownload.utilities.WSResponseBody;

/**
 * Created by keerthi on 15-01-2017.
 */
public class BuildStatusHandler implements WSResponseHandler {
    private static final String className = BuildStatusHandler.class.getSimpleName();

    @Override
    public void updateStatusText(String statusText) {
        BuildScreen.updateStatusText(StatusTextType.BUILD_STATUS, className + "::" + statusText);
    }

    @Override
    public void nextStep(CodenvyResponseWS codenvyResponse) {
        final CodenvyResponseWS currentResponse = WSResponseBody.extract(codenvyResponse);
        final String respStatus = currentResponse.getStatus();
        if ("STOPPED".equals(respStatus)) {
            CustomLogger.d(className, "nextStep", "respStatus", respStatus);
            AppData.setBuildStatus(BuildStatus.COMPLETED);
            CodenvyClient.checkCompletion();
            this.updateStatusText(respStatus);
        } else if ("STARTED".equals(respStatus)) {
            CustomLogger.d(className, "nextStep", "respStatus", respStatus);
            AppData.setBuildStatus(BuildStatus.STARTED);
            this.updateStatusText(respStatus);
        } else {
            CustomLogger.d(className, "nextStep", "respStatus::Unknown", respStatus);
            this.updateStatusText("Build Status Unknown" + respStatus);
        }
    }

    @Override
    public void onConnect() {
        CustomLogger.i(className, "onConnect", "inside Function");
        CodenvyClient.isBuildStatusHandlerReady = true;
        CodenvyClient.triggerBuild();
    }
}
