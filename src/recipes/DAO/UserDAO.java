package recipes.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import recipes.Entity.Recipe;
import recipes.Entity.User;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    @Query(value =
            "SELECT u " +
            "FROM User u " +
            "WHERE u.email=:email")
    List<User> getUserByEmail(@Param("email") String email);
}
