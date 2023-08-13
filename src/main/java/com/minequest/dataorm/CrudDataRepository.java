package com.minequest.dataorm;


import java.util.List;

public interface CrudDataRepository<E, ID> {

  E save(E entity);

  E findById(ID id);

  List<E> findAll();

  void deleteById(ID id);

}
