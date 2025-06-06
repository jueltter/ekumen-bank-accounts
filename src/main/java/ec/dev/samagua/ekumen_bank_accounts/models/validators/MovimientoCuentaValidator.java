package ec.dev.samagua.ekumen_bank_accounts.models.validators;

import ec.dev.samagua.commons_lib.models.DataValidationResult;
import ec.dev.samagua.ekumen_bank_accounts.models.MovimientoCuenta;
import ec.dev.samagua.ekumen_bank_accounts.utils.BalanceUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MovimientoCuentaValidator {
    private static final List<String> TIPOS_MOVIMIENTO = List.of("RETIRO", "DEPOSITO");

    public DataValidationResult validateForCreating(MovimientoCuenta model) {
        Map<String, String> errors = new HashMap<>();

        // validate id
        if (model.getId() != null) {
            errors.put("id", "must be null");
        }

        // validate account movement type
        if (model.getTipoMovimiento() == null || !TIPOS_MOVIMIENTO.contains(model.getTipoMovimiento())) {
            errors.put("tipoMovimiento", "possible values are: " + TIPOS_MOVIMIENTO);
        }

        // validate value
        if (model.getValor() == null || model.getValor().compareTo(BigDecimal.ZERO) == 0) {
            errors.put("valor", "is mandatory and must be different than 0");
        }
        else {
            if (!BalanceUtils.canApply(model.getSaldoAnterior(), model.getValor())) {
                errors.put("valor", "balance not available");
            }
        }

        if (!errors.isEmpty()) {
            return DataValidationResult.builder()
                    .valid(Boolean.FALSE)
                    .errors(errors)
                    .build();

        }

        return DataValidationResult.builder()
                .valid(Boolean.TRUE)
                .errors(null)
                .build();
    }

    public DataValidationResult validateForUpdating(MovimientoCuenta model) {
        Map<String, String> errors = new HashMap<>();

        // validate account movement type
        if (model.getTipoMovimiento() == null || !TIPOS_MOVIMIENTO.contains(model.getTipoMovimiento())) {
            errors.put("tipoMovimiento", "possible values are: " + TIPOS_MOVIMIENTO);
        }

        // validate value
        if (model.getValor() == null || model.getValor().compareTo(BigDecimal.ZERO) == 0) {
            errors.put("valor", "is mandatory and must be different than 0");
        }
        else {
            if (!BalanceUtils.canApply(model.getSaldoAnterior(), model.getValor())) {
                errors.put("valor", "balance not available");
            }
        }

        if (!errors.isEmpty()) {
            return DataValidationResult.builder()
                    .valid(Boolean.FALSE)
                    .errors(errors)
                    .build();

        }

        return DataValidationResult.builder()
                .valid(Boolean.TRUE)
                .errors(null)
                .build();
    }

    public DataValidationResult validateForPatching(MovimientoCuenta model) {
        Map<String, String> errors = new HashMap<>();

        // validate account movement type
        if (model.getTipoMovimiento() != null && !TIPOS_MOVIMIENTO.contains(model.getTipoMovimiento())) {
            errors.put("tipoMovimiento", "possible values are: " + TIPOS_MOVIMIENTO);
        }

        // validate value
        if (model.getValor() != null) {
            if (model.getValor().compareTo(BigDecimal.ZERO) == 0) {
                errors.put("valor", "must be different than 0");
            }
            else {
                if (!BalanceUtils.canApply(model.getSaldoAnterior(), model.getValor())) {
                    errors.put("valor", "balance not available");

                }
            }
        }

        if (!errors.isEmpty()) {
            return DataValidationResult.builder()
                    .valid(Boolean.FALSE)
                    .errors(errors)
                    .build();

        }

        return DataValidationResult.builder()
                .valid(Boolean.TRUE)
                .errors(null)
                .build();
    }
}
