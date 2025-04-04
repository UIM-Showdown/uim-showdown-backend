package org.uimshowdown.bingo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.models.CollectionLogGroup;
import org.uimshowdown.bingo.repositories.CollectionLogGroupRepository;

@RestController
public class CollectionLogGroupController {
	
	@Autowired
	private CollectionLogGroupRepository collectionLogGroupRepository;
	
	@GetMapping("/collectionLogGroups")
	public Set<CollectionLogGroup> getCollectionLogGroups() {
		Set<CollectionLogGroup> groups = new HashSet<CollectionLogGroup>();
		for(CollectionLogGroup group : collectionLogGroupRepository.findAll()) {
			groups.add(group);
		}
		return groups;
	}

}
