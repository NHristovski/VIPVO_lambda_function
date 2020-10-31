package nikola.hristovski.vipvo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikola.hristovski.vipvo.lambda.model.AdditionRequest;
import nikola.hristovski.vipvo.lambda.model.AdditionResponse;
import nikola.hristovski.vipvo.lambda.model.FullRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EntryPoint implements RequestStreamHandler {

    private ObjectMapper mapper = new ObjectMapper();

    public void handleRequest(final InputStream inputStream, final OutputStream outputStream,
                              final Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        logger.log("[INFO] Loading Java Lambda handler for addition request");

        FullRequest fullRequest = mapper.readValue(inputStream, FullRequest.class);

        logger.log("[REQUEST BODY] " + fullRequest.getBody());

        AdditionRequest request = mapper.readValue(
                new ByteArrayInputStream(fullRequest.getBody().getBytes())
                , AdditionRequest.class);

        AdditionResponse response;

        if (request.getId() == null || request.getFirstNumber() == null || request.getSecondNumber() == null) {
            response =
                    AdditionResponse.builder()
                            .id(request.getId())
                            .hasError(Boolean.TRUE)
                            .errorMessage("The request is not valid. Please make sure every value is present")
                            .additionResult(null).build();

            sendResponse(response, outputStream);
        }

        try {
            Long result = request.getFirstNumber() + request.getSecondNumber();

            response =
                    AdditionResponse.builder()
                            .id(request.getId())
                            .hasError(Boolean.FALSE)
                            .errorMessage("")
                            .additionResult(result).build();

            sendResponse(response, outputStream);
        } catch (Exception ex) {
            logger.log("[ERROR] Failed to perform addition. Message: " + ex.getMessage());

            response =
                    AdditionResponse.builder()
                            .id(request.getId())
                            .hasError(Boolean.TRUE)
                            .errorMessage(ex.getMessage())
                            .additionResult(null).build();

            sendResponse(response, outputStream);

        }

    }

    private void sendResponse(AdditionResponse response, OutputStream outputStream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        writer.write(mapper.writeValueAsString(response));
        writer.close();
    }

}
