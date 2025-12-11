package lamdepzai.reponsitory;

import java.util.List;
import java.util.Optional;

public interface JpaRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);    // add hoặc update
    void delete(T entity);
    List<T> findAll(int pageNumber, int pageSize);
    long count();
    <R> R executeQuery(String jpql, Class<R> resultClass, Object... params);
    <R> List<R> executeQueryList(String jpql, Class<R> resultClass, Object... params);

    List<T> findAllPage(int pageNumber, int pageSize);

    <R> List<R> executeQueryWithPagination(String jpql, Class<R> resultClass, int pageNumber, int pageSize, Object... params);
    long countQuery(String jpql, Object... params);
}
