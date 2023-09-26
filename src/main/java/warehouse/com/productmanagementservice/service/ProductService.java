package warehouse.com.productmanagementservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import warehouse.com.productmanagementservice.model.dto.request.ProductDto;

public interface ProductService {

  Mono<ProductDto> createProduct(ProductDto productDto);

  Mono<ProductDto> updateProduct(Long id, ProductDto productDto);

  Mono<ProductDto> findProductById(Long id);

  Flux<Page<ProductDto>> getAllProducts(Pageable pageable);

  Mono<Void> deleteProductById(Long id);
}
