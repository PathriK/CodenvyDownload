package in.pathri.codenvydownload.client;

import java.util.List;

import in.pathri.codenvydownload.client.dao.BuildResult;
import in.pathri.codenvydownload.client.dao.BuildStatus;
import in.pathri.codenvydownload.client.dao.Channels;
import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.responsehandlers.ApkDownloadHandler;
import in.pathri.codenvydownload.responsehandlers.ArtifactIdResponseHandler;
import in.pathri.codenvydownload.responsehandlers.BuildOutputHandler;
import in.pathri.codenvydownload.responsehandlers.BuildStatusHandler;
import in.pathri.codenvydownload.responsehandlers.LoginDialogResponseHandler;
import in.pathri.codenvydownload.responsehandlers.LoginResponseHandler;
import in.pathri.codenvydownload.responsehandlers.MachineResponseHandler;
import in.pathri.codenvydownload.responsehandlers.VoidResponseHandler;
import in.pathri.codenvydownload.responsehandlers.WorkspaceResponseHandler;
import in.pathri.codenvydownload.responsehandlers.WorkspaceStatusHandler;
import in.pathri.codenvydownload.responsehandlers.WorkspaceStatusWSHandler;
import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.services.MainService;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 24-12-2016.
 */

public class CodenvyClient {
    private static final String className = CodenvyClient.class.getSimpleName();
    public static boolean isBuildStatusHandlerReady = false;
    public static boolean isBuildOutputHandlerReady = false;
    private static CodenvyAPI codenvyAPI;

    static {
        CustomLogger.i(className, "static", "Into Function");
        codenvyAPI = new CodenvyAPI();
        codenvyAPI.apiInit();
    }

    public static void postSettingsLogin() {
        CustomLogger.i(className, "postSettingsLogin", "Into Function");
        codenvyAPI.postLogin(AppData.getLoginData(), new LoginDialogResponseHandler());
    }

    public static void getWorkspaceDetails() {
        CustomLogger.i(className, "getWorkspaceIdMap", "Into Function");
        codenvyAPI.getWorkspaceDetails(new WorkspaceResponseHandler());
    }

    public static void postBuildLogin() {
        CustomLogger.i(className, "postBuildLogin", "Into Function");
        codenvyAPI.postLogin(AppData.getLoginData(), new LoginResponseHandler());
    }

    public static void buildProj() {
        CustomLogger.i(className, "buildProj", "Into Function");
        codenvyAPI.getWorkspaceDetail(AppData.getWorkspaceId(), new WorkspaceStatusHandler());
    }

    public static void readyWorkspaceStatusHandler() {
        CustomLogger.i(className, "readyWorkspaceStatusHandler", "Inside readyWorkspaceStatusHandler");
        CodenvyWSClient.getInstance(AppData.getWorkspaceId(), Channels.WORKSPACE_STATUS).initChannel(AppData.getWorkspaceId(), new WorkspaceStatusWSHandler());
    }

    public static void startWorkspace() {
        CustomLogger.i(className, "startWorkspace", "Inside startWorkspace");
        codenvyAPI.startWorkspace(AppData.getWorkspaceId(), new VoidResponseHandler());
    }

    public static void readyBuild() {
        try {
            Thread.sleep(3000);
            getMachineTokenAndProjectURL();
            readyBuildHandler();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getMachineTokenAndProjectURL() {
        codenvyAPI.getMachineDetails(AppData.getWorkspaceId(), new MachineResponseHandler());
    }

    public static void getArtifactId() {
        String projectURL = AppData.getProjectURL();
        String machineToken = AppData.getMachineToken();
        codenvyAPI.getArtifactId(projectURL, machineToken, new ArtifactIdResponseHandler());
    }

    private static void readyBuildHandler() {
        CustomLogger.i(className, "triggerBuild", "Inside Trigger Build");
        AppData.generateGUID();
        isBuildOutputHandlerReady = isBuildStatusHandlerReady = false;
        CodenvyWSClient.getInstance(AppData.getWorkspaceId(), Channels.PROCESS_OUTPUT).initChannel(AppData.getGUID(), new BuildOutputHandler());
        CodenvyWSClient.getInstance(AppData.getWorkspaceId(), Channels.PROCESS_STATUS).initChannel(AppData.getMachineId(), new BuildStatusHandler());
    }

    synchronized public static void checkCompletion() {
        CustomLogger.d(className, "checkCompletion", "BuildStatus", AppData.getBuildStatus().name());
        CustomLogger.d(className, "checkCompletion", "BuildResult", AppData.getBuildResult().name());
        if (BuildStatus.COMPLETED.equals(AppData.getBuildStatus())) {
            if (BuildResult.SUCCESS.equals(AppData.getBuildResult())) {
                CustomLogger.i(className, "checkCompletion", "Inside Succcess");
                String artifactID = AppData.getArtifactId();
                String projectURL = AppData.getProjectURL();
                String machineToken = AppData.getMachineToken();
                if (artifactID != "" && projectURL != "" & machineToken != "") {
                    CustomLogger.d(className, "checkCompletion", "Setting APK URL::", artifactID + ":" + projectURL + ":" + machineToken);
                    AppData.setApkUrl(AppData.getProjectURL() + "/project/export/file/" + AppData.getProject() + "/target/" + AppData.getArtifactId() + "." + AppData.getArtifactExt() + "?token=" + AppData.getMachineToken());
                    CodenvyClient.getAPK();
                } else {
                    CustomLogger.d(className, "checkCompletion", "Error Setting APK URL::", artifactID + ":" + projectURL + ":" + machineToken);
                }
            } else if (BuildResult.FAILED.equals(AppData.getBuildResult())) {
                BuildScreen.updateStatusText(StatusTextType.STATUS_MSG, className + ":checkCompletion::" + AppData.getBuildOutput());
            }
        }
    }

    public synchronized static void triggerBuild() {
        CustomLogger.d(className, "triggerBuild", "isBuildStatusHandlerReady|isBuildOutputHandlerReady", isBuildStatusHandlerReady + "|" + isBuildOutputHandlerReady);
        if (isBuildStatusHandlerReady && isBuildOutputHandlerReady) {
            codenvyAPI.buildProj(AppData.getWorkspaceId(), AppData.getMachineId(), Channels.PROCESS_OUTPUT.getChannel(AppData.getGUID()), AppData.getCommand(), new VoidResponseHandler());
        }
    }

    public static void getAPK() {
        CustomLogger.i(className, "getAPK", "Into Function");
        codenvyAPI.getAPK(AppData.getMachineId(), AppData.getApkUrl(), new ApkDownloadHandler());
    }

//    public static void postRefreshLogin() {
//        CustomLogger.i(className, "postRefreshLogin", "Into Function");
//        codenvyAPI.postLogin(AppData.getLoginData(), new LoginRefreshHandler(SetupScreen.workspaceProgressDialog));
//    }
//

    public static void installAPK() {
        MainService.installAPK(AppData.getApkPath());
    }
    public static boolean isInitialised() {
        return codenvyAPI.isInitialised();
    }

    //    public static void setWorkspaceDetailsMap(Map< String, CodenvyResponse> workspaceList){
//        codenvyAPI.setWorkspaceDetailsMap(workspaceList);
//    }
//
//
    public static void updateCookie(List<String> cookies) {
        codenvyAPI.updateCookie(cookies);
    }

}
