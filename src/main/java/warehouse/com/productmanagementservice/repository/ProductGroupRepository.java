package warehouse.com.productmanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warehouse.com.productmanagementservice.model.ProductGroup;

public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {

}