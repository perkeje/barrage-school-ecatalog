package net.barrage.school.java.ecatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
public class Merchant {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "merchant", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Product> products;
}