package ec.dev.samagua.ekumen_bank_accounts.services.impl;

import ec.dev.samagua.ekumen_bank_accounts.models.MovimientoCuenta;
import ec.dev.samagua.ekumen_bank_accounts.repositories.impl.CuentaRepository;
import ec.dev.samagua.ekumen_bank_accounts.repositories.impl.MovimientoCuentaRepository;
import ec.dev.samagua.ekumen_bank_accounts.services.MovimientoCuentaService;
import ec.dev.samagua.ekumen_bank_accounts.utils.BalanceUtils;
import ec.dev.samagua.ekumen_bank_accounts.utils.BeanCopyUtil;
import ec.dev.samagua.commons_lib.exceptions.InvalidDataException;
import ec.dev.samagua.commons_lib.models.DataValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimientoCuentaServiceImpl implements MovimientoCuentaService {

    private final MovimientoCuentaRepository repository;
    private final CuentaRepository cuentaRepository;

    @Override
    public Mono<List<MovimientoCuenta>> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<MovimientoCuenta> create(MovimientoCuenta movimientoCuenta) {
        return cuentaRepository.findByNumeroCuenta(movimientoCuenta.getNumeroCuenta()).flatMap(
                cuenta -> {

                    if (!cuenta.isValidId()) {
                        return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("numeroCuenta", "is invalid")));
                    }

                    movimientoCuenta.setCuenta(cuenta.getId());
                    movimientoCuenta.setSaldoAnterior(cuenta.getSaldoInicial());
                    return repository.findByCuenta(cuenta.getId());
                }
        ).flatMap(movimientos -> {
            movimientos.sort(Comparator.comparing(MovimientoCuenta::getFecha).reversed());
            movimientos.stream().findFirst().ifPresent(ultimoMovimiento -> movimientoCuenta.setSaldoAnterior(ultimoMovimiento.getSaldo()));

            DataValidationResult validationResult = movimientoCuenta.validateForCreating();

            if (!validationResult.isValid()) {
                return Mono.error(InvalidDataException.getInstance(validationResult.getErrors()));
            }

            BigDecimal balance = BalanceUtils.apply(movimientoCuenta.getSaldoAnterior(), movimientoCuenta.getValor());

            movimientoCuenta.setFecha(LocalDateTime.now());
            movimientoCuenta.setSaldo(balance);

            return repository.create(movimientoCuenta);
        });
    }

    @Override
    public Mono<MovimientoCuenta> update(Long id, MovimientoCuenta newData) {
        return repository.findById(id).flatMap(movimientoCuenta -> {
            if (!movimientoCuenta.isValidId()) {
                return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("id", "is invalid")));
            }

            return cuentaRepository.findById(movimientoCuenta.getCuenta());

        }).flatMap(
                cuenta -> {
                    if (!cuenta.getNumeroCuenta().equals(newData.getNumeroCuenta())) {
                        return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("numeroCuenta", "is mandatory and cannot be changed")));
                    }
                    return repository.findByCuenta(cuenta.getId());
                }
        ).flatMap(movimientos -> {
            movimientos.sort(Comparator.comparing(MovimientoCuenta::getFecha).reversed());
            MovimientoCuenta ultimoMovimiento = movimientos.stream().findFirst().get();

            if (!ultimoMovimiento.getId().equals(id)) {
                return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("account movement", "is not the last one")));
            }

            newData.setSaldoAnterior(ultimoMovimiento.getSaldoAnterior());

            DataValidationResult validationResult = newData.validateForUpdating();

            if (!validationResult.isValid()) {
                return Mono.error(InvalidDataException.getInstance(validationResult.getErrors()));
            }

            BigDecimal balance = BalanceUtils.apply(newData.getSaldoAnterior(), newData.getValor());

            newData.setFecha(LocalDateTime.now());
            newData.setSaldo(balance);

            BeanCopyUtil.copyNonNullProperties(newData, ultimoMovimiento);

            return repository.update(ultimoMovimiento);
        });
    }

    @Override
    public Mono<MovimientoCuenta> patch(Long id, MovimientoCuenta newData) {
        return repository.findById(id).flatMap(movimientoCuenta -> {
            if (!movimientoCuenta.isValidId()) {
                return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("id", "is invalid")));
            }

            return cuentaRepository.findById(movimientoCuenta.getCuenta());

        }).flatMap(
                cuenta -> {
                    if (newData.getNumeroCuenta() != null && !cuenta.getNumeroCuenta().equals(newData.getNumeroCuenta())) {
                        return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("numeroCuenta", "is mandatory and cannot be changed")));
                    }
                    return repository.findByCuenta(cuenta.getId());
                }
        ).flatMap(movimientos -> {
            movimientos.sort(Comparator.comparing(MovimientoCuenta::getFecha).reversed());
            MovimientoCuenta ultimoMovimiento = movimientos.stream().findFirst().get();

            if (!ultimoMovimiento.getId().equals(id)) {
                return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("account movement", "is not the last one")));
            }

            newData.setSaldoAnterior(ultimoMovimiento.getSaldoAnterior());

            DataValidationResult validationResult = newData.validateForPatching();

            if (!validationResult.isValid()) {
                return Mono.error(InvalidDataException.getInstance(validationResult.getErrors()));
            }

            if (newData.getValor() != null) {
                BigDecimal balance = BalanceUtils.apply(newData.getSaldoAnterior(), newData.getValor());
                newData.setSaldo(balance);
            }

            newData.setFecha(LocalDateTime.now());

            BeanCopyUtil.copyNonNullProperties(newData, ultimoMovimiento);

            return repository.update(ultimoMovimiento);
        });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id).flatMap(movimientoCuenta -> {
            if (!movimientoCuenta.isValidId()) {
                return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("id", "is invalid")));
            }

            return cuentaRepository.findById(movimientoCuenta.getCuenta());

        }).flatMap(
                cuenta -> repository.findByCuenta(cuenta.getId())
        ).flatMap(movimientos -> {
            movimientos.sort(Comparator.comparing(MovimientoCuenta::getFecha).reversed());
            MovimientoCuenta ultimoMovimiento = movimientos.stream().findFirst().get();

            if (!ultimoMovimiento.getId().equals(id)) {
                return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("account movement", "is not the last one")));
            }

            return repository.delete(ultimoMovimiento);
        });
    }
}
