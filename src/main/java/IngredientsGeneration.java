import java.util.Arrays;
import java.util.List;

public class IngredientsGeneration {
    public static Ingredients getDefault(){
        return new Ingredients(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72"));
    }
    public static Ingredients getNull(){
        return new Ingredients(List.of());
    }
    public static Ingredients getNotCorrect(){
        return new Ingredients(List.of("!61c0c5a71d1f82001bdaaa6d", "!61c0c5a71d1f82001bdaaa6f", "!61c0c5a71d1f82001bdaaa72"));
    }

}
