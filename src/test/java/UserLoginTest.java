import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class UserLoginTest {
    private User user;
    private User other_user;
    private  UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGeneration.getDefault();
        other_user = UserGeneration.getIncorrectDate();
        userClient = new UserClient();
    }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }

    @Test
    @DisplayName("Проверка успешной авторизации")
    public void checkUserCanBeLoggedIn(){
        userClient.register(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        String getAccessToken = loginResponse.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
        int statusCode = loginResponse.extract().statusCode();
        Boolean success = loginResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Проверка ошибки при авторизации с неверным паролем")
    public void checkUserWithIncorrectDataCantBeLoggedIn(){
        ValidatableResponse response = userClient.register(user);
        String getAccessToken = response.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(other_user));
        int statusCode = loginResponse.extract().statusCode();
        Boolean success = loginResponse.extract().path("success");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals(false, success);
    }

}
