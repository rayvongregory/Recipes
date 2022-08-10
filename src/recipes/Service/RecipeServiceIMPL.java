package recipes.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.DAO.DirectionDAO;
import recipes.DAO.IngredientDAO;
import recipes.DAO.RecipeDAO;
import recipes.Entity.Recipe;
import recipes.Entity.RecipeDirection;
import recipes.Entity.RecipeIngredient;
import recipes.Request.RecipeRequest;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RecipeServiceIMPL implements RecipeService {

    @Autowired
    private RecipeDAO recipeDAO;
    @Autowired
    private IngredientDAO ingredientDAO;
    @Autowired
    private DirectionDAO directionDAO;

    @Override
    public ResponseEntity<HashMap<String, Object>> addNewRecipe(RecipeRequest recipeRequest, int userId) {
        String name = recipeRequest.getName();
        String description = recipeRequest.getDescription();
        String category = recipeRequest.getCategory();
        String[] ingredients = recipeRequest.getIngredients();
        String[] directions = recipeRequest.getDirections();
        if(name == null || name.trim().equals("") ||
                description == null || description.trim().equals("") ||
                category == null || category.trim().equals("") ||
                ingredients == null || ingredients.length == 0 ||
                directions == null || directions.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {
                {
                    put("error", "recipe missing information");
                }
            });
        }
        Recipe recipe = new Recipe();
        recipe.setRecipeName(name);
        recipe.setCategory(category);
        recipe.setDescription(description);
        recipe.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        recipe.setUserId(userId);
        try {
            int id = recipeDAO.save(recipe).getRecipeId();

            for(String i : ingredients) {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setRecipeId(id);
                ingredient.setIngredient(i);
                ingredientDAO.save(ingredient);
            }

            for(String d : directions) {
                RecipeDirection direction = new RecipeDirection();
                direction.setRecipeId(id);
                direction.setDirection(d);
                directionDAO.save(direction);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new HashMap<>() {
                {
                    put("id", id);
                }
            });
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>() {
                {
                    put("error", "something went wrong, recipe was not saved");
                }
            });
        }
    }

    @Override
    public ResponseEntity<HashMap<String, Object>> getRecipe(int id) {
        try {
            Recipe recipe =  recipeDAO.getById(id);
            List<RecipeIngredient> ingredientList = ingredientDAO.getIngredientsByRecipeId(id);
            List<RecipeDirection> directionList = directionDAO.getDirectionsByRecipeId(id);

            return ResponseEntity.status(HttpStatus.OK).body(new HashMap<>() {
                {
                    put("name", recipe.getRecipeName());
                    put("category", recipe.getCategory());
                    put("description", recipe.getDescription());
                    put("ingredients", reduceList(ingredientList));
                    put("directions", reduceList(directionList));
                    put("date", recipe.getDate());
                }
            });

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {
                {
                    put("error", String.format("recipe with id %s not found", id));
                }
            });
        }
    }

    @Override
    public ResponseEntity<HashMap<String, Object>> deleteRecipe(int id) {
        try {
            recipeDAO.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new HashMap<>() {
                {
                    put("msg", String.format("recipe with id %s successfully deleted", id));
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {
                {
                    put("error", String.format("recipe with id %s not found", id));
                }
            });
        }
    }

    @Override
    public ResponseEntity<HashMap<String, Object>> updateRecipe(int id, RecipeRequest recipeRequest) {
        try {
            Recipe recipe =  recipeDAO.getById(id);
            String requestName = recipeRequest.getName();
            String requestCategory = recipeRequest.getCategory();
            String requestDescription = recipeRequest.getDescription();
            String[] requestIngredients = recipeRequest.getIngredients();
            String[] requestDirections = recipeRequest.getDirections();

            if(requestName == null || requestName.trim().equals("") ||
            requestDescription == null || requestDescription.trim().equals("") ||
            requestCategory == null || requestCategory.trim().equals("") ||
            requestIngredients == null || requestIngredients.length == 0 ||
            requestDirections == null || requestDirections.length == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>() {
                    {
                        put("error", "recipe missing information");
                    }
                });
            }

            recipe.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            recipe.setRecipeName(requestName);
            recipe.setCategory(requestCategory);
            recipe.setDescription(requestDescription);
            recipe.setUserId(recipe.getUserId());
            for(RecipeIngredient i : ingredientDAO.getIngredientsByRecipeId(id)) ingredientDAO.delete(i);
            for(String i : requestIngredients) {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setRecipeId(id);
                ingredient.setIngredient(i);
                ingredientDAO.save(ingredient);
            }

            for(RecipeDirection d : directionDAO.getDirectionsByRecipeId(id)) directionDAO.delete(d);
            for(String d : recipeRequest.getDirections()) {
                RecipeDirection direction = new RecipeDirection();
                direction.setRecipeId(id);
                direction.setDirection(d);
                directionDAO.save(direction);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new HashMap<>() {
                {
                    put("name", recipe.getRecipeName());
                    put("description", recipe.getDescription());
                    put("category", recipe.getCategory());
                    put("ingredients", reduceList(ingredientDAO.getIngredientsByRecipeId(id)));
                    put("directions", reduceList(directionDAO.getDirectionsByRecipeId(id)));
                    put("date", recipe.getDate());
                }
            });

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {
                {
                    put("error", String.format("recipe with id %s not found", id));
                }
            });
        }
    }

    @Override
    public ResponseEntity<Object> searchRecipes(Optional<String> name, Optional<String> category) {
        String searchName = name.orElse(null);
        String searchCategory = category.orElse(null);
        if((searchName != null && searchCategory != null) ||
                (searchName == null && searchCategory == null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if(searchName != null) {
            return ResponseEntity.status(HttpStatus.OK).body(formatList(recipeDAO.getRecipesByName(searchName)));
        }
        return ResponseEntity.status(HttpStatus.OK).body(formatList(recipeDAO.getRecipesByCategory(searchCategory)));
    }

    private String[] reduceList(Object ingOrDir) {
        try {
            List<RecipeIngredient> ingredients = (List<RecipeIngredient>) ingOrDir;
            String[] strArr = new String[ingredients.size()];
            for(int i = 0; i < strArr.length; i++) strArr[i] = ingredients.get(i).getIngredient();
            return strArr;
        } catch (ClassCastException e) {
            List<RecipeDirection> directions = (List<RecipeDirection>) ingOrDir;
            String[] strArr = new String[directions.size()];
            for(int i = 0; i < strArr.length; i++) strArr[i] = directions.get(i).getDirection();
            return strArr;
        }
    }

    private List<HashMap<String, Object>> formatList(List<Recipe> recipes) {
        List<HashMap<String, Object>> searchResults = new ArrayList<>();
        for(Recipe recipe : recipes) {
            int id = recipe.getRecipeId();
            searchResults.add(new HashMap<>() {
                {
                    put("name", recipe.getRecipeName());
                    put("category", recipe.getCategory());
                    put("description", recipe.getDescription());
                    put("ingredients", ingredientDAO.getIngredientsByRecipeId__INGREDIENT_ONLY(id));
                    put("directions", directionDAO.getDirectionsByRecipeId__DIRECTION_ONLY(id));
                    put("date", recipe.getDate());
                }
            });
        }
        Collections.reverse(searchResults);
        return searchResults;
    }

    @Override
    public boolean recipeExistsAndBelongsToUser(int recipeId, int userId) throws AccessDeniedException, EntityNotFoundException {
        try {
            Recipe recipe = recipeDAO.getById(recipeId);
            if (recipe.getUserId() != userId)
                throw new AccessDeniedException("This recipe does not belong to you.");
        } catch (EntityNotFoundException e) {
            throw  new EntityNotFoundException("Recipe with id: " + recipeId + " does not exist.");
        }
        return true;
    }

}
