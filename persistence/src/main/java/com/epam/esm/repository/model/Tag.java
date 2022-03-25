package com.epam.esm.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.epam.esm.repository.metadata.TagMetadata;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = TagMetadata.TABLE_NAME)
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TagMetadata.ID)
    private long id;
    @Column(name = TagMetadata.NAME,unique = true)
    private String name;
    @ToString.Exclude
    @ManyToMany(mappedBy = "associatedTags",fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private List<GiftCertificate> certs = new ArrayList<>();
}
