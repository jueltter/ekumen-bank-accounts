package ec.dev.samagua.ekumen_bank_accounts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "cuenta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {


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




}
