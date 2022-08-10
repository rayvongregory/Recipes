package recipes.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeRequest {

    private String name;
    private String category;
    private String description;
    private String[] ingredients;
    private String[] directions;
}
