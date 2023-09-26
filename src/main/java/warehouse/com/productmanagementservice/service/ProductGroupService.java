package warehouse.com.productmanagementservice.service;

import reactor.core.publisher.Mono;
import warehouse.com.productmanagementservice.model.dto.request.ProductGroupDto;

public interface ProductGroupService {

  Mono<ProductGroupDto> findProductGroupById(Long id);
}
