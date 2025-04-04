package org.uimshowdown.bingo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;

@RestController
public class ContributionMethodController {
    
    @Autowired
    private ContributionMethodRepository contributionMethodRepository;
    
    @GetMapping("/contributionMethods")
    public Set<ContributionMethod> getContributionMethods() {
        Set<ContributionMethod> contributionMethods = new HashSet<ContributionMethod>();
        for(ContributionMethod contributionMethod : contributionMethodRepository.findAll()) {
            contributionMethods.add(contributionMethod);
        }
        return contributionMethods;
    }

}
