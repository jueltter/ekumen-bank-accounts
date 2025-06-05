package ec.dev.samagua.ekumen_bank_accounts.utils_models;

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
}
