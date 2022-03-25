package com.epam.esm.repository.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.epam.esm.repository.metadata.UserMetadata;

import org.hibernate.FetchMode;

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
@Table(name = UserMetadata.TABLE_NAME)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = UserMetadata.ID)
    private long id;
    @Column(name = UserMetadata.NAME)
    private String name;
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    private List<Order> orders;
}
