package warehouse.com.productmanagementservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import warehouse.com.productmanagementservice.model.ProductGroup;

@Repository
public interface ProductGroupRepository extends ReactiveCrudRepository<ProductGroup, Long> {

}