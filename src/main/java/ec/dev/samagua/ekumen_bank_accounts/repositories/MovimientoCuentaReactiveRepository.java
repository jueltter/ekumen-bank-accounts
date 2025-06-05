package ec.dev.samagua.ekumen_bank_accounts.repositories;

import ec.dev.samagua.ekumen_bank_accounts.entities.MovimientoCuenta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface MovimientoCuentaReactiveRepository extends ReactiveCrudRepository<MovimientoCuenta, Long> {
    Mono<Long> countByCuenta(Long cuenta);
    Flux<MovimientoCuenta> findByCuentaAndFechaBetween(Long cuenta, LocalDateTime fechaInicial, LocalDateTime fechaFinal);
    Flux<MovimientoCuenta> findByCuenta(Long cuenta);
}
