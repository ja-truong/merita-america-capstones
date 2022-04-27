package com.foodnect.entities;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Table(name="link_tbl")
public class LinkInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="id")
    private int linkId;
    private String sender;
    private String recipient;
    private LocalDate expirationDate;
    private String link;

    @OneToMany
    @JoinColumn(name = "linkId")
    private Set<RestoFromLink> restoFromLinkSet;
}
