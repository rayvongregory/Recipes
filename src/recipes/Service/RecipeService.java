package recipes.Service;

import org.springframework.http.ResponseEntity;
import recipes.Request.RecipeRequest;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Optional;

public interface RecipeService {
    ResponseEntity<HashMap<String, Object>> addNewRecipe(RecipeRequest recipeRequest, int userId);
    ResponseEntity<HashMap<String, Object>> getRecipe(int id);
    ResponseEntity<HashMap<String, Object>> deleteRecipe(int id);
    ResponseEntity<HashMap<String, Object>> updateRecipe(int id, RecipeRequest recipeRequest) ;
    ResponseEntity<Object> searchRecipes(Optional<String> name, Optional<String> category);
    boolean recipeExistsAndBelongsToUser(int recipeId, int userId) throws AccessDeniedException;
}
