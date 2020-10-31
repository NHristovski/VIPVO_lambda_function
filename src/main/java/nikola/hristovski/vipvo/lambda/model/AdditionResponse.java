package nikola.hristovski.vipvo.lambda.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("has_error")
    private Boolean hasError;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("addition_result")
    private Long additionResult;
}

