package recipes.Entity.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientId implements Serializable {
    private int ingredientId;
    private int recipeId;
}
