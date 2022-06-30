package com.epam.esm.repository.model;

import com.epam.esm.repository.audit.AuditListener;
import com.epam.esm.repository.metadata.UserMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditListener.class)
@Table(name = UserMetadata.TABLE_NAME)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = UserMetadata.ID)
    private long id;
    @Column(name = UserMetadata.NAME)
    private String name;
    @Column(name = UserMetadata.PASSWORD)
    private String password;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY,mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
}
