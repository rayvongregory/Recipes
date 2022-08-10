package recipes.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.Entity.Recipe;
import recipes.Service.RecipeService;
import recipes.Service.UserService;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class GET {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/recipe/{id}")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> getRecipe(@RequestHeader(value = "Authorization", required = false) String authentication,
                                                             @PathVariable(name = "id") int id) {
        try {
            if(authentication == null) throw new AuthenticationException("No authorization header");
            userService.authorizeUser(authentication); // this may throw an authentication error
            return recipeService.getRecipe(id);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>() {
                {
                    put("error", e.getMessage());
                }
            });
        }
    }

    @GetMapping("/api/recipe/search")
    @ResponseBody
    public ResponseEntity<Object> searchRecipes(@RequestHeader(value = "Authorization", required = false) String authentication,
                                                @RequestParam Optional<String> name,
                                                @RequestParam Optional<String> category) {
        try {
            if(authentication == null) throw new AuthenticationException("No authorization header");
            userService.authorizeUser(authentication); // this may throw an authentication error
            return recipeService.searchRecipes(name, category);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>() {
                {
                    put("error", e.getMessage());
                }
            });
        }
    }

}
