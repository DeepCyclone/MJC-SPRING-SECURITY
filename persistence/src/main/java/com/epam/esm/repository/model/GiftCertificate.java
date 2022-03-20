package com.epam.esm.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.epam.esm.repository.metadata.GiftCertificateMetadata;
import com.epam.esm.repository.metadata.JoinedTablesMetadata;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = GiftCertificateMetadata.TABLE_NAME)
public class GiftCertificate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GiftCertificateMetadata.ID)
    private long id;
    @Column(name = GiftCertificateMetadata.NAME)
    private String name;
    @Column(name = GiftCertificateMetadata.DESCRIPTION)
    private String description;
    @Column(name = GiftCertificateMetadata.PRICE)
    private BigDecimal price;
    @Column(name = GiftCertificateMetadata.DURATION)
    private Integer duration;
    @Column(name = GiftCertificateMetadata.CREATE_DATE)
    private Timestamp createDate;
    @Column(name = GiftCertificateMetadata.LAST_UPDATE_DATE)
    private Timestamp lastUpdateDate;
    @ManyToMany
    @JoinTable(
        name = JoinedTablesMetadata.TAG_M2M_CERTIFICATE,
        joinColumns = @JoinColumn(name = "tmgc_gc_id"),
        inverseJoinColumns = @JoinColumn(name = "tmgc_t_id")
    )
    List<Tag> associatedTags;
}
