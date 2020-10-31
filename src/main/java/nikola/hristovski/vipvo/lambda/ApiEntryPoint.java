package nikola.hristovski.vipvo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import nikola.hristovski.vipvo.lambda.model.AdditionRequest;
import nikola.hristovski.vipvo.lambda.model.AdditionResponse;
import nikola.hristovski.vipvo.lambda.model.FullResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

public class ApiEntryPoint implements RequestHandler<APIGatewayV2ProxyRequestEvent, FullResponse> {

    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public FullResponse handleRequest(APIGatewayV2ProxyRequestEvent event, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("[INFO] Started Java Handler for API Event the body is: " + event.getBody());

        String body = event.getBody();

        AdditionRequest request = null;

        try {
            request = mapper.readValue(new ByteArrayInputStream(body.getBytes()), AdditionRequest.class);
        } catch (IOException e) {

            return FullResponse.builder()
                    .isBase64Encoded(false)
                    .headers(new HashMap<>())
                    .multiValueHeaders(new HashMap<>())
                    .statusCode(500)
                    .body(
                            mapper.writeValueAsString(
                                    AdditionResponse.builder()
                                            .hasError(Boolean.TRUE)
                                            .errorMessage("FAILED TO PARSE BODY" + body)
                                            .build()
                            )
                    ).build();
        }

        if (request.getId() == null || request.getFirstNumber() == null || request.getSecondNumber() == null) {

            return FullResponse.builder()
                    .isBase64Encoded(false)
                    .headers(new HashMap<>())
                    .multiValueHeaders(new HashMap<>())
                    .statusCode(400)
                    .body(
                            mapper.writeValueAsString(
                                    AdditionResponse.builder()
                                            .id(request.getId())
                                            .hasError(Boolean.TRUE)
                                            .errorMessage("The request is not valid. Please make sure every value is present")
                                            .additionResult(null)
                                            .build()
                            )
                    ).build();

        }

        try {
            Long result = request.getFirstNumber() + request.getSecondNumber();

            return FullResponse.builder()
                    .isBase64Encoded(false)
                    .headers(new HashMap<>())
                    .multiValueHeaders(new HashMap<>())
                    .statusCode(200)
                    .body(
                            mapper.writeValueAsString(
                                    AdditionResponse.builder()
                                            .id(request.getId())
                                            .hasError(Boolean.FALSE)
                                            .errorMessage("")
                                            .additionResult(result)
                                            .build()
                            )
                    ).build();

        } catch (Exception ex) {
            logger.log("[ERROR] Failed to perform addition. Message: " + ex.getMessage());

            return FullResponse.builder()
                    .isBase64Encoded(false)
                    .headers(new HashMap<>())
                    .multiValueHeaders(new HashMap<>())
                    .statusCode(500)
                    .body(
                            mapper.writeValueAsString(
                                    AdditionResponse.builder()
                                            .id(request.getId())
                                            .hasError(Boolean.TRUE)
                                            .errorMessage(ex.getMessage())
                                            .additionResult(null)
                                            .build()
                            )
                    )
                    .build();

        }
    }
}
