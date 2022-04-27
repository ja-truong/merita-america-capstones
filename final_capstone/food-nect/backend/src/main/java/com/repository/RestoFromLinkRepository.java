package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.entities.RestoFromLink;

public interface RestoFromLinkRepository extends JpaRepository<RestoFromLink, Integer> {
    List<RestoFromLink> findByLinkId(int linkId);

    RestoFromLink findByLinkIdAndId(int linkId, int id);
}
