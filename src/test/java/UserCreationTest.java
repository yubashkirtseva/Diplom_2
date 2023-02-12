import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class UserCreationTest {
    private User user;
    private User other_user;
    private  UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGeneration.getDefault();
        other_user = UserGeneration.getIncompleteDate();
        userClient = new UserClient();
    }

    @After
    public void cleanUp(){
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        String getAccessToken = loginResponse.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
        userClient.delete(bearerToken);

    }

    @Test
    @DisplayName("Проверка успешного создания уникального пользователя")
    public void checkUserCanBeCreated() {
        ValidatableResponse response = userClient.register(user);
        int statusCode = response.extract().statusCode();
        Boolean success = response.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Проверка ошибки при создании ранее зарегистрированного пользователя")
    public void checkRepeatedUserCantBeCreated() {
        userClient.register(user);
        ValidatableResponse response = userClient.register(user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals("User already exists", message);
    }

    @Test
    @DisplayName("Проверка ошибки при создании пользователя без заполненного обязательного поля")
    public void checkUserWithoutRequiredFieldCantBeCreated() {
        ValidatableResponse response = userClient.register(other_user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals("Email, password and name are required fields", message);
    }




}
