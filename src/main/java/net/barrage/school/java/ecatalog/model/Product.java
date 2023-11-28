package net.barrage.school.java.ecatalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Product {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String imageUrl;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be a positive value")
    @Column(nullable = false)
    private double price;
}