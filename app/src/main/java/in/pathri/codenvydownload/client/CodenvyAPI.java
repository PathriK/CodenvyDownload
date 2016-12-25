package in.pathri.codenvydownload.client;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.dao.Global;
import in.pathri.codenvydownload.dao.LoginData;
import in.pathri.codenvydownload.utilities.CustomLogger;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by keerthi on 25-12-2016.
 */

public class CodenvyAPI {
    private static final String className = CodenvyAPI.class.getSimpleName();

    private final String BASE_URL = Global.API_BASE_URL;
    private Retrofit retrofit;
    private CodenvyApiService apiService;
    private boolean initialised = false;

    public void apiInit() {
        try {
            CustomLogger.i(className, "apiInit", "Inside Method");
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            // Service setup
            apiService = retrofit.create(CodenvyApiService.class);
            initialised = true;
        } catch (Exception e) {
            CustomLogger.e(className, "apiInit", "Api Init Error", e);
            e.printStackTrace();
        }
    }

    public void postLogin(LoginData loginData, Callback<CodenvyResponse> loginResponseHandler) {

        CustomLogger.d(className, "postLogin", "LoginData", loginData.toString());
        // Prepare the HTTP request
        Call<CodenvyResponse> loginCall = apiService.postWithJson(loginData);

        // Asynchronously execute HTTP request
        loginCall.enqueue(loginResponseHandler);
    }

    public void getWorkspaceDetails(Callback<List<CodenvyResponse>> workspaceResponseHandler) {
        CustomLogger.i(className, "getWorkspaceIdMap", "Into Function");
        Call<List<CodenvyResponse>> workspaceDetailsCall = apiService.getWorkspaceDetails();
        workspaceDetailsCall.enqueue(workspaceResponseHandler);
    }

    public boolean isInitialised() {
        CustomLogger.d(className, "isInitialised", "initialised", initialised ? "true" : "false");
        return initialised;
    }


//    public void buildProj(String workspaceId, String machineId, String project, CommandDetails command,
//                          Callback<ResponseBody> voidResponseHandler) {
//        CustomLogger.d(className, "buildProj", "machineId|project|command", workspaceId + "|" + project + "|" + command.toString());
//        // Prepare the HTTP request
//        Call<ResponseBody> buildCall = apiService.buildProj(workspaceId, machineId, project, command);
//
//        // Asynchronously execute HTTP request
//        buildCall.enqueue(voidResponseHandler);
//    }
//
//    public void getAPK(String machineId, String downloadURL, Callback<ResponseBody> apkDownloadHandler) {
//        CustomLogger.d(className, "getAPK", "machineId|apkPath", machineId + "|" + downloadURL);
//
//        // Prepare the HTTP request
//        Call<ResponseBody> getApkCall = apiService.getAPK(downloadURL);
//
//        // Asynchronously execute HTTP request
//        getApkCall.enqueue(apkDownloadHandler);
//    }
//
//    public void getWorkspaceDetail(String wid, Callback<CodenvyResponse> workspaceStatusHandler) {
//        CustomLogger.d(className, "getWorkspaceDetail", "wid", wid);
//        Call<CodenvyResponse> workspaceDetailCall = apiService.getWorkspaceDetail(wid);
//        workspaceDetailCall.enqueue(workspaceStatusHandler);
//    }
//
//    public void getMachineDetails(String workspaceId, Callback<List<CodenvyResponse>> machineDetailsResponseHandler) {
//        CustomLogger.d(className, "getMachineDetails", "machineId", workspaceId);
//        Call<List<CodenvyResponse>> machineDetailsCall = apiService.getMachineDetails(workspaceId);
//        machineDetailsCall.enqueue(machineDetailsResponseHandler);
//    }
//
//    public void startWorkspace(String wid, Callback<ResponseBody> voidResponseHandler) {
//        CustomLogger.d(className, "startWorkspace", "wid", wid);
//        Call<ResponseBody> startWorkspaceCall = apiService.startWorkspace(wid);
//        startWorkspaceCall.enqueue(voidResponseHandler);
//    }
//
//    public void getArtifactId(String projectURL, String machineToken, Callback<List<CodenvyResponse>> artifactIdResponseHandler) {
//        CustomLogger.d(className, "getArtifactId", "projectURL|machineToken", projectURL + "|" + machineToken);
//
//        String artifactURL = projectURL + "/project?token=" + machineToken;
//        // Prepare the HTTP request
//        Call<List<CodenvyResponse>> getArtifactCall = apiService.getArtifactId(artifactURL);
//
//        // Asynchronously execute HTTP request
//        getArtifactCall.enqueue(artifactIdResponseHandler);
//
//
//    }
//

    public void updateCookie(List<String> cookies) {
        CustomLogger.i(className, "updateCookie", "Update Cookie Called");
        CodenvyWS.updateCookie(cookies);
    }

//    public void setWorkspaceDetailsMap(Map<String, CodenvyResponse> workspaceList) {
//        CustomLogger.i(className, "setWorkspaceDetailsMap", "setWorkspaceDetailsMap called");
//        this.workspaceDetailsMap = workspaceList;
//    }

    //    private Map<String, CodenvyResponse> workspaceDetailsMap;
//
    private interface CodenvyApiService {

        // Login
        @POST("auth/login")
        Call<CodenvyResponse> postWithJson(@Body LoginData loginData);

        // Get Workspace Details
        @GET("workspace")
        Call<List<CodenvyResponse>> getWorkspaceDetails();

//        // Build
//        @POST("workspace/{wid}/machine/{machineId}/command")
//        Call<ResponseBody> buildProj(@Path("wid") String workspaceId, @Path("machineId") String machineId, @Query("outputChannel") String channelGUID, @Body CommandDetails command);
//
//        //Get Token
//        @GET("workspace/{wid}/machine")
//        Call<List<CodenvyResponse>> getMachineDetails(@Path("wid") String workspaceId);
//
//        @GET
//        Call<List<CodenvyResponse>> getArtifactId(@Url String projectURL);
//
//        //Download APK
//        @GET
//        Call<ResponseBody> getAPK(@Url String apkUrl);
//
//
//        // Get Worskspace Detail
//        @GET("workspace/{wid}")
//        Call<CodenvyResponse> getWorkspaceDetail(@Path("wid") String workspaceId);
//
//        // Start Workspace
//        @POST("workspace/{wid}/runtime")
//        Call<ResponseBody> startWorkspace(@Path("wid") String workspaceId);
    }
}
