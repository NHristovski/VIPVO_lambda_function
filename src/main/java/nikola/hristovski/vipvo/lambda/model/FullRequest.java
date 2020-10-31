package nikola.hristovski.vipvo.lambda.model;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FullRequest {
    private String resource;
    private String path;
    private String httpMethod;
    private Map<String, String> headers;
    private Map<String, List<String>> multiValueHeaders;
    private Map<String, String> queryStringParameters;
    private Map<String, List<String>> multiValueQueryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;
    private APIGatewayV2ProxyRequestEvent.RequestContext requestContext;
    private String body;
    private boolean isBase64Encoded = false;
}
