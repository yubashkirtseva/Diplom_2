import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class GettingTheUsersOrderTest {
    private User user;
    private  UserClient userClient;
    private OrderClient orderClient;
    private String bearerToken;

    @Before
    public void setUp(){
        user = UserGeneration.getDefault();
        userClient = new UserClient();
        orderClient = new OrderClient();

    }

    @After
    public void cleanUp(){
        userClient.delete(bearerToken);
    }

    @Test
    @DisplayName("Проверка успешного получения списка заказов авторизованного пользователя")
    public void checkGetOrdersWithAuth(){
        ValidatableResponse registerResponse = userClient.register(user);
        String getAccessToken = registerResponse.extract().path("accessToken");
        bearerToken = String.valueOf(getAccessToken);
        ValidatableResponse orderResponse = orderClient.getOrders(bearerToken);
        int statusCode = orderResponse.extract().statusCode();
        Boolean success = orderResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Проверка ошибки при получении списка заказов неавторизованным пользователем")
    public void checkGetOrdersWithoutAuth(){
        bearerToken = "";
        ValidatableResponse orderResponse = orderClient.getOrders(bearerToken);
        int statusCode = orderResponse.extract().statusCode();
        String message = orderResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals("You should be authorised", message);
    }
}
