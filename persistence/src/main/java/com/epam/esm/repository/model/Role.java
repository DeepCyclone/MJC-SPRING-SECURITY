package com.epam.esm.repository.model;

import com.epam.esm.repository.metadata.RoleMetadata;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = RoleMetadata.TABLE_NAME)
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = RoleMetadata.ID)
    private long id;
    @Column(name = RoleMetadata.NAME)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> associatedUsers;

    @Override
    public String getAuthority() {
        return name;
    }
}
