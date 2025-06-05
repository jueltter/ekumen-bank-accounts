package ec.dev.samagua.ekumen_bank_accounts.dtos_mappers;

import ec.dev.samagua.ekumen_bank_accounts.dtos.EstadoClienteCuentaMovimientoDto;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoClienteCuentaMovimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EstadoClienteCuentaMovimientoDtoMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "cuenta")
    @Mapping(ignore = true, target = "tipoMovimiento")
    EstadoClienteCuentaMovimientoDto modelToDto(EstadoClienteCuentaMovimiento model);
    EstadoClienteCuentaMovimiento dtoToModel(EstadoClienteCuentaMovimientoDto dto);
}
