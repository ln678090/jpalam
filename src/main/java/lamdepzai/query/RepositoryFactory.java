package lamdepzai.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.lang.reflect.Proxy;

public class RepositoryFactory {
    // existing method that accepts an EntityManager (keeps backward compatibility)
    public static <T> T createRepository(Class<T> repoInterface, EntityManager em, Class<?> domainClass) {
        RepositoryProxy<T> proxy = new RepositoryProxy<>(repoInterface, em, domainClass);
        Object instance = Proxy.newProxyInstance(
                repoInterface.getClassLoader(),
                new Class[]{repoInterface},
                proxy);
        return repoInterface.cast(instance);
    }

    // New convenience method: accept EntityManagerFactory and domain class
    // so callers can create a repository in one line like Spring Data JPA.
    public static <T> T createRepository(Class<T> repoInterface, EntityManagerFactory emf, Class<?> domainClass) {
        // InvocationHandler will create/close EntityManager or use JpaRepositoryimpl which opens its own
        RepositoryInvocationHandler handler = new RepositoryInvocationHandler(emf, domainClass);
        Object instance = Proxy.newProxyInstance(
                repoInterface.getClassLoader(),
                new Class[]{repoInterface},
                handler);
        return repoInterface.cast(instance);
    }
}
