//package warehouse.com.productmanagementservice.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import reactor.core.publisher.Mono;
//import warehouse.com.productmanagementservice.exception.EntityNotFoundException;
//import warehouse.com.productmanagementservice.model.dto.request.ProductGroupDto;
//import warehouse.com.productmanagementservice.repository.ProductGroupRepository;
//import warehouse.com.productmanagementservice.service.ProductGroupService;
//
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//@Service
//public class ProductGroupServiceImpl implements ProductGroupService {
//
//  private final ProductGroupRepository productGroupRepository;
//
//  @Override
//  public Mono<ProductGroupDto> findProductGroupById(Long id) {
////    return productGroupRepository.findById(id)
////        .switchIfEmpty(Mono.error(new EntityNotFoundException()))
////        .map(productMapper::toDto);
//
//    return null;
//}
//}