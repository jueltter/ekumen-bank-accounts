package ec.dev.samagua.ekumen_bank_accounts.models;

import ec.dev.samagua.ekumen_bank_accounts.utils.BalanceUtils;
import ec.dev.samagua.commons_lib.models.DataValidationResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table(name = "movimiento_cuenta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoCuenta {
    private static final List<String> TIPOS_MOVIMIENTO = List.of("RETIRO", "DEPOSITO");

    @Id
    @Column(value= "id")
    private Long id;

    @Column(value= "fecha")
    private LocalDateTime fecha;

    @Column(value= "tipo_movimiento")
    private String tipoMovimiento;

    @Column(value= "valor")
    private BigDecimal valor;

    @Column(value= "saldo")
    private BigDecimal saldo;

    @Column(value= "cuenta")
    private Long cuenta;

    @Column(value= "saldo_anterior")
    private BigDecimal saldoAnterior;

    @Transient
    private String numeroCuenta;

    public static MovimientoCuenta getDefaultInstance() {
        return MovimientoCuenta.builder()
                .id(-1L)
                .build();
    }

    public boolean isValidId() {
        return getId() != null && getId() > 0;
    }


    public DataValidationResult validateForCreating() {
        Map<String, String> errors = new HashMap<>();

        // validate id
        if (this.id != null) {
            errors.put("id", "must be null");
        }

        // validate account movement type
        if (this.getTipoMovimiento() == null || !TIPOS_MOVIMIENTO.contains(this.getTipoMovimiento())) {
            errors.put("tipoMovimiento", "possible values are: " + TIPOS_MOVIMIENTO);
        }

        // validate value
        if (this.getValor() == null || this.getValor().compareTo(BigDecimal.ZERO) == 0) {
            errors.put("valor", "is mandatory and must be different than 0");
        }
        else {
            if (!BalanceUtils.canApply(this.getSaldoAnterior(), this.getValor())) {
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

    public DataValidationResult validateForUpdating() {
        Map<String, String> errors = new HashMap<>();

        // validate account movement type
        if (this.getTipoMovimiento() == null || !TIPOS_MOVIMIENTO.contains(this.getTipoMovimiento())) {
            errors.put("tipoMovimiento", "possible values are: " + TIPOS_MOVIMIENTO);
        }

        // validate value
        if (this.getValor() == null || this.getValor().compareTo(BigDecimal.ZERO) == 0) {
            errors.put("valor", "is mandatory and must be different than 0");
        }
        else {
            if (!BalanceUtils.canApply(this.getSaldoAnterior(), this.getValor())) {
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

    public DataValidationResult validateForPatching() {
        Map<String, String> errors = new HashMap<>();

        // validate account movement type
        if (this.getTipoMovimiento() != null && !TIPOS_MOVIMIENTO.contains(this.getTipoMovimiento())) {
            errors.put("tipoMovimiento", "possible values are: " + TIPOS_MOVIMIENTO);
        }

        // validate value
        if (this.getValor() != null) {
            if (this.getValor().compareTo(BigDecimal.ZERO) == 0) {
                errors.put("valor", "must be different than 0");
            }
            else {
                if (!BalanceUtils.canApply(this.getSaldoAnterior(), this.getValor())) {
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
