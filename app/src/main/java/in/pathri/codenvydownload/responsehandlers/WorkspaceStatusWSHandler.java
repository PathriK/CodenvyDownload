package in.pathri.codenvydownload.responsehandlers;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.CodenvyResponseWS;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.utilities.CustomLogger;
import in.pathri.codenvydownload.utilities.WSResponseBody;

public class WorkspaceStatusWSHandler implements WSResponseHandler {
    private static final String className = WorkspaceStatusWSHandler.class.getSimpleName();

    @Override
    public void updateStatusText(String statusText) {
        BuildScreen.updateStatusText(StatusTextType.TRIGGER_STATUS, className + "::" + statusText);
    }


    @Override
    public void nextStep(CodenvyResponseWS codenvyResponse) {
        final CodenvyResponseWS currentResponse = WSResponseBody.extract(codenvyResponse);
        final String respStatus = currentResponse.getStatus();
        if ("STOPPED".equals(respStatus)) {
            CustomLogger.d(className, "nextStep", "StoppedData", currentResponse.toString());
            CodenvyClient.readyWorkspaceStatusHandler();
            this.updateStatusText(respStatus);
        } else if ("CREATING".equals(respStatus)) {
            CustomLogger.d(className, "nextStep", "CreatingData", currentResponse.toString());
            this.updateStatusText(respStatus);
        } else if ("RUNNING".equals(respStatus)) {
            CustomLogger.d(className, "nextStep", "RunningData", currentResponse.toString());
            CustomLogger.d(className, "nextStep", "MachineId", currentResponse.getMachineId());
            AppData.setMachineId(currentResponse.getMachineId());
            CodenvyClient.readyBuild();
        } else {
            this.updateStatusText("Build Status Unknown" + respStatus);
        }
    }


    @Override
    public void onConnect() {
        CustomLogger.i(className, "onConnect", "into method");
        CodenvyClient.startWorkspace();
    }

}