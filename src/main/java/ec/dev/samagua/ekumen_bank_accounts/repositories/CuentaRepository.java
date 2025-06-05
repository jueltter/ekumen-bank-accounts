package ec.dev.samagua.ekumen_bank_accounts.repositories;

import ec.dev.samagua.ekumen_bank_accounts.entities.Cuenta;
import ec.dev.samagua.ekumen_bank_accounts.utils_exceptions.RepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CuentaRepository {

    private final CuentaReactiveRepository repository;

    public Mono<List<Cuenta>> findAll() {
        return repository.findAll().collectList();
    }

    public Mono<Cuenta> findById(Long id) {
        return repository.findById(id)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(error -> log.error("Error finding account", error))
                .defaultIfEmpty(Cuenta.getDefaultInstance());
    }

    public Mono<List<Cuenta>> findbyClienteId(String clienteId) {
        return repository.findByClienteId(clienteId)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(error -> log.error("Error finding accounts by client id", error))
                .collectList();
    }

    public Mono<Cuenta> create(Cuenta cuenta) {
        return repository.save(cuenta)
                .onErrorMap(RepositoryException::getCreateException)
                .doOnError(e -> log.error("Error creating cuenta", e));
    }

    public Mono<Long> countByClienteId(String numeroCuenta) {
        return repository.countByNumeroCuenta(numeroCuenta)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(e -> log.error("Error counting cuentas by numeroCuenta", e));
    }

    public Mono<Cuenta> update(Cuenta cuenta) {
        return repository.save(cuenta)
                .onErrorMap(RepositoryException::getUpdateException)
                .doOnError(e -> log.error("Error updating cuenta", e));
    }

    public Mono<Void> delete(Cuenta cuenta) {
        return repository.delete(cuenta)
                .onErrorMap(RepositoryException::getDeleteException)
                .doOnError(e -> log.error("Error deleting cuenta", e));
    }

    public Mono<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return repository.findByNumeroCuenta(numeroCuenta)
                .onErrorMap(RepositoryException::getReadException)
                .doOnError(e -> log.error("Error finding cuenta by numeroCuenta", e))
                .defaultIfEmpty(Cuenta.getDefaultInstance());
    }

}
