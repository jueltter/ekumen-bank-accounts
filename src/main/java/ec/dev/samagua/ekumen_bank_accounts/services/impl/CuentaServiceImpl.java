package ec.dev.samagua.ekumen_bank_accounts.services.impl;

import ec.dev.samagua.ekumen_bank_accounts.models.Cuenta;
import ec.dev.samagua.ekumen_bank_accounts.infrastructure.clients.clients.ClienteServiceClient;
import ec.dev.samagua.ekumen_bank_accounts.models.validators.CuentaValidator;
import ec.dev.samagua.ekumen_bank_accounts.repositories.impl.CuentaRepository;
import ec.dev.samagua.ekumen_bank_accounts.repositories.impl.MovimientoCuentaRepository;
import ec.dev.samagua.ekumen_bank_accounts.services.CuentaService;
import ec.dev.samagua.ekumen_bank_accounts.utils.BeanCopyUtil;
import ec.dev.samagua.commons_lib.exceptions.InvalidDataException;
import ec.dev.samagua.commons_lib.models.DataValidationResult;
import ec.dev.samagua.commons_lib.models.IdentityFieldWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository repository;
    private final ClienteServiceClient clienteServiceClient;
    private final MovimientoCuentaRepository movimientoCuentaRepository;
    private final CuentaValidator cuentaValidator;


    @Override
    public Mono<List<Cuenta>> search(String clienteId) {
        if (clienteId == null) {
            return repository.findAll();
        }
        return repository.findbyClienteId(clienteId);
    }

    @Override
    public Mono<Cuenta> create(Cuenta cuenta) {
        if (cuenta.getNombreCliente() == null || cuenta.getNombreCliente().isBlank()) {
            return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("nombreCliente", "is mandatory")));
        }

        return clienteServiceClient.findByNombreOrClienteId(cuenta.getNombreCliente(), null)
                .flatMap(clientes -> {
                    if (clientes.isEmpty()) {
                        return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("nombreCliente", "is invalid")));
                    }

                    cuenta.setClienteId(clientes.stream().findFirst().get().getClienteId());

                    return repository.countByNumeroCuenta(cuenta.getNumeroCuenta());


                }).flatMap(countNumeroCuenta -> {

                            DataValidationResult validationResult = cuentaValidator.validateForCreating(cuenta, countNumeroCuenta);

                            if (!validationResult.isValid()) {
                                return Mono.error(InvalidDataException.getInstance(validationResult.getErrors()));
                            }

                            return repository.create(cuenta);
                        }


                );
    }

    @Override
    public Mono<Cuenta> update(Long id, Cuenta newData) {
        AtomicReference<Cuenta> atomicEntity = new AtomicReference<>();

        return repository.findById(id)
                .flatMap(entity -> {
                    if (!entity.isValidId()) {
                        return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("id", "is invalid")));
                    }

                    atomicEntity.set(entity);

                    return clienteServiceClient.findByNombreOrClienteId(newData.getNombreCliente(), null)
                            .flatMap(clientes -> {
                                if (clientes.isEmpty()) {
                                    return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("nombreCliente", "is invalid")));
                                }

                                newData.setClienteId(clientes.stream().findFirst().get().getClienteId());

                                return repository.countByNumeroCuenta(newData.getNumeroCuenta());
                            });
                }).flatMap(countNumeroCuenta -> {

                    IdentityFieldWrapper numeroCuentaWrapper = new IdentityFieldWrapper(countNumeroCuenta, Objects.equals(atomicEntity.get().getNumeroCuenta(), newData.getNumeroCuenta()));

                    DataValidationResult validationResult = cuentaValidator.validateForUpdating(newData, numeroCuentaWrapper);

                    if (!validationResult.isValid()) {
                        return Mono.error(InvalidDataException.getInstance(validationResult.getErrors()));
                    }

                    newData.setId(id);

                    Cuenta entity = atomicEntity.updateAndGet(cuenta -> {
                        BeanUtils.copyProperties(newData, cuenta);
                        return cuenta;
                    });

                    return repository.update(entity);
                });
    }

    @Override
    public Mono<Cuenta> patch(Long id, Cuenta newData) {
        AtomicReference<Cuenta> atomicEntity = new AtomicReference<>();

        return repository.findById(id)
                .flatMap(entity -> {
                    if (!entity.isValidId()) {
                        return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("id", "is invalid")));
                    }

                    atomicEntity.set(entity);

                    return clienteServiceClient.findByNombreOrClienteId(newData.getNombreCliente(), null)

                            .flatMap(clientes -> {
                                if (newData.getNombreCliente() != null && clientes.isEmpty()) {
                                    return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("nombreCliente", "is invalid")));
                                }

                                clientes.stream().findFirst().ifPresent(cliente -> newData.setClienteId(cliente.getClienteId()));

                                return repository.countByNumeroCuenta(newData.getNumeroCuenta());
                            });
                }).flatMap(countNumeroCuenta -> {

                    IdentityFieldWrapper numeroCuentaWrapper = new IdentityFieldWrapper(countNumeroCuenta, Objects.equals(atomicEntity.get().getNumeroCuenta(), newData.getNumeroCuenta()));

                    DataValidationResult validationResult = cuentaValidator.validateForPatching(newData, numeroCuentaWrapper);

                    if (!validationResult.isValid()) {
                        return Mono.error(InvalidDataException.getInstance(validationResult.getErrors()));
                    }

                    newData.setId(id);

                    Cuenta entity = atomicEntity.updateAndGet(cuenta -> {
                        BeanCopyUtil.copyNonNullProperties(newData, cuenta);
                        return cuenta;
                    });

                    return repository.update(entity);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        AtomicReference<Cuenta> atomicEntity = new AtomicReference<>();

        return repository.findById(id).flatMap(entity -> {
            if (!entity.isValidId()) {
                return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("id", "is invalid")));
            }

            atomicEntity.set(entity);

            return movimientoCuentaRepository.countByCuenta(id);
        }).flatMap(countMovimientoCuenta -> {

                    DataValidationResult validationResult = cuentaValidator.validateForDeleting(atomicEntity.get(), countMovimientoCuenta);

                    if (!validationResult.isValid()) {
                        return Mono.error(InvalidDataException.getInstance(validationResult.getErrors()));
                    }

                    return repository.delete(atomicEntity.get());

                }

        );
    }
}
