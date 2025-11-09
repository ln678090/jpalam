package lamdepzai.reponsitory;

import lamdepzai.entity.User;
import lamdepzai.query.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email = ?1 or u.userName = ?1")
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
}
