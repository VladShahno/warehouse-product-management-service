package warehouse.com.productmanagementservice.service.impl;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static warehouse.com.productmanagementservice.common.Constants.Logging.NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import warehouse.com.productmanagementservice.exception.EntityNotFoundException;
import warehouse.com.productmanagementservice.mapper.ProductMapper;
import warehouse.com.productmanagementservice.model.dto.request.ProductDto;
import warehouse.com.productmanagementservice.repository.ProductRepository;
import warehouse.com.productmanagementservice.service.ProductService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;


  @Override
  public Mono<ProductDto> createProduct(ProductDto productDto) {
    log.debug("Request to save Product with {}", kv(NAME, productDto.getProductName()));
    var product = productMapper.toEntity(productDto);
    //var productGroup =

    return null;
  }

  @Override
  public Mono<ProductDto> updateProduct(Long id, ProductDto productDto) {
    return null;
  }

  @Override
  public Mono<ProductDto> findProductById(Long id) {
    return productRepository.findById(id)
        .switchIfEmpty(Mono.error(new EntityNotFoundException()))
        .map(productMapper::toDto);
  }

  @Override
  public Flux<Page<ProductDto>> getAllProducts(Pageable pageable) {
    return productRepository.findAllBy(pageable).map(products -> products.map(
        productMapper::toDto));
  }


  @Override
  public Mono<Void> deleteProductById(Long id) {
    return productRepository.deleteById(id);
  }
}
