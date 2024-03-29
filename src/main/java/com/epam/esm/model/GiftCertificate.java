package com.epam.esm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.*;
import org.hibernate.envers.Audited;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Audited
@Table(name = "certificates")
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long id;

    @Column(name = "certificate_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "create_date", updatable = false)
    private String createDate;

    @Column(name = "last_update_date")
    private String lastUpdateDate;
    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .format(LocalDateTime.now(ZoneOffset.UTC));
        }
        this.lastUpdateDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .format(LocalDateTime.now(ZoneOffset.UTC));
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdateDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .format(LocalDateTime.now(ZoneOffset.UTC));
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "gift_certificate_tag",
        joinColumns = @JoinColumn(name = "certificate_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    public GiftCertificate(String name, String description, Double price, Long duration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }
}
