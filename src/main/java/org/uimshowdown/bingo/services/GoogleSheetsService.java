package org.uimshowdown.bingo.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.repositories.TeamRepository;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.DeleteSheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Component
public class GoogleSheetsService {
    
    @Value("${google.outputSheetID}")
    private String sheetID;
    
    @Autowired TeamRepository teamRepository;
    
    private GoogleCredentials getCredentials() throws Exception {
        InputStream in = GoogleSheetsService.class.getResourceAsStream("/google-creds.json");
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        return ServiceAccountCredentials.fromStream(in).createScoped(scopes);
    }
    
    private Sheets getSheetsService() throws Exception {
        GoogleCredentials creds = getCredentials();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(creds)).setApplicationName("UIM Showdown Backend").build();
    }
    
    public void initializeTabs() throws Exception {
        Sheets service = getSheetsService();
        BatchUpdateSpreadsheetRequest tabsRequest = new BatchUpdateSpreadsheetRequest();
        List<Request> requests = new ArrayList<Request>();
        Spreadsheet spreadsheet = service.spreadsheets().get(sheetID).execute();
        
        // Delete all the tabs except the first one, which we can't delete (and we will later rename)
        for(Sheet tab : spreadsheet.getSheets()) {
            if(tab.getProperties().getTitle().startsWith("unf_")) {
                requests.add(new Request().setDeleteSheet(new DeleteSheetRequest().setSheetId(tab.getProperties().getSheetId())));
            }
        }
        
        // Set up the general tabs
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_LastUpdated").setIndex(0))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_StandingsMainLeaderboard").setIndex(1))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_StandingsRecordLeaderboard").setIndex(2))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_StandingsChallengeLeaderboard").setIndex(3))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_StandingsMVPLeaderboard").setIndex(4))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_MVPDetails").setIndex(5))));
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_MVPExtras").setIndex(6))));
        
        // Set up the team tabs
        int index = 7;
        for(Team team : teamRepository.findAll()) {
            String abbr = team.getAbbreviation();
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_BoardDetails" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_RecordDetails" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_ChallengeDetails" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_MVPRace" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_RelayComponents" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_ProgressOverview" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_Progress" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_ClogChecklists" + abbr).setIndex(index++))));
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle("unf_ClogCounters" + abbr).setIndex(index++))));
        }
        
        tabsRequest.setRequests(requests);
        service.spreadsheets().batchUpdate(sheetID, tabsRequest).execute();
    }
    
    public ValueRange createUpdateRequest(String range, List<List<Object>> data) {
        return new ValueRange().setRange(range).setValues(data);
    }
    
    public void executeSingleUpdate(String range, List<List<Object>> data) throws Exception {
        Sheets service = getSheetsService();
        ValueRange request = createUpdateRequest(range, data);
        service.spreadsheets().values().update(sheetID, request.getRange(), request).setValueInputOption("RAW").execute();
    }
    
    public void executeBatchUpdate(List<ValueRange> requests) throws Exception {
        Sheets service = getSheetsService();
        service.spreadsheets().values().batchUpdate(sheetID, new BatchUpdateValuesRequest().setData(requests).setValueInputOption("RAW")).execute();
    }

}
