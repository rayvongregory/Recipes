package recipes.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Recipe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "RecipeIdGenerator")
    @TableGenerator(name = "RecipeIdGenerator", table = "RecipeIdGenerator")
    @Column(name = "RecipeId")
    private int recipeId;

    @NotNull
    @Column(name = "RecipeName")
    private String recipeName;

    @NotNull
    @Column(name = "Category")
    private String category;

    @NotNull
    @Column(name = "Description")
    private String description;

    @NotNull
    @Column(name = "Date")
    private String date;

    @NotNull
    @Column(name = "UserId")
    private int userId;

}
