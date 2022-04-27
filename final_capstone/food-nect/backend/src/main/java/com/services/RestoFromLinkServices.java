package com.services;

import com.entities.RestoFromLink;
import com.repository.RestoFromLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestoFromLinkServices {

    @Autowired
    RestoFromLinkRepository restoFromLinkRepository;

    public List<RestoFromLink> findRestosByLinkId(int linkId) {
        return restoFromLinkRepository.findByLinkId(linkId);
    }

    public RestoFromLink findRestoByLinkIdAndId(int linkId, int id) {
        return restoFromLinkRepository.findByLinkIdAndId(linkId, id);
    }

    public RestoFromLink updateRestoFromLink(RestoFromLink restoFromLink) {

        List<RestoFromLink> sameLinkIdlist = findRestosByLinkId(restoFromLink.getLinkId());

        RestoFromLink newRestoFromLink = sameLinkIdlist.get(restoFromLink.getId());

        newRestoFromLink.setId(restoFromLink.getId());
        newRestoFromLink.setThumbsUp(restoFromLink.getThumbsUp());
        newRestoFromLink.setThumbsDown(restoFromLink.getThumbsDown());

        return newRestoFromLink;
    }
}
