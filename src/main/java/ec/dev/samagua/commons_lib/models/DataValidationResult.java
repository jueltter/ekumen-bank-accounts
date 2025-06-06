package ec.dev.samagua.commons_lib.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataValidationResult {
    private boolean valid;
    private Map<String, String> errors;

    public static DataValidationResult from(Map<String, String> errors) {
        return DataValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .build();
    }
}
