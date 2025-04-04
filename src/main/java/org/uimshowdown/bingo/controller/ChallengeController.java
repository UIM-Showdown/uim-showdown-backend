package org.uimshowdown.bingo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.repositories.ChallengeRepository;

@RestController
public class ChallengeController {
	
	@Autowired
	private ChallengeRepository challengeRepository;
	
	@GetMapping("/challenges")
	public Set<Challenge> getChallenges() {
		Set<Challenge> challenges = new HashSet<Challenge>();
		for(Challenge challenge : challengeRepository.findAll()) {
			challenges.add(challenge);
		}
		return challenges;
	}

}
