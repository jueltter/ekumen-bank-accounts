package ec.dev.samagua.ekumen_bank_accounts.dtos.mappers;

import ec.dev.samagua.ekumen_bank_accounts.dtos.MovimientoCuentaDto;
import ec.dev.samagua.ekumen_bank_accounts.models.MovimientoCuenta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovimientoCuentaDtoMapper {
    MovimientoCuentaDto entityToDto(MovimientoCuenta entity);

    MovimientoCuenta dtoToEntity(MovimientoCuentaDto dto);
}
