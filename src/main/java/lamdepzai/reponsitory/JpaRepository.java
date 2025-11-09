package lamdepzai.reponsitory;

import java.util.List;
import java.util.Optional;

public interface JpaRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);    // add hoặc update
    void delete(T entity);
}
