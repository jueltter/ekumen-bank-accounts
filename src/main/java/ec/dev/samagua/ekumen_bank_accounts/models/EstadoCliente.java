package ec.dev.samagua.ekumen_bank_accounts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoCliente {
    private Long id;
    private String nombre;
    private String genero;
    private LocalDate fechaNacimiento;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String clienteId;
    private String clave;
    private String estado;
    private Long edad;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private List<EstadoClienteCuenta> cuentas;

    /**
     * Adds a new cuenta to the list of cuentas.
     * @param cuenta cuenta
     */
    public void addCuenta(EstadoClienteCuenta cuenta) {
        if (cuentas == null) {
            cuentas = new ArrayList<>();
        }
        cuentas.add(cuenta);
    }

    /**
     * Adds a list of movimientos to a specific cuenta.
     * @param idCuenta id cuenta
     * @param movimientos movimientos
     */
    public void addMovimientosCuenta(Long idCuenta, List<EstadoClienteCuentaMovimiento> movimientos) {
        Stream.ofNullable(cuentas)
                .flatMap(obj -> cuentas.stream())
                .filter(obj -> obj.getId().equals(idCuenta))
                .findFirst()
                .ifPresent(cuenta -> cuenta.setMovimientos(movimientos));
    }
}
