package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Audited
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @JsonBackReference
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "certificate_id")
  private GiftCertificate certificate;

  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "purchase_time", nullable = false)
  private LocalDateTime purchaseTime;

  @Column(name = "create_date", updatable = false)
  private String createDate;
  @PrePersist
  protected void onCreate() {
    if (this.createDate == null) {
      this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
          .format(LocalDateTime.now(ZoneOffset.UTC));
    }
  }

  public Order(User user, GiftCertificate certificate, Double price, LocalDateTime purchaseTime) {
    this.user = user;
    this.certificate = certificate;
    this.price = price;
    this.purchaseTime = purchaseTime;
  }
}
