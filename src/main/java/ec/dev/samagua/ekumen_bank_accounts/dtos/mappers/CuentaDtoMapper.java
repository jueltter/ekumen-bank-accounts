package ec.dev.samagua.ekumen_bank_accounts.dtos.mappers;

import ec.dev.samagua.ekumen_bank_accounts.dtos.CuentaDto;
import ec.dev.samagua.ekumen_bank_accounts.models.Cuenta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CuentaDtoMapper {
    CuentaDto entityToDto(Cuenta cuenta);
    Cuenta dtoToEntity(CuentaDto cuentaDto);
}
