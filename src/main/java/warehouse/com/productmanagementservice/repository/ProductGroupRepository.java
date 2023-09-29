package warehouse.com.productmanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warehouse.com.productmanagementservice.model.entity.ProductGroup;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {

  boolean existsByProductGroupName(String groupName);

}