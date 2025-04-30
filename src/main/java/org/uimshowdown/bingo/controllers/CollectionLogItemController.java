package org.uimshowdown.bingo.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;

@RestController
public class CollectionLogItemController {
    
    @Autowired
    private CollectionLogItemRepository collectionLogItemRepository;
    
    @GetMapping("/collectionLogItems")
    public Set<CollectionLogItem> getCollectionLogItems() {
        Set<CollectionLogItem> items = new HashSet<CollectionLogItem>();
        for(CollectionLogItem item : collectionLogItemRepository.findAll()) {
            items.add(item);
        }
        return items;
    }

}
