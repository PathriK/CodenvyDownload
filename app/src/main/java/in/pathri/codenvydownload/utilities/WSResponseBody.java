package in.pathri.codenvydownload.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.pathri.codenvydownload.client.dao.Body;
import in.pathri.codenvydownload.client.dao.CodenvyResponseWS;

/**
 * Created by keerthi on 16-01-2017.
 */

public class WSResponseBody {
    public static CodenvyResponseWS extract(CodenvyResponseWS codenvyResponseWS) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(retrofit2.http.Body.class, new wsBodyDeserializer());
        Gson gson = builder.create();

        Body body = gson.fromJson(codenvyResponseWS.getMessage(), Body.class);

        return codenvyResponseWS.setMessageObject(body);
    }
}
