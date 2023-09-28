package warehouse.com.productmanagementservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warehouse.com.productmanagementservice.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findAllBy(Pageable pageable);

  boolean existsByProductName(String productName);
}