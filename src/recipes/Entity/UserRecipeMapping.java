package recipes.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipes.Entity.Serializable.IngredientId;
import recipes.Entity.Serializable.UserRecipeId;

import javax.persistence.*;

@Entity
@IdClass(UserRecipeId.class)
@Table(name = "UserRecipeMapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecipeMapping {

    @Id
    @Column(name = "UserId")
    private int userId;

    @Id
    @Column(name = "RecipeId")
    private int recipeId;
}
