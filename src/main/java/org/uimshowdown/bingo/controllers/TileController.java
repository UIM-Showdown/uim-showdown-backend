package org.uimshowdown.bingo.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.repositories.TileRepository;

@RestController
public class TileController {
    
    @Autowired
    private TileRepository tileRepository;
    
    @GetMapping("/tiles")
    public Set<Tile> getContributionMethods() {
        Set<Tile> tiles = new HashSet<Tile>();
        for(Tile tile : tileRepository.findAll()) {
            tiles.add(tile);
        }
        return tiles;
    }

}
