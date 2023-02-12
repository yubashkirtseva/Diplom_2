import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client{

    private static final String ORDER_PATH = "api/orders";

    public ValidatableResponse createOrder(Ingredients ingredients, String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOrders(String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }

}
