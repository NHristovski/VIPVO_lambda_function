package nikola.hristovski.vipvo.lambda.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdditionRequest {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_number")
    private Long firstNumber;

    @JsonProperty("second_number")
    private Long secondNumber;
}
