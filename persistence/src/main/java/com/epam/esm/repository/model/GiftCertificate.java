package com.epam.esm.repository.model;

import com.epam.esm.repository.audit.AuditListener;
import com.epam.esm.repository.metadata.GiftCertificateMetadata;
import com.epam.esm.repository.metadata.JoinedTablesMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditListener.class)
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
    @CreationTimestamp
    private Timestamp createDate;
    @Column(name = GiftCertificateMetadata.LAST_UPDATE_DATE)
    @CreationTimestamp
    private Timestamp lastUpdateDate;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinTable(
        name = JoinedTablesMetadata.TAG_M2M_CERTIFICATE,
        joinColumns = @JoinColumn(name = "tmgc_gc_id"),
        inverseJoinColumns = @JoinColumn(name = "tmgc_t_id")
    )
    private List<Tag> associatedTags = new ArrayList<>();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY,mappedBy = "associatedCertificates")
    private List<Order> associatedOrders = new ArrayList<>();
}
