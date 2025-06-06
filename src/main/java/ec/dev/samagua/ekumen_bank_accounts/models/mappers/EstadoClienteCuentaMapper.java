package ec.dev.samagua.ekumen_bank_accounts.models.mappers;

import ec.dev.samagua.ekumen_bank_accounts.models.Cuenta;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoClienteCuenta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoClienteCuentaMapper {
    EstadoClienteCuenta entityToModel(Cuenta cuenta);
    Cuenta modelToEntity(EstadoClienteCuenta estadoClienteCuenta);
}
