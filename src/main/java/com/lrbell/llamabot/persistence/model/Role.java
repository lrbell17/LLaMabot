package com.lrbell.llamabot.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
public class Role {

    /**
     * The ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roleId;

    /**
     * The name of the role.
     */
    @Column(nullable = false, unique = true)
    @Setter
    private String roleName;

    /**
     * The permissions associated with the role.
     */
//    @ManyToMany(fetch = FetchType.EAGER)
//    @Column(nullable = false)
//    @Setter
//    private Set<Permission> permissions;

//    /**
//     * Constructor.
//     *
//     * @param roleName
//     * @param permissions
//     */
//    public Role(final String roleName, final Set<Permission> permissions) {
//        this.roleName = roleName;
//        this.permissions = permissions;
//    }

    /**
     * Constructor.
     *
     * @param roleName
     */
    public Role(final String roleName) {
        this.roleName = roleName;
    }

    /**
     * No arg constructor required by JPA.
     */
    public Role() {}

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        final Role role = (Role) o;
        return Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }
}
