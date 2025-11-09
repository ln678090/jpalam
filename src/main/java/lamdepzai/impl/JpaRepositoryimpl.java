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

    public <R> R executeQuery(String jpql, Class<R> resultClass, Object... params) {
        EntityManager em = getEntityManager();
        try {
            var query = em.createQuery(jpql, resultClass);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public <R> List<R> executeQueryList(String jpql, Class<R> resultClass, Object... params) {
        EntityManager em = getEntityManager();
        try {
            var query = em.createQuery(jpql, resultClass);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
            return query.getResultList();
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
}
