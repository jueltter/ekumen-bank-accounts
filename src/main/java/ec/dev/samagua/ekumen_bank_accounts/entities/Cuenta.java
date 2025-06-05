package ec.dev.samagua.ekumen_bank_accounts.entities;

import ec.dev.samagua.ekumen_bank_accounts.utils_models.DataValidationResult;
import ec.dev.samagua.ekumen_bank_accounts.utils_models.IdentityFieldWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table(name = "cuenta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {
    private static final List<String> TIPOS_CUENTA = List.of("AHORROS", "CORRIENTE");
    private static final List<String> ESTADOS = List.of("TRUE", "FALSE");

    @Id
    @Column(value= "id")
    private Long id;

    @Column(value= "numero_cuenta")
    private String numeroCuenta;

    @Column(value= "tipo_cuenta")
    private String tipoCuenta;

    @Column(value= "saldo_inicial")
    private BigDecimal saldoInicial;

    @Column(value= "estado")
    private String estado;

    @Column(value= "cliente_id")
    private String clienteId;

    @Transient
    private String nombreCliente;

    public static Cuenta getDefaultInstance() {
        return Cuenta.builder()
                .id(-1L)
                .build();
    }

    public boolean isValidId() {
        return getId() != null && getId() > 0;
    }

    public DataValidationResult validateForCreating(Long countNumeroCuenta) {
        Map<String, String> errors = new HashMap<>();

        // validate id
        if (this.id != null) {
            errors.put("id", "must be null");
        }

        // validate account number
        if (countNumeroCuenta > 0) {
            errors.put("numeroCuenta", "is already in use");
        }
        else {
            if (this.numeroCuenta == null || this.numeroCuenta.isBlank()) {
                errors.put("numeroCuenta", "is mandatory");
            }
        }

        // validate account type
        if (this.tipoCuenta == null || !TIPOS_CUENTA.contains(this.tipoCuenta)) {
            errors.put("tipoCuenta", "possible values are: " + TIPOS_CUENTA);
        }

        // validate initial balance
        if (this.saldoInicial == null) {
            errors.put("saldoInicial", "is mandatory");
        }
        else {
            if (this.saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
                errors.put("saldoInicial", "must be greater than or equal to 0");
            }
        }

        // validate account status
        if (this.estado == null || !ESTADOS.contains(this.estado)) {
            errors.put("estado", "possible values are: " + ESTADOS);
        }

        // validate client id
        if (this.clienteId == null || this.clienteId.isBlank()) {
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

    public DataValidationResult validateForUpdating(IdentityFieldWrapper numeroCuentaWrapper) {
        Map<String, String> errors = new HashMap<>();

        // validate account number
        if (!numeroCuentaWrapper.noChange() && numeroCuentaWrapper.count() > 0) {
            errors.put("numeroCuenta", "is already in use");
        }

        // validate account type
        if (this.tipoCuenta == null || !TIPOS_CUENTA.contains(this.tipoCuenta)) {
            errors.put("tipoCuenta", "possible values are: " + TIPOS_CUENTA);
        }

        // validate initial balance
        if (this.saldoInicial == null) {
            errors.put("saldoInicial", "is mandatory");
        }
        else {
            if (this.saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
                errors.put("saldoInicial", "must be greater than or equal to 0");
            }
        }

        // validate account status
        if (this.estado == null || !ESTADOS.contains(this.estado)) {
            errors.put("estado", "possible values are: " + ESTADOS);
        }

        // validate client id
        if (this.clienteId == null || this.clienteId.isBlank()) {
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

    public DataValidationResult validateForPatching(IdentityFieldWrapper numeroCuentaWrapper) {
        Map<String, String> errors = new HashMap<>();

        // validate account number
        if (this.numeroCuenta !=null && !numeroCuentaWrapper.noChange() && numeroCuentaWrapper.count() > 0) {
            errors.put("numeroCuenta", "is already in use");
        }

        // validate account type
        if (this.tipoCuenta != null && !TIPOS_CUENTA.contains(this.tipoCuenta)) {
            errors.put("tipoCuenta", "possible values are: " + TIPOS_CUENTA);
        }

        // validate initial balance
        if (this.saldoInicial != null && this.saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            errors.put("saldoInicial", "must be greater than or equal to 0");
        }

        // validate account status
        if (this.estado != null && !ESTADOS.contains(this.estado)) {
            errors.put("estado", "possible values are: " + ESTADOS);
        }

        // validate client id
        if (this.clienteId != null && this.clienteId.isBlank()) {
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

    public DataValidationResult validateForDeleting(Long countMovimientoCuenta) {
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
