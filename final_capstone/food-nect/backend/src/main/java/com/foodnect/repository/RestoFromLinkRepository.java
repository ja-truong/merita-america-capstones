package com.foodnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.foodnect.entities.RestoFromLink;
import org.springframework.stereotype.Repository;

@Repository
public interface RestoFromLinkRepository extends JpaRepository<RestoFromLink, Integer> {
    List<RestoFromLink> findByLinkId(int linkId);

    RestoFromLink findByLinkIdAndId(int linkId, int id);
}
