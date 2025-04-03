package org.uimshowdown.bingo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "collection_log_completions")
public class CollectionLogCompletion {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private CollectionLogItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "screenshot_url", length = 512)
    private String screenshotUrl;

    public int getId() {
        return id;
    }

    public CollectionLogItem getItem() {
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setItem(CollectionLogItem item) {
        this.item = item;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof CollectionLogCompletion && ((CollectionLogCompletion) obj).getId() == this.id;
    }
    
}
