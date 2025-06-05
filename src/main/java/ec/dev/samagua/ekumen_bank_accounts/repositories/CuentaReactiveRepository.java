package ec.dev.samagua.ekumen_bank_accounts.repositories;

import ec.dev.samagua.ekumen_bank_accounts.entities.Cuenta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaReactiveRepository extends ReactiveCrudRepository<Cuenta, Long> {
    Mono<Long> countByNumeroCuenta(String numeroCuenta);
    Flux<Cuenta> findByClienteId(String clienteId);
    Mono<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
