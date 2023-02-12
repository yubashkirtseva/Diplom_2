import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class UserDataChangeTest {
    private User user;
    private User update_user;
    private  UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGeneration.getDefault();
        update_user = UserGeneration.getUpdateDate();
        userClient = new UserClient();
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
    @DisplayName("Проверка успешного изменения данных авторизованного пользователя")
    public void checkUsersDataCanBeChanged(){

        ValidatableResponse changeResponse = userClient.change(update_user,bearerToken);
        int statusCode = changeResponse.extract().statusCode();
        Boolean success = changeResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Проверка ошибки при изменении данных пользователя без авторизации")
    public void checkUsersDataCantBeChangedWithoutLogin(){

        ValidatableResponse changeResponse = userClient.changeWithoutToken(user);
        int statusCode = changeResponse.extract().statusCode();
        Boolean success = changeResponse.extract().path("success");
        String message = changeResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals(false, success);
        assertEquals("You should be authorised", message);


    }
}
