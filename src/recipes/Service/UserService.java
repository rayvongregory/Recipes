package recipes.Service;

import org.springframework.http.ResponseEntity;
import recipes.Request.UserRequest;

import javax.naming.AuthenticationException;
import java.util.HashMap;

public interface UserService {
    ResponseEntity<HashMap<String, Object>> registerUser(UserRequest userRequest);
    int authorizeUser(String authentication) throws AuthenticationException;

}
