package com.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="voting_tbl")
public class RestoFromLink {

    private int linkId; //link_tbl id
    private int id; //restaurant_tbl id
    private int thumbsUp;
    private int thumbsDown;

    public RestoFromLink(int linkId, int id, int thumbsUp, int thumbsDown) {
        this.linkId = linkId;
        this.id = id;
        this.thumbsUp = thumbsUp;
        this.thumbsDown = thumbsDown;
    }

    public RestoFromLink() {
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }
}
