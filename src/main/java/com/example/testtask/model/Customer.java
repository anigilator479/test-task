package com.example.testtask.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Table(name = "customers")
@SQLDelete(sql = "UPDATE customers SET is_active = false WHERE id = ?")
@Where(clause = "is_active=true")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long created;

    @Column(nullable = false)
    private Long updated;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    public void prePersist() {
        this.created = System.currentTimeMillis();
        this.updated = this.created;
    }

    @PreUpdate
    public void preUpdate() {
        this.updated = System.currentTimeMillis();
    }
}
