package in.pathri.codenvydownload.responsehandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.pathri.codenvydownload.client.dao.CodenvyResponse;
import in.pathri.codenvydownload.client.dao.ResponseType;
import in.pathri.codenvydownload.client.dao.WorkspaceDetails;
import in.pathri.codenvydownload.dao.SetupData;
import in.pathri.codenvydownload.dao.SpinnerType;
import in.pathri.codenvydownload.dao.SummaryTextType;
import in.pathri.codenvydownload.screens.SetupScreen;
import in.pathri.codenvydownload.services.MainService;
import okhttp3.ResponseBody;

/**
 * Created by keerthi on 25-12-2016.
 */
public class WorkspaceResponseHandler extends ApiResponseHandler<List<CodenvyResponse>> {
    private static final String className = WorkspaceResponseHandler.class.getSimpleName();

    public WorkspaceResponseHandler() {
        super(SpinnerType.WORKSPACE_REFRESH, ResponseType.ARRAY);
    }

    @Override
    void updateStatusText(String statusText) {
        SetupScreen.SetupFragment.updateSummary(SummaryTextType.WORKSPACE, statusText);
    }

    @Override
    void nextStep(List<CodenvyResponse> codenvyResponse) {
        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        List<CodenvyResponse> workspaceList = codenvyResponse;
        Iterator<CodenvyResponse> iterator = workspaceList.iterator();
        Map<String, CodenvyResponse> workspaceDetailsMap = new HashMap<String, CodenvyResponse>();
        while (iterator.hasNext()) {
            CodenvyResponse workspaceDetails = iterator.next();
            WorkspaceDetails workspaceRef = workspaceDetails.getWorkspaceDetails();
            String name = workspaceRef.name;
            String id = workspaceRef.id;
            workspaceDetailsMap.put(id, workspaceDetails);
            names.add(name);
            ids.add(id);
            SetupData.addWorspaceMap(id, name);
        }
        SetupData.setWorkspaceDetailsMap(workspaceDetailsMap);
        String[] namesArr = names.toArray(new String[names.size()]);
        String[] idsArr = ids.toArray(new String[ids.size()]);
        MainService.updateWorspacePreference(namesArr, idsArr);
    }

    @Override
    void nextStep(CodenvyResponse codenvyResponse) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "CodenvyResponse" + "::" + "Application Error!!");
    }

    @Override
    void nextStep(ResponseBody responseBody) {
        this.updateStatusText(className + "::" + "nextStep" + "::" + "ResponseBody" + "::" + "Application Error!!");
    }

}
