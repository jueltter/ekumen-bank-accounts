package ec.dev.samagua.ekumen_bank_accounts.models_mappers;

import ec.dev.samagua.ekumen_bank_accounts.entities.Cuenta;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoClienteCuenta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoClienteCuentaMapper {
    EstadoClienteCuenta entityToModel(Cuenta cuenta);
    Cuenta modelToEntity(EstadoClienteCuenta estadoClienteCuenta);
}
