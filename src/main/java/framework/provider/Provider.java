package framework.provider;

import java.util.List;
import java.util.Optional;

public interface Provider<E, ID> {

    List<E> findAll();

    Optional<E> save(E entity);


}
