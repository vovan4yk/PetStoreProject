package pet.store.util;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;

import static java.lang.Thread.sleep;
import static java.util.Objects.isNull;

public class ApiUtil {

    @SneakyThrows
    public static Response requestWithRetry(Method method, RequestSpecification requestSpecification) {
        Response response;
        var internalTries = 3;

        do {
            if (internalTries != 3) {
                sleep(200);
            }

            response = requestSpecification.request(method);

            internalTries--;
        }
        while (internalTries >= 0 && (isNull(response) || response.getStatusCode() != 200));

        return response;
    }
}
