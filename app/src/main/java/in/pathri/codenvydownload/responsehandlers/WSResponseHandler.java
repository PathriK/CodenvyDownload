package in.pathri.codenvydownload.responsehandlers;

import in.pathri.codenvydownload.client.dao.CodenvyResponseWS;

/**
 * Created by keerthi on 15-01-2017.
 */

public interface WSResponseHandler {
    void nextStep(CodenvyResponseWS codenvyResponseWS);

    void updateStatusText(String statusText);

    void onConnect();
}
