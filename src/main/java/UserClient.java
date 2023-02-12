import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client{
    private static final String CREATE_PATH = "api/auth/register";
    private static final String LOGIN_PATH = "api/auth/login ";
    private static final String CHANGE_PATH = "api/auth/user";
    private static final String DELETE_PATH = "api/auth/user";

    public ValidatableResponse register(User user){
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_PATH)
                .then();
    }

    public ValidatableResponse login(UserCredentials credentials){
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse delete(String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .when()
                .delete(DELETE_PATH)
                .then();
    }

    public ValidatableResponse change(User user, String bearerToken){
        return given()
                .spec(getSpec())
                .header("authorization", bearerToken)
                .body(user)
                .when()
                .patch(CHANGE_PATH)
                .then();
    }
    public ValidatableResponse changeWithoutToken(User user){
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(CHANGE_PATH)
                .then();
    }

}
