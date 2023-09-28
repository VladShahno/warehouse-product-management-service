package warehouse.com.productmanagementservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<D, E> {

  E create(D dto);

  E update(Long id, D dto);

  E findById(Long id);

  Page<E> getAll(Pageable pageable);

  void deleteById(Long id);
}
