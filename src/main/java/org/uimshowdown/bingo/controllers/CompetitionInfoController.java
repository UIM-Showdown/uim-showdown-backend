package org.uimshowdown.bingo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;

@RestController
public class CompetitionInfoController {
    
    @Autowired CompetitionConfiguration competitionConfiguration;
    
    @GetMapping("/competitionInfo")
    public Map<String, Object> getCompetitionInfo() {
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("eventName", competitionConfiguration.getEventName());
        info.put("startDatetime", competitionConfiguration.getStartDatetime());
        info.put("endDatetime", competitionConfiguration.getEndDatetime());
        return info;
    }

}
