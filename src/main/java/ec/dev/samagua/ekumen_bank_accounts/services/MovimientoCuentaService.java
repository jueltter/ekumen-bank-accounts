package ec.dev.samagua.ekumen_bank_accounts.services;

import ec.dev.samagua.ekumen_bank_accounts.entities.MovimientoCuenta;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MovimientoCuentaService {
    Mono<List<MovimientoCuenta>> findAll();

    Mono<MovimientoCuenta> create(MovimientoCuenta movimientoCuenta);
    Mono<MovimientoCuenta> update(Long id, MovimientoCuenta newData);
    Mono<MovimientoCuenta> patch(Long id, MovimientoCuenta newData);
    Mono<Void> delete(Long id);
}
