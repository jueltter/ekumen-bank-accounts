package ec.dev.samagua.ekumen_bank_accounts.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoClienteCuentaMovimientoDto {
    private Long id;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private Long cuenta;
    private BigDecimal saldoAnterior;

    private String numeroCuenta;
    private String nombreCliente;
    private String tipoCuenta;
    private String estadoCuenta;
}
