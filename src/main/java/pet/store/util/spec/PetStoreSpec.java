package pet.store.util.spec;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pet.store.model.data.OrderData;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static pet.store.util.ConfigUtil.getPetStoreUrl;

public class PetStoreSpec {

    public static RequestSpecification getRequestPetStoreSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(getPetStoreUrl() + "store/")
                .setContentType(JSON)
                .build();
    }

    public static RequestSpecification getRequestPetStoreInventorySpec() {
        return given()
                .spec(getRequestPetStoreSpec())
                .basePath("inventory");
    }

    public static RequestSpecification getRequestPetStoreOrderSpec() {
        return given()
                .spec(getRequestPetStoreSpec())
                .basePath("order");
    }

    public static Response postOrderRequest(OrderData order) {
        return getRequestPetStoreOrderSpec()
                .body(order)
                .post();
    }

    public static RequestSpecification getOrderRequest(Long orderId) {
        return given()
                .spec(getRequestPetStoreOrderSpec())
                .basePath("order/" + orderId);
    }
}
