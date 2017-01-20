package in.pathri.codenvydownload.responsehandlers;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
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

/**
 * Created by keerthi on 15-01-2017.
 */
public class ApkDownloadHandler extends ApiResponseHandler<ResponseBody> {

    private static final String className = ApkDownloadHandler.class.getSimpleName();

    public ApkDownloadHandler() {
        super(SpinnerType.BUILD_DOWNLOAD, ResponseType.BINARY);
    }

    @Override
    void updateStatusText(String statusText) {
        CustomLogger.d(className, "status", "statusText", statusText);
        BuildScreen.updateStatusText(StatusTextType.DOWNLOAD_STATUS, className + "::" + statusText);
    }

    @Override
    void nextStep(ResponseBody rawResponse) {
        String fileName = AppData.getApkFileName();
        try {
            FileOutputStream fileOutputStream =
                    BuildScreen.context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            IOUtils.write(rawResponse.bytes(), fileOutputStream);
            this.updateStatusText("Downloaded, Installing..");
            String path = BuildScreen.context.getFilesDir().getAbsolutePath() + "/" + fileName;
            this.updateStatusText(path);
            AppData.setApkPath(path);
            CodenvyClient.installAPK();
        } catch (IOException e) {
            this.updateStatusText("Error while writing file! " + e.toString());
        }
    }

    @Override
    public void nextStep(CodenvyResponse codenvyResponses) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse" + "::" + "Application Error!!");
    }


    @Override
    void nextStep(List<CodenvyResponse> codenvyResponses) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "List CodenvyResponse" + "::" + "Application Error!!");
    }


}
