package ec.dev.samagua.ekumen_bank_accounts.services;

import ec.dev.samagua.ekumen_bank_accounts.models.Cuenta;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CuentaService {
    Mono<List<Cuenta>> search(String clienteId);
    Mono<Cuenta> create(Cuenta cuenta);
    Mono<Cuenta> update(Long id, Cuenta newData);
    Mono<Cuenta> patch(Long id, Cuenta newData);
    Mono<Void> delete(Long id);

}
