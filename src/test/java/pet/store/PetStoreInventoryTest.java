package pet.store;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pet.store.util.spec.PetStoreSpec.getRequestPetStoreInventorySpec;

@Tag("storeRegression")
public class PetStoreInventoryTest {

    @Test
    public void verifyGetInventoryReturnsMapOfStatusCodesToQuantities() {
        var response = getRequestPetStoreInventorySpec()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .response();

        var jsonMap = response.jsonPath().getMap("$");

        assertNotNull(jsonMap, "Response JSON is null");
        assertFalse(jsonMap.isEmpty(), "Response JSON is empty");

        jsonMap.forEach((key, value) -> {
            assertNotNull(key, "Key is null");
            assertFalse(key.toString().trim().isEmpty(), "Key is empty");

            assertNotNull(value, "Value for key '" + key + "' is null");
            assertFalse(value.toString().trim().isEmpty(), "Value for key '" + key + "' is empty");
        });
    }
}
