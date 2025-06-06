package ec.dev.samagua.ekumen_bank_accounts.repositories.impl;

import ec.dev.samagua.ekumen_bank_accounts.models.MovimientoCuenta;
import ec.dev.samagua.ekumen_bank_accounts.repositories.MovimientoCuentaReactiveRepository;
import ec.dev.samagua.commons_lib.exceptions.RepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MovimientoCuentaRepository {
    private final MovimientoCuentaReactiveRepository repository;

    public Mono<List<MovimientoCuenta>> findAll() {
        return repository.findAll()
                .collectList()
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(e -> log.error("Error finding all account movements",e));
    }

    public Mono<Long> countByCuenta(Long cuenta) {
        return repository.countByCuenta(cuenta)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(e -> log.error("Error counting account movements by account", e));
    }

    public Mono<List<MovimientoCuenta>> findByCuentaAndFechaBetween(Long cuenta, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        return repository.findByCuentaAndFechaBetween(cuenta, fechaInicial, fechaFinal)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(e -> log.error("Error finding account movements by account and date range", e))
                .collectList();
    }

    public Mono<MovimientoCuenta> create(MovimientoCuenta movimientoCuenta) {
        return repository.save(movimientoCuenta)
                .onErrorMap(RepositoryException::getCreateException)
                .doOnError(e -> log.error("Error creating movimiento cuenta", e));
    }

    public Mono<MovimientoCuenta> update(MovimientoCuenta movimientoCuenta) {
        return repository.save(movimientoCuenta)
                .onErrorMap(RepositoryException::getUpdateException)
                .doOnError(e -> log.error("Error updating movimiento cuenta", e));
    }

    public Mono<Void> delete(MovimientoCuenta movimientoCuenta) {
        return repository.delete(movimientoCuenta)
                .onErrorMap(RepositoryException::getDeleteException)
                .doOnError(e -> log.error("Error deleting movimiento cuenta", e));
    }

    public Mono<List<MovimientoCuenta>> findByCuenta(Long cuenta) {
        return repository.findByCuenta(cuenta)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(e -> log.error("Error finding account movements by account", e))
                .collectList()
                //.defaultIfEmpty(Collections.emptyList())
                .doOnSuccess(obj -> log.debug("findByCuenta retorno: {}", obj));

    }

    public Mono<MovimientoCuenta> findById(Long id) {
        return repository.findById(id)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(e -> log.error("Error finding account movement by ID", e))
                .defaultIfEmpty(MovimientoCuenta.getDefaultInstance());
    }

}
