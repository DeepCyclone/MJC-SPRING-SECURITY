package com.epam.esm.repository.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.epam.esm.repository.metadata.JoinedTablesMetadata;
import com.epam.esm.repository.metadata.OrderMetadata;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
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
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
        name = JoinedTablesMetadata.ORDER_M2M_CERTIFICATE,
        joinColumns = @JoinColumn(name = "omc_o_id"),
        inverseJoinColumns = @JoinColumn(name = "omc_gc_id")
    )
    private List<GiftCertificate> certificates;
    @ToString.Exclude
    @ManyToOne
    @JoinTable(
        name = JoinedTablesMetadata.USER_M2M_ORDER,
        joinColumns = @JoinColumn(name = "umo_o_id"),
        inverseJoinColumns = @JoinColumn(name = "umo_u_id")
    )
    private User user;
}
