package in.pathri.codenvydownload.client;

import java.util.List;

import in.pathri.codenvydownload.dao.AppData;
import in.pathri.codenvydownload.responsehandlers.LoginDialogResponseHandler;
import in.pathri.codenvydownload.responsehandlers.WorkspaceResponseHandler;
import in.pathri.codenvydownload.screens.SetupScreen;
import in.pathri.codenvydownload.utilities.CustomLogger;

/**
 * Created by keerthi on 24-12-2016.
 */

public class CodenvyClient {
    private static final String className = CodenvyClient.class.getSimpleName();
    //    public boolean isBuildStatusHandlerReady = false;
//    public boolean isBuildOutputHandlerReady = false;
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
        codenvyAPI.getWorkspaceDetails(new WorkspaceResponseHandler(SetupScreen.workspaceProgressDialog));
    }
    //    public static void postBuildLogin() {
//        CustomLogger.i(className, "postBuildLogin", "Into Function");
//        codenvyAPI.postLogin(AppData.getLoginData(),new LoginResponseHandler());
//    }
//
//    public static void postRefreshLogin() {
//        CustomLogger.i(className, "postRefreshLogin", "Into Function");
//        codenvyAPI.postLogin(AppData.getLoginData(), new LoginRefreshHandler(SetupScreen.workspaceProgressDialog));
//    }
//
//    public static void buildProj() {
//        CustomLogger.i(className, "buildProj", "Into Function");
//        codenvyAPI.getWorkspaceDetail(AppData.getWorkspaceId(), new WorkspaceStatusHandler(AppData.getWorkspaceId()));
//    }
//    public static void getAPK() {
//        CustomLogger.i(className, "getAPK", "Into Function");
//        codenvyAPI.getAPK(AppData.getMachineId(), AppData.getApkUrl(), new ApkDownloadHandler());
//    }

    public static boolean isInitialised() {
        return codenvyAPI.isInitialised();
    }

    //    public static void setWorkspaceDetailsMap(Map< String, CodenvyResponse> workspaceList){
//        codenvyAPI.setWorkspaceDetailsMap(workspaceList);
//    }
//
//
//    public static void installAPK(){
//       MainService.installAPK(AppData.getApkPath());
//    }
    public static void updateCookie(List<String> cookies) {
        codenvyAPI.updateCookie(cookies);
    }
}
