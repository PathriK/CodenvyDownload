package in.pathri.codenvydownload.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.pathri.codenvydownload.R;
import in.pathri.codenvydownload.dao.StatusTextType;
import in.pathri.codenvydownload.services.MainService;
import in.pathri.codenvydownload.utilities.CustomLogger;

public class BuildScreen extends AppCompatActivity {
    private static final String className = BuildScreen.class.getSimpleName();
    public static Context context;
    static TextView loginStatus, buildStatus, statusMsg, downloadStatus, triggerStatus, currentStatus;
    private static ProgressBar loginSpinner, buildSpinner, triggerSpinner, downloadSpinner;

    public static void updateStatusText(StatusTextType statusTextType, String msg) {
        switch (statusTextType) {
            case LOGIN_STATUS:
                loginStatus.setText(msg);
                break;
            case TRIGGER_STATUS:
                triggerStatus.setText(msg);
                break;
            case BUILD_STATUS:
                buildStatus.setText(msg);
                break;
            case DOWNLOAD_STATUS:
                downloadStatus.setText(msg);
                break;
            case STATUS_MSG:
                String temp = statusMsg.getText().toString();
                statusMsg.setText(temp + msg);
                break;
        }
        CustomLogger.i(className, statusTextType.name(), msg);
    }

    public static void updateCurrentState(String msg) {
        currentStatus.setText(msg);
    }

    public static void clearStatusTexts() {
        for (StatusTextType statusText : StatusTextType.values()) {
            updateStatusText(statusText, "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_screen);

        currentStatus = (TextView) findViewById(R.id.current_state);

        loginStatus = (TextView) findViewById(R.id.login_status);
        triggerStatus = (TextView) findViewById(R.id.trigger_status);
        buildStatus = (TextView) findViewById(R.id.build_status);
        downloadStatus = (TextView) findViewById(R.id.download_status);
        statusMsg = (TextView) findViewById(R.id.status_msg);

        loginSpinner = (ProgressBar) findViewById(R.id.login_progress);
        triggerSpinner = (ProgressBar) findViewById(R.id.trigger_progress);
        buildSpinner = (ProgressBar) findViewById(R.id.build_progress);
        downloadSpinner = (ProgressBar) findViewById(R.id.download_progress);

        context = getApplicationContext();

        statusMsg.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setup:
                Intent i = new Intent(this, SetupScreen.class);
                startActivity(i);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomLogger.i(className, "onPause", "into onPause");
        MainService.BuildScreenOnPause(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomLogger.i(className, "onResume", "into onResume");
        MainService.BuildScreenOnResume(context);
    }

    public void onBuild(View view) {
        clearStatusTexts();
        loginStatus.setText("Logging In");
        MainService.doBuildLogin();
    }
}
