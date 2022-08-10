package recipes.Entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipes.Entity.Serializable.DirectionId;

import javax.persistence.*;

@Entity
@IdClass(DirectionId.class)
@Table(name = "RecipeDirection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDirection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "DirectionIdGenerator")
    @TableGenerator(name = "DirectionIdGenerator", table = "DirectionIdGenerator")
    @Column(name = "DirectionId")
    private int directionId;

    @Id
    @NotNull
    @Column(name = "RecipeId")
    private int recipeId;

    @NotNull
    @Column(name = "Direction")
    private String direction;
}
