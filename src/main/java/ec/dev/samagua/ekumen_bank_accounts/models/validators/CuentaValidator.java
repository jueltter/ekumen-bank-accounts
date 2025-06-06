package ec.dev.samagua.ekumen_bank_accounts.models.validators;

import ec.dev.samagua.commons_lib.models.DataValidationResult;
import ec.dev.samagua.commons_lib.models.IdentityFieldWrapper;
import ec.dev.samagua.ekumen_bank_accounts.infrastructure.clients.clients.ClienteServiceClient;
import ec.dev.samagua.ekumen_bank_accounts.models.Cuenta;
import ec.dev.samagua.ekumen_bank_accounts.repositories.impl.CuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class CuentaValidator {

    private static final List<String> TIPOS_CUENTA = List.of("AHORROS", "CORRIENTE");
    private static final List<String> ESTADOS = List.of("TRUE", "FALSE");

    private final CuentaRepository repository;
    private final ClienteServiceClient clienteServiceClient;

    public Mono<DataValidationResult> validateForCreating(Cuenta model) {
        var errorsMap = new ConcurrentHashMap<String, String>();

        return Mono.just(model).flatMap(cuenta -> {
                    if (cuenta.getNombreCliente() == null || cuenta.getNombreCliente().isBlank()) {
                        errorsMap.put("nombreCliente", "is mandatory");
                        return Mono.just(cuenta);
                    } else {
                        return clienteServiceClient.findByNombreOrClienteId(cuenta.getNombreCliente(), null).flatMap(clientes -> {
                            if (clientes.isEmpty()) errorsMap.put("nombreCliente", "is invalid");
                            return Mono.just(cuenta);
                        });
                    }
                })
                .flatMap(cuenta -> {
                    if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isBlank()) {
                        errorsMap.put("numeroCuenta", "is mandatory");
                        return Mono.just(cuenta);
                    } else {
                        return repository.countByNumeroCuenta(cuenta.getNumeroCuenta()).flatMap(count -> {
                            if (count > 0) errorsMap.put("numeroCuenta", "is already in use");
                            return Mono.just(cuenta);
                        });
                    }
                })
                .flatMap(cuenta -> {
                    // validate id
                    if (cuenta.getId() != null)
                        errorsMap.put("id", "must be null");

                    // validate account type
                    if (cuenta.getTipoCuenta() == null || !TIPOS_CUENTA.contains(cuenta.getTipoCuenta()))
                        errorsMap.put("tipoCuenta", "possible values are: " + TIPOS_CUENTA);

                    // validate initial balance
                    if (cuenta.getSaldoInicial() == null) {
                        errorsMap.put("saldoInicial", "is mandatory");
                    } else {
                        if (cuenta.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0)
                            errorsMap.put("saldoInicial", "must be greater than or equal to 0");
                    }

                    // validate account status
                    if (cuenta.getEstado() == null || !ESTADOS.contains(cuenta.getEstado()))
                        errorsMap.put("estado", "possible values are: " + ESTADOS);
                    log.info(errorsMap.toString());
                    return Mono.just(cuenta);
                })
                .then(Mono.just(errorsMap))
                .flatMap(errors -> Mono.just(DataValidationResult.from(errors)));
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
