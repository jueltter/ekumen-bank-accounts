package ec.dev.samagua.ekumen_bank_accounts.dtos_mappers;

import ec.dev.samagua.ekumen_bank_accounts.dtos.CuentaDto;
import ec.dev.samagua.ekumen_bank_accounts.entities.Cuenta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CuentaDtoMapper {
    CuentaDto entityToDto(Cuenta cuenta);
    Cuenta dtoToEntity(CuentaDto cuentaDto);
}
