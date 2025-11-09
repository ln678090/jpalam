package lamdepzai.query;

import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class RepositoryProxy<T> implements InvocationHandler {
    private final Class<T> repositoryInterface;
    private final EntityManager entityManager;
    private final Class<?> domainClass;

    public RepositoryProxy(Class<T> repositoryInterface, EntityManager em, Class<?> domainClass) {
        this.repositoryInterface = repositoryInterface;
        this.entityManager = em;
        this.domainClass = domainClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Query queryAnnotation = method.getAnnotation(Query.class);
        if (queryAnnotation != null) {
            String jpql = queryAnnotation.value();
            TypedQuery<?> query = entityManager.createQuery(jpql, domainClass);

            // map args to parameters ?1, ?2, ... (simplify here)
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i + 1, args[i]);
            }

            // handle return type, e.g. Optional<User>
            Class<?> returnType = method.getReturnType();

            if (returnType.equals(Optional.class)) {
                Object singleResult = null;
                try {
                    singleResult = query.getSingleResult();
                } catch (NoResultException e) {
                    return Optional.empty();
                }
                return Optional.ofNullable(singleResult);
            }

            // handle List return type
            if (List.class.isAssignableFrom(returnType)) {
                return query.getResultList();
            }

            return query.getSingleResult();
        }

        // fallback default methods here (e.g. findAll, save) or throw exception
        throw new UnsupportedOperationException("Method not supported: " + method.getName());
    }
}
