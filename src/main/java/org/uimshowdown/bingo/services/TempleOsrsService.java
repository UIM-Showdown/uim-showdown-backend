package org.uimshowdown.bingo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TempleOsrsService {
    
    @Autowired
    @Qualifier("templeOsrsClient")
    private RestClient restClient;

    public void updateCompetition() {
        /** @todo Pull all contribution methods, group together PVM KC and SKILLING XP methods */

        /** @todo Pull data from endpoint for skills */

        /** @todo Iterate through participants, map `username` to player */

        /** @todo Iterate through `detailed_gains` and map any contributions based on available contribution methods */

        /** @todo Repeat the steps above but for PVM KC contribution methods */
    }
}
