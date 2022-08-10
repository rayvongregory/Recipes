package recipes.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import recipes.Entity.RecipeIngredient;

import java.util.List;

public interface IngredientDAO extends JpaRepository<RecipeIngredient, Integer> {

    @Query(value =
            "SELECT ri " +
            "FROM RecipeIngredient ri " +
            "WHERE ri.recipeId=:id")
    List<RecipeIngredient> getIngredientsByRecipeId(@Param("id") int id);

    @Query(value =
            "SELECT ri.ingredient " +
            "FROM RecipeIngredient ri " +
            "WHERE ri.recipeId=:id")
    List<String> getIngredientsByRecipeId__INGREDIENT_ONLY(@Param("id") int id);
}
