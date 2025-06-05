package ec.dev.samagua.ekumen_bank_accounts.infrastructure.clients.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
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
}
