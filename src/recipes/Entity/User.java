package recipes.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public enum Role {
        USER, ADMIN;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UserIdGenerator")
    @TableGenerator(name = "UserIdGenerator", table = "UserIdGenerator")
    @Column(name = "UserId")
    private int userId;

    @NotNull
    @Column(name = "Email")
    private String email;

    @NotNull
    @Column(name = "Password")
    private String password;

    @NotNull
    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;
}

