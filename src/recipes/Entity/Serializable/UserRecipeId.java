package recipes.Entity.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecipeId implements Serializable {
    private int userId;
    private int recipeId;
}
