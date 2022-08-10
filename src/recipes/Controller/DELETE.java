package recipes.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import recipes.Service.RecipeService;
import recipes.Service.UserService;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;

@RestController
public class DELETE {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteRecipe(@RequestHeader(value = "Authorization", required = false) String authentication,
                                                                @PathVariable(name = "id") int id) {

        try {
            if(authentication == null) throw new AuthenticationException("No authorization header");
            int userId = userService.authorizeUser(authentication); // this may throw an authentication error
            recipeService.recipeExistsAndBelongsToUser(id, userId); // this may throw access denied error or not found
            return recipeService.deleteRecipe(id);
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
