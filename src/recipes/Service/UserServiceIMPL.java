package recipes.Service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.DAO.UserDAO;
import recipes.Entity.User;
import recipes.Request.UserRequest;
import org.jasypt.util.password.BasicPasswordEncryptor;


import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceIMPL implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public ResponseEntity<HashMap<String, Object>> registerUser(UserRequest userRequest) {
        String email = userRequest.getEmail().trim();
        String password = userRequest.getPassword().trim();
        if(email == null || email.equals("") || !email.contains("@") || !email.contains(".")
                ||  password == null || password.trim().equals("") || password.length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>(){
                {
                    put("error", "Email or password do not meet requirements. Email must contain @ and . (dot) symbols. Passwords must be at least 8 characters.");
                }
            });
        }

        List<User> userList = userDAO.getUserByEmail(email);
        if(!userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<>(){
                {
                    put("error", "An account is already associated with this email address.");
                }
            });
        }
        User user = new User();
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        user.setEmail(email);
        user.setPassword(passwordEncryptor.encryptPassword(password));
        user.setRole(User.Role.USER);
        userDAO.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new HashMap<>(){
            {
                put("msg", "New user created!");
            }
        });
    }

    @Override
    public int authorizeUser(String encodedAuthHeader) throws AuthenticationException {
        String pair = new String(Base64.decodeBase64(encodedAuthHeader.substring(6)));
        String[] parts = pair.split(":");
        String email = parts[0];
        String password = parts[1];
        List<User> userList = userDAO.getUserByEmail(email);
        if(userList.isEmpty()) throw new AuthenticationException("User with that email does not exist");
        User user = userList.get(0);
        String hashedPassword = user.getPassword();
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if(!passwordEncryptor.checkPassword(password, hashedPassword))
            throw new AuthenticationException("Incorrect credentials");
        return user.getUserId();
    }
}
