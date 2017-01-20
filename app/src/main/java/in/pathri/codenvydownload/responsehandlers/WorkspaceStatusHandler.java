package in.pathri.codenvydownload.responsehandlers;

import java.util.List;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.ResponseType;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.utilities.CustomLogger;
import okhttp3.ResponseBody;

public class WorkspaceStatusHandler extends ApiResponseHandler<CodenvyResponse> {
    private static final String className = WorkspaceStatusHandler.class.getSimpleName();

    public WorkspaceStatusHandler() {
        super(SpinnerType.BUILD_TRIGGER, ResponseType.POJO);
    }

    @Override
    void updateStatusText(String statusText) {
        BuildScreen.updateStatusText(StatusTextType.TRIGGER_STATUS, className + "::" + statusText);
    }

    @Override
    public void nextStep(CodenvyResponse codenvyResponse) {
        final CodenvyResponse currentResponse = codenvyResponse;
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
    void nextStep(ResponseBody responseBody) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }


    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "List CodenvyResponse" + "::" + "Application Error!!");
    }
}