package warehouse.com.productmanagementservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import warehouse.com.productmanagementservice.model.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

  Flux<Page<Product>> findAllBy(Pageable pageable);
}