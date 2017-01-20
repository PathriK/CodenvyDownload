package in.pathri.codenvydownload.client;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.CommandDetails;
import in.pathri.codenvydownload.dao.Global;
import in.pathri.codenvydownload.dao.LoginData;
import in.pathri.codenvydownload.utilities.CustomLogger;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

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

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager))
                    .addInterceptor(interceptor).build();

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
        Call<CodenvyResponse> loginCall = apiService.postWithJson(loginData);
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


    public void getWorkspaceDetail(String wid, Callback<CodenvyResponse> workspaceStatusHandler) {
        CustomLogger.d(className, "getWorkspaceDetail", "wid", wid);
        Call<CodenvyResponse> workspaceDetailCall = apiService.getWorkspaceDetail(wid);
        workspaceDetailCall.enqueue(workspaceStatusHandler);
    }


    public void getMachineDetails(String workspaceId, Callback<List<CodenvyResponse>> machineDetailsResponseHandler) {
        CustomLogger.d(className, "getMachineDetails", "machineId", workspaceId);
        Call<List<CodenvyResponse>> machineDetailsCall = apiService.getMachineDetails(workspaceId);
        machineDetailsCall.enqueue(machineDetailsResponseHandler);
    }

    public void getArtifactId(String projectURL, String machineToken, Callback<List<CodenvyResponse>> artifactIdResponseHandler) {
        CustomLogger.d(className, "getArtifactId", "projectURL|machineToken", projectURL + "|" + machineToken);

        String artifactURL = projectURL + "/project?token=" + machineToken;
        Call<List<CodenvyResponse>> getArtifactCall = apiService.getArtifactId(artifactURL);
        getArtifactCall.enqueue(artifactIdResponseHandler);


    }


    public void buildProj(String workspaceId, String machineId, String project, CommandDetails command,
                          Callback<ResponseBody> voidResponseHandler) {
        CustomLogger.d(className, "buildProj", "machineId|project|command", workspaceId + "|" + project + "|" + command.toString());
        Call<ResponseBody> buildCall = apiService.buildProj(workspaceId, machineId, project, command);
        buildCall.enqueue(voidResponseHandler);
    }

    public void getAPK(String machineId, String downloadURL, Callback<ResponseBody> apkDownloadHandler) {
        CustomLogger.d(className, "getAPK", "machineId|apkPath", machineId + "|" + downloadURL);
        Call<ResponseBody> getApkCall = apiService.getAPK(downloadURL);
        getApkCall.enqueue(apkDownloadHandler);
    }

    public void startWorkspace(String wid, Callback<ResponseBody> voidResponseHandler) {
        CustomLogger.d(className, "startWorkspace", "wid", wid);
        Call<ResponseBody> startWorkspaceCall = apiService.startWorkspace(wid);
        startWorkspaceCall.enqueue(voidResponseHandler);
    }

    public void updateCookie(List<String> cookies) {
        CustomLogger.i(className, "updateCookie", "Update Cookie Called");
        CodenvyWSClient.updateCookie(cookies);
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

        // Get Worskspace Detail
        @GET("workspace/{wid}")
        Call<CodenvyResponse> getWorkspaceDetail(@Path("wid") String workspaceId);

        //Get Token
        @GET("workspace/{wid}/machine")
        Call<List<CodenvyResponse>> getMachineDetails(@Path("wid") String workspaceId);

        @GET
        Call<List<CodenvyResponse>> getArtifactId(@Url String projectURL);

        // Build
        @POST("workspace/{wid}/machine/{machineId}/command")
        Call<ResponseBody> buildProj(@Path("wid") String workspaceId, @Path("machineId") String machineId, @Query("outputChannel") String channelGUID, @Body CommandDetails command);


        //Download APK
        @GET
        Call<ResponseBody> getAPK(@Url String apkUrl);


        // Start Workspace
        @POST("workspace/{wid}/runtime")
        Call<ResponseBody> startWorkspace(@Path("wid") String workspaceId);
    }
}
