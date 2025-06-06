package ec.dev.samagua.ekumen_bank_accounts.models.validators;

import ec.dev.samagua.commons_lib.models.DataValidationResult;
import ec.dev.samagua.commons_lib.models.IdentityFieldWrapper;
import ec.dev.samagua.ekumen_bank_accounts.models.Cuenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CuentaValidator {

    private static final List<String> TIPOS_CUENTA = List.of("AHORROS", "CORRIENTE");
    private static final List<String> ESTADOS = List.of("TRUE", "FALSE");

    public DataValidationResult validateForCreating(Cuenta model, Long countNumeroCuenta) {
        Map<String, String> errors = new HashMap<>();

        // validate id
        if (model.getId() != null) {
            errors.put("id", "must be null");
        }

        // validate account number
        if (countNumeroCuenta > 0) {
            errors.put("numeroCuenta", "is already in use");
        }
        else {
            if (model.getNumeroCuenta() == null || model.getNumeroCuenta().isBlank()) {
                errors.put("numeroCuenta", "is mandatory");
            }
        }

        // validate account type
        if (model.getTipoCuenta() == null || !TIPOS_CUENTA.contains(model.getTipoCuenta())) {
            errors.put("tipoCuenta", "possible values are: " + TIPOS_CUENTA);
        }

        // validate initial balance
        if (model.getSaldoInicial() == null) {
            errors.put("saldoInicial", "is mandatory");
        }
        else {
            if (model.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
                errors.put("saldoInicial", "must be greater than or equal to 0");
            }
        }

        // validate account status
        if (model.getEstado() == null || !ESTADOS.contains(model.getEstado())) {
            errors.put("estado", "possible values are: " + ESTADOS);
        }

        // validate client id
        if (model.getClienteId() == null || model.getClienteId().isBlank()) {
            errors.put("clienteId", "is mandatory");
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

    public DataValidationResult validateForUpdating(Cuenta model, IdentityFieldWrapper numeroCuentaWrapper) {
        Map<String, String> errors = new HashMap<>();

        // validate account number
        if (!numeroCuentaWrapper.noChange() && numeroCuentaWrapper.count() > 0) {
            errors.put("numeroCuenta", "is already in use");
        }

        // validate account type
        if (model.getTipoCuenta() == null || !TIPOS_CUENTA.contains(model.getTipoCuenta())) {
            errors.put("tipoCuenta", "possible values are: " + TIPOS_CUENTA);
        }

        // validate initial balance
        if (model.getSaldoInicial() == null) {
            errors.put("saldoInicial", "is mandatory");
        }
        else {
            if (model.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
                errors.put("saldoInicial", "must be greater than or equal to 0");
            }
        }

        // validate account status
        if (model.getEstado() == null || !ESTADOS.contains(model.getEstado())) {
            errors.put("estado", "possible values are: " + ESTADOS);
        }

        // validate client id
        if (model.getClienteId() == null || model.getClienteId().isBlank()) {
            errors.put("clienteId", "is mandatory");
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

    public DataValidationResult validateForPatching(Cuenta model, IdentityFieldWrapper numeroCuentaWrapper) {
        Map<String, String> errors = new HashMap<>();

        // validate account number
        if (model.getNumeroCuenta() !=null && !numeroCuentaWrapper.noChange() && numeroCuentaWrapper.count() > 0) {
            errors.put("numeroCuenta", "is already in use");
        }

        // validate account type
        if (model.getTipoCuenta() != null && !TIPOS_CUENTA.contains(model.getTipoCuenta())) {
            errors.put("tipoCuenta", "possible values are: " + TIPOS_CUENTA);
        }

        // validate initial balance
        if (model.getSaldoInicial() != null && model.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
            errors.put("saldoInicial", "must be greater than or equal to 0");
        }

        // validate account status
        if (model.getEstado() != null && !ESTADOS.contains(model.getEstado())) {
            errors.put("estado", "possible values are: " + ESTADOS);
        }

        // validate client id
        if (model.getClienteId() != null && model.getClienteId().isBlank()) {
            errors.put("clienteId", "is mandatory");
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

    public DataValidationResult validateForDeleting(Cuenta model, Long countMovimientoCuenta) {
        Map<String, String> errors = new HashMap<>();

        // validate account movements
        if (countMovimientoCuenta > 0) {
            errors.put("cuenta", "has account movements");
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
