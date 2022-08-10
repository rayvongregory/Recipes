package recipes.Entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipes.Entity.Serializable.IngredientId;

import javax.persistence.*;

@Entity
@IdClass(IngredientId.class)
@Table(name = "RecipeIngredient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    @Id
    @Column(name = "IngredientId")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "IngredientIdGenerator")
    @TableGenerator(name = "IngredientIdGenerator", table = "IngredientIdGenerator")
    private int ingredientId;

    @Id
    @NotNull
    @Column(name = "RecipeId")
    private int recipeId;

    @NotNull
    @Column(name = "Ingredient")
    private String ingredient;
}
