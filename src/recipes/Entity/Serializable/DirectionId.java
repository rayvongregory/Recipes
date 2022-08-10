package recipes.Entity.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectionId implements Serializable  {
    private int directionId;
    private int recipeId;
}
