package recipes.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import recipes.Entity.Recipe;

import java.util.List;

public interface RecipeDAO extends JpaRepository<Recipe, Object> {
    @Query(value =
            "SELECT r " +
            "FROM Recipe r " +
            "WHERE r.recipeName LIKE %:name% " +
            "ORDER BY date")
    List<Recipe> getRecipesByName(@Param("name") String name);

    @Query(value =
            "SELECT r " +
            "FROM Recipe r " +
            "WHERE r.category=:category " +
            "ORDER BY date")
    List<Recipe> getRecipesByCategory(@Param("category") String category);

    @Query(value =
            "SELECT r " +
                    "FROM Recipe r " +
                    "WHERE r.id=:id " +
                    "AND r.userId=:userId " +
                    "ORDER BY date")
    List<Recipe> getByRecipeIdAndUserId(@Param("id") int recipeId, @Param("userId") int userId);
}
