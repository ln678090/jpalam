package lamdepzai.query;

import jakarta.persistence.EntityManagerFactory;
import lamdepzai.impl.JpaRepositoryimpl;
import lamdepzai.query.annotation.Query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class RepositoryInvocationHandler implements InvocationHandler {
    private final JpaRepositoryimpl<?, ?> repository;
    private final Class<?> entityClass;

    public RepositoryInvocationHandler(EntityManagerFactory emf, Class<?> entityClass) {
        this.repository = new JpaRepositoryimpl<>(emf, entityClass);
        this.entityClass = entityClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Kiểm tra xem phương thức có annotation @Query không
        Query queryAnnotation = method.getAnnotation(Query.class);
        if (queryAnnotation != null) {
            String jpql = queryAnnotation.value();
            
            // Xác định kiểu trả về
            Class<?> returnType = method.getReturnType();
            if (List.class.isAssignableFrom(returnType)) {
                // Nếu trả về List
                return ((JpaRepositoryimpl<?, ?>) repository).executeQueryList(
                    jpql, 
                    entityClass,
                    args != null ? args : new Object[0]
                );
            } else {
                // Nếu trả về đối tượng đơn lẻ
                return ((JpaRepositoryimpl<?, ?>) repository).executeQuery(
                    jpql, 
                    returnType,
                    args != null ? args : new Object[0]
                );
            }
        }

        // Nếu không có @Query, cố gắng delegate tới phương thức tương ứng trên JpaRepositoryimpl
        try {
            Method repoMethod = repository.getClass().getMethod(method.getName(), method.getParameterTypes());
            return repoMethod.invoke(repository, args);
        } catch (NoSuchMethodException e) {
            // Không có phương thức tương ứng trên repository implementation
            throw new UnsupportedOperationException("Method not implemented: " + method.getName());
        }
    }
}