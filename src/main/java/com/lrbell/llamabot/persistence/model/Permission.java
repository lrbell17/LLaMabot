package com.lrbell.llamabot.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Permission {

    /**
     * The ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String permissionId;

    /**
     * The name of the permission.
     */
    @Column(nullable = false, unique = true)
    @Setter
    private String permissionName;

    /**
     * Constructor.
     *
     * @param permissionName
     */
    public Permission(final String permissionName) {
        this.permissionName = permissionName;
    }

    /**
     * No arg constructor required by JPA.
     */
    public Permission() {}
}
