package warehouse.com.productmanagementservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"created", "updated"}, allowGetters = true)
@Entity
@Table(name = "products")
public class Product implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "product_name", unique = true, nullable = false)
  private String productName;

  @Column(name = "amount_reserved", nullable = false)
  private int amountOfReserved;

  @Column(name = "purchase_price", nullable = false)
  private BigDecimal purchasePrice;

  @Column(name = "sale_price", nullable = false)
  private BigDecimal salePrice;

  @ManyToOne(optional = false, cascade = CascadeType.ALL)
  @JsonBackReference
  private ProductGroup productGroup;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "product_warehouse",
      joinColumns = {@JoinColumn(name = "product_id")},
      inverseJoinColumns = {@JoinColumn(name = "warehouse_id")}
  )
  private List<Warehouse> warehouses;

  @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
  private List<ProductStock> stockItems;

  @Column(name = "article", nullable = false)
  private String article;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", updatable = false)
  @CreatedDate
  private Date created;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated")
  @LastModifiedDate
  private Date updated;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Product that = (Product) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : super.hashCode();
  }
}
