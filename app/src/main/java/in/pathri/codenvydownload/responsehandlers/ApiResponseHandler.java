package in.pathri.codenvydownload.responsehandlers;

import java.io.IOException;
import java.util.List;

import in.pathri.codenvydownload.client.CodenvyClient;
import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.ResponseType;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.screens.BuildScreen;
import in.pathri.codenvydownload.utilities.Common;
import in.pathri.codenvydownload.utilities.CustomLogger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by keerthi on 25-12-2016.
 */

public abstract class ApiResponseHandler<T> implements Callback<T> {
    final static String START = "start";
    final static String END = "end";
    private static final String className = ApiResponseHandler.class.getSimpleName();
    //    ProgressBar spinner = null;
//    CustomProgressDialog pd = null;
    SpinnerType spinner;
    ResponseType responseType = ResponseType.BINARY;
    boolean retryFlag = false;
    Response response;

    ApiResponseHandler(SpinnerType spinner, ResponseType responseType) {
        this.spinner = spinner;
        this.responseType = responseType;
        updateProgress(START);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        CustomLogger.d(className, "onResponse", "Request URL:", call.request().url().toString());
        // TODO: 25-12-2016
        this.response = response;
        if (response.isSuccessful()) {
            if (200 == response.code()) {
                switch (responseType) {
                    case POJO:
                        CodenvyResponse apiResponse = (CodenvyResponse) response.body();
                        if (apiResponse == null) {
                            this.updateStatusText("Empty Response");
                        } else {
                            this.updateStatusText("Success");
                            this.nextStep(apiResponse);
                        }
                        break;
                    case BINARY:
                        ResponseBody apiResponseBinary = (ResponseBody) response.body();
                        if (apiResponseBinary == null) {
                            this.updateStatusText("Empty Response");
                        } else {
                            this.updateStatusText("Success");
                            this.nextStep(apiResponseBinary);
                        }
                        break;
                    case ARRAY:
                        List<CodenvyResponse> apiResponseArr = (List<CodenvyResponse>) response.body();
                        if (apiResponseArr == null) {
                            this.updateStatusText("Empty Response");
                        } else {
                            this.updateStatusText("Success");
                            this.nextStep(apiResponseArr);
                        }
                        break;
                }
            } else {
                this.updateStatusText("Not Success - " + response.code());
            }
        } else {
            ResponseBody k = response.errorBody();
            if (k == null) {
                this.updateStatusText("Empty Error Response");
            } else {
                try {
                    if (401 == response.code() || 403 == response.code()) {
                        if (!retryFlag) {
                            retryFlag = true;
                            this.handleAuthIssue(k);
                        } else {
                            this.updateStatusText("Error: " + k.string());
                        }
                    } else {
                        this.updateStatusText("Error: " + k.string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        updateProgress(END);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        updateProgress(END);
        this.updateStatusText("Connection Error. Details Below");
        BuildScreen.updateStatusText(StatusTextType.STATUS_MSG, Common.getStackTraceString(t));
    }

    abstract void nextStep(CodenvyResponse codenvyResponse);

    abstract void nextStep(List<CodenvyResponse> codenvyResponse);

    abstract void nextStep(ResponseBody responseBody);

    abstract void updateStatusText(String statusText);

    private void handleAuthIssue(ResponseBody responseBody) {
        try {
            String responseString = responseBody.string();
            CustomLogger.d(className, "handleAuthIssue", "responseString::", responseString);

            if (responseString.contains("User not authorized to call this method")) {
                this.updateStatusText("Session Time Out. Please re-login from Settings");
            } else {
                this.updateStatusText("Unknown Network error. Please try after some time");
            }
        } catch (IOException e) {
            String exception = Common.getStackTraceString(e);
            CustomLogger.d(className, "handleAuthIssue", "ResponseString", exception);
            e.printStackTrace();

        }
    }

    protected void extractCookie() {
        List<String> cookies = response.headers().values("Set-Cookie");
        CodenvyClient.updateCookie(cookies);
    }

    private void updateProgress(String status) {
        if (START.equalsIgnoreCase(status)) {
            CustomLogger.i(className, "updateProgress", "Visible");
            spinner.showSpinner();
        } else {
            CustomLogger.i(className, "updateProgress", "Hide");
            spinner.hideSpinner();
        }
    }
//    private void updateProgress(String status) {
//        if (START.equalsIgnoreCase(status)) {
//            if (spinner != null) {
//                CustomLogger.i(className, "updateProgress", "Visible");
//                this.spinner.setVisibility(View.VISIBLE);
//            }
//            if (pd != null) {
//                pd.show();
//            }
//        } else {
//            if (spinner != null) {
//                this.spinner.setVisibility(View.GONE);
//            }
//            if (pd != null) {
//                pd.dismiss();
//            }
//        }
//    }
}
