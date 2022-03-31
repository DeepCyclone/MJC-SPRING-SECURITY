package com.epam.esm.repository.model;

import com.epam.esm.repository.audit.AuditListener;
import com.epam.esm.repository.metadata.JoinedTablesMetadata;
import com.epam.esm.repository.metadata.OrderMetadata;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditListener.class)
@Table(name = OrderMetadata.TABLE_NAME)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = OrderMetadata.ID)
    private long id;
    @Column(name = OrderMetadata.PRICE)
    private BigDecimal price;
    @Column(name = OrderMetadata.PURCHASE_DATE)
    @CreationTimestamp
    private Timestamp purchaseDate;
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinTable(
        name = JoinedTablesMetadata.ORDER_M2M_CERTIFICATE,
        joinColumns = @JoinColumn(name = "omc_o_id"),
        inverseJoinColumns = @JoinColumn(name = "omc_gc_id")
    )
    private List<GiftCertificate> associatedCertificates;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinTable(
        name = JoinedTablesMetadata.USER_M2M_ORDER,
        joinColumns = @JoinColumn(name = "umo_o_id"),
        inverseJoinColumns = @JoinColumn(name = "umo_u_id")
    )
    private User user;
}
