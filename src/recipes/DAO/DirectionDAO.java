package recipes.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import recipes.Entity.RecipeDirection;

import java.util.List;

public interface DirectionDAO extends JpaRepository<RecipeDirection, Integer> {
    @Query(value =
            "SELECT rd " +
            "FROM RecipeDirection rd " +
            "WHERE rd.recipeId=:id")
    List<RecipeDirection> getDirectionsByRecipeId(@Param("id") int id);

    @Query(value =
            "SELECT rd.direction " +
            "FROM RecipeDirection rd " +
            "WHERE rd.recipeId=:id")
    List<String> getDirectionsByRecipeId__DIRECTION_ONLY(@Param("id") int id);
}
