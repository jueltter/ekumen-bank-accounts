package ec.dev.samagua.ekumen_bank_accounts.models_mappers;

import ec.dev.samagua.ekumen_bank_accounts.infrastructure.clients.models.Cliente;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoCliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoClienteMapper {
    EstadoCliente entityToModel(Cliente cliente);

    Cliente modelToEntity(EstadoCliente estadoCliente);
}
