package com.foodnect.controller;

import com.foodnect.entities.RestoFromLink;
import com.foodnect.services.RestoFromLinkServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/link")
public class RestoFromLinkController {

    @Autowired
    private RestoFromLinkServices restoFromLinkServices;

    @GetMapping("/search/{linkId}")
    public List<RestoFromLink> findAllRestosByLinkId(@PathVariable int linkId) {
        return restoFromLinkServices.findRestosByLinkId(linkId);
    }

    @GetMapping("/search/{linkId}/{id}")
    public RestoFromLink findRestoByLinkIdAndId(@PathVariable int linkId, @PathVariable int id) {
        return restoFromLinkServices.findRestoByLinkIdAndId(linkId, id);
    }

    @PutMapping("")
    public RestoFromLink updateResto(@RequestBody RestoFromLink restoFromLink) {
        return restoFromLinkServices.updateRestoFromLink(restoFromLink);
    }
}