package in.pathri.codenvydownload.responsehandlers;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.BuildResult;
import in.pathri.codenvydownload.client.dao.CodenvyResponseWS;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 15-01-2017.
 */
public class BuildOutputHandler implements WSResponseHandler {
    private static final String className = BuildOutputHandler.class.getSimpleName();

    @Override
    public void nextStep(CodenvyResponseWS codenvyResponse) {
        final CodenvyResponseWS currentResponse = codenvyResponse;
        String outputMsg = currentResponse.getMessage();
        CustomLogger.d(className, "nextStep", "outputMsg", outputMsg);
        AppData.addBuildOutput(outputMsg);
        checkMsgStatus(outputMsg);
    }

    @Override
    public void updateStatusText(String statusText) {
        BuildScreen.updateStatusText(StatusTextType.STATUS_MSG, className + "::" + statusText);
    }

    private void checkMsgStatus(String msg) {
        boolean buildSuccess = false;
        boolean doCheck = false;
        if (msg.contains("[ERROR]")) {
            buildSuccess = false;
            doCheck = true;
        } else if (msg.contains("[INFO] BUILD FAILURE")) {
            buildSuccess = false;
            doCheck = true;
        } else if (msg.contains("[INFO] BUILD SUCCESS")) {
            buildSuccess = true;
            doCheck = true;
        }

        if (doCheck) {
            if (buildSuccess) {
                AppData.setBuildResult(BuildResult.SUCCESS);
                CodenvyClient.checkCompletion();
            } else {
                AppData.setBuildResult(BuildResult.FAILED);
                CodenvyClient.checkCompletion();
            }
        }
    }

    @Override
    public void onConnect() {
        CustomLogger.i(className, "onConnect", "inside Function");
        CodenvyClient.isBuildOutputHandlerReady = true;
        CodenvyClient.triggerBuild();
    }

}
