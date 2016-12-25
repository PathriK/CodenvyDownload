package in.pathri.codenvydownload.screens;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by keerthi on 24-12-2016.
 */

public class CustomProgressDialog {
    ProgressDialog pd;

    public CustomProgressDialog(Context pdContext, String title, String message) {
        this.pd = new ProgressDialog(pdContext);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
    }

    public void show() {
        this.pd.show();
    }

    public void dismiss() {
        this.pd.dismiss();
    }
}
