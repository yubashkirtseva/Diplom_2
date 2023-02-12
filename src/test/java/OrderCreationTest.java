import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class OrderCreationTest {
    private User user;
    private Ingredients ingredients;
    private Ingredients withoutIngredients;
    private Ingredients withIncorrectIngredients;
    private  UserClient userClient;
    private OrderClient orderClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGeneration.getDefault();
        ingredients = IngredientsGeneration.getDefault();
        withoutIngredients = IngredientsGeneration.getNull();
        withIncorrectIngredients = IngredientsGeneration.getNotCorrect();
        userClient = new UserClient();
        orderClient = new OrderClient();
        userClient.register(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        String getAccessToken = loginResponse.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
    }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }

    @Test
    @DisplayName("Проверка успешного создания заказа")
    public void checkOrderWithIngredientsCanBeCreatedByAutoUser(){
        ValidatableResponse orderResponse = orderClient.createOrder(ingredients, bearerToken);
        int statusCode = orderResponse.extract().statusCode();
        String name = orderResponse.extract().path("name");
        assertEquals(SC_OK, statusCode);
        assertEquals("Spicy бессмертный флюоресцентный бургер", name);
    }

    @Test
    @DisplayName("Проверка ошибки при создании заказа без ингредиентов")
    public void checkOrderWithoutIngredientsCantBeCreated(){
        ValidatableResponse orderResponse = orderClient.createOrder(withoutIngredients, bearerToken);
        int statusCode = orderResponse.extract().statusCode();
        String message = orderResponse.extract().path("message");
        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals("Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Проверка ошибки при создании заказа с неверным хешем ингредиентов")
    public void checkOrderWithIncorrectIngredientsCantBeCreated(){
        ValidatableResponse orderResponse = orderClient.createOrder(withIncorrectIngredients, bearerToken);
        int statusCode = orderResponse.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

}
