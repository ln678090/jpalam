package lamdepzai.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lamdepzai.reponsitory.JpaRepository;

import java.util.List;
import java.util.Optional;

public class JpaRepositoryimpl<T, ID> implements JpaRepository<T, ID> {

    private final Class<T> entityClass;
    private final EntityManagerFactory emf;

    public JpaRepositoryimpl(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }
    @Override
    public <R> R executeQuery(String jpql, Class<R> resultClass, Object... params) {
        EntityManager em = getEntityManager();
        try {
            var query = em.createQuery(jpql, resultClass);

            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }

            List<R> list = query.getResultList();

            if (list == null || list.isEmpty()) return null;

            return list.get(0);

        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public <R> List<R> executeQueryList(String jpql, Class<R> resultClass, Object... params) {
        EntityManager em = getEntityManager();
        try {
            var query = em.createQuery(jpql, resultClass);

            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }

            List<R> result = query.getResultList();
            return result == null ? List.of() : result;
        } catch (Exception e) {

            return List.of();
        } finally {
            em.close();
        }
    }


    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        EntityManager em = getEntityManager();
        try {
            return Optional.ofNullable(em.find(entityClass, id));
        } finally {
            em.close();
        }
    }

    @Override
    public T save(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T merged = em.merge(entity);
            em.getTransaction().commit();
            return merged;
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T managed = em.merge(entity);
            em.remove(managed);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * @return Số bản ghi
     */
    @Override
    public long count() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }
    /**
     * @param pageNumber Số trang
     * @param pageSize Số bản ghi
     * @return Danh sách bản ghi
     */
    @Override
    public List<T> findAllPage(int pageNumber, int pageSize) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM " + entityClass.getSimpleName() + " e",
                            entityClass
                    )
                    .setFirstResult(pageNumber * pageSize)  // OFFSET
                    .setMaxResults(pageSize)                // LIMIT
                    .getResultList();
        } finally {
            em.close();
        }
    }
    /**
     * @param jpql lệnh JPQL truy vấn
     * @param resultClass Loại kết quả
     * @param pageNumber Số trang
     * @param pageSize Số bản ghi
     * @param params Tham số
     * @return Danh sách bản ghi
     */
    @Override
    public <R> List<R> executeQueryWithPagination(
            String jpql,
            Class<R> resultClass,
            int pageNumber,
            int pageSize,
            Object... params) {
        EntityManager em = getEntityManager();
        try {
            var query = em.createQuery(jpql, resultClass);

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }

            return query
                    .setFirstResult(pageNumber * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    /**
     * @param jpql lệnh JPQL truy vấn
     * @param params Tham số
     * @return Số bản ghi
     */
    @Override
    public long countQuery(String jpql, Object... params) {
        EntityManager em = getEntityManager();
        try {
            // Chuyển SELECT ... FROM thành SELECT COUNT(*) FROM
            String countJpql = jpql.replaceFirst("(?i)SELECT .+ FROM", "SELECT COUNT(*) FROM");

            var query = em.createQuery(countJpql, Long.class);

            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
