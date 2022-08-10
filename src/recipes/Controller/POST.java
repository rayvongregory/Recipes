package recipes.Controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.Request.RecipeRequest;
import recipes.Request.UserRequest;
import recipes.Service.RecipeService;
import recipes.Service.UserService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class POST {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @PostMapping("/api/recipe/new")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> postNewRecipe(@RequestHeader(value = "Authorization", required = false) String authentication,
                                                                 @RequestBody RecipeRequest recipeRequest) {

        try {
            if(authentication == null) throw new AuthenticationException("No authorization header");
            int userId = userService.authorizeUser(authentication); // this should throw an error
            return recipeService.addNewRecipe(recipeRequest, userId);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<>(){
                {
                    put("error", e.getMessage());
                }
            });
        }
    }

    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> registerUser(@RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }
}
