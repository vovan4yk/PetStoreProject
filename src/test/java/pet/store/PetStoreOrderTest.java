package pet.store;

import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pet.store.model.data.OrderData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.GET;
import static java.time.OffsetDateTime.parse;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static pet.store.util.ApiUtil.requestWithRetry;
import static pet.store.util.spec.PetStoreSpec.*;

@Tag("storeRegression")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetStoreOrderTest {

    private static List<OrderData> orders = new ArrayList<>(List.of(
            OrderData.builder()
                    .shipDate(parse("2025-08-26T11:57:41.759Z"))
                    .status("placed")
                    .complete(true)
                    .build(),
            OrderData.builder()
                    .id(getRandomInt(4))
                    .petId(getRandomInt(4))
                    .quantity(getRandomInt(3).intValue())
                    .shipDate(parse("2025-10-27T11:57:41.759Z"))
                    .status("sent")
                    .complete(true)
                    .build(),
            OrderData.builder()
                    .id(getRandomInt(4))
                    .petId(getRandomInt(4))
                    .quantity(getRandomInt(3).intValue())
                    .shipDate(parse("2025-10-27T11:57:41.759Z"))
                    .status("done")
                    .complete(false)
                    .build()
    ));

    private static final String ORDER_NOT_FOUND = "Order not found";

    static Stream<OrderData> validOrderProvider() {
        return orders.stream();
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("validOrderProvider")
    public void verifyPostValidOrder(OrderData order) {
        var response = postOrderRequest(order)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(OrderData.class);

        if (order.getId() > 0) {
            assertThat(response)
                    .isEqualTo(order);
        } else {

            assertThat(response)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(order);

            order.setId(response.getId());
        }
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource("validOrderProvider")
    public void verifyGetValidOrder(OrderData order) {
        var reps = requestWithRetry(GET, getOrderRequest(order.getId()))
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(OrderData.class);

        assertThat(reps)
                .isEqualTo(order);
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource("validOrderProvider")
    public void verifyDeleteValidOrder(OrderData order) {
        requestMethodWithRetry(DELETE, order.getId(), SC_OK, String.valueOf(order.getId()));
        requestMethodWithRetry(GET, order.getId(), SC_NOT_FOUND, ORDER_NOT_FOUND);
    }

    @Test
    public void verifyDeleteOrderWithInvalidId() {
        requestMethodWithRetry(DELETE, getRandomInt(5), SC_NOT_FOUND, ORDER_NOT_FOUND);
    }

    @Test
    public void verifyGetOrderWithInvalidId() {
        requestMethodWithRetry(GET, getRandomInt(6), SC_NOT_FOUND, ORDER_NOT_FOUND);
    }

    private void requestMethodWithRetry(Method method, Long orderId, int status, String expectedMessage) {
        requestWithRetry(method, getOrderRequest(orderId))
                .then()
                .statusCode(status)
                .body("message", containsStringIgnoringCase(expectedMessage));
    }

    private static Long getRandomInt(int size) {
        return Long.parseLong(RandomStringUtils.secure().next(size, false, true));
    }
}
