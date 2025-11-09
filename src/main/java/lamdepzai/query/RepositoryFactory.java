package lamdepzai.query;
import jakarta.persistence.EntityManager;

import java.lang.reflect.Proxy;

public class RepositoryFactory {
    public static <T> T createRepository(Class<T> repoInterface, EntityManager em, Class<?> domainClass) {
        RepositoryProxy<T> proxy = new RepositoryProxy<>(repoInterface, em, domainClass);
        Object instance = Proxy.newProxyInstance(
                repoInterface.getClassLoader(),
                new Class[]{repoInterface},
                proxy);
        return repoInterface.cast(instance);
    }
}
