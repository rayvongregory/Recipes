package recipes.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.Request.RecipeRequest;
import recipes.Service.RecipeService;
import recipes.Service.UserService;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;

@RestController
public class PUT {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @PutMapping("/api/recipe/{id}")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> updateRecipe(@RequestHeader(value = "Authorization", required = false) String authentication,
                                                                @PathVariable(name = "id") int id,
                                                                @RequestBody RecipeRequest recipeRequest) {
        try {
            if(authentication == null) throw new AuthenticationException("No authorization header");
            int userId = userService.authorizeUser(authentication); // this should throw an error
            recipeService.recipeExistsAndBelongsToUser(id, userId);
            return recipeService.updateRecipe(id, recipeRequest);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(){
                {
                    put("error", e.getMessage());
                }
            });
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HashMap<>(){
                {
                    put("error", e.getMessage());
                }
            });
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>(){
                {
                    put("error", e.getMessage());
                }
            });
        }
    }

}
