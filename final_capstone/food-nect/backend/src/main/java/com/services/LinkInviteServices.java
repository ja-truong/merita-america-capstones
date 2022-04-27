package com.services;

import com.entities.LinkInvite;
import com.repository.LinkInviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkInviteServices {
    
    @Autowired
    LinkInviteRepository linkInviteRepository;

    public List<LinkInvite> findLinkInviteByLinkId(String linkId) {
        return linkInviteRepository.findByLinkId(linkId);
    }

    public LinkInvite saveLinkInvite(LinkInvite linkInvite) {
        return linkInviteRepository.save(linkInvite);
    }
}
