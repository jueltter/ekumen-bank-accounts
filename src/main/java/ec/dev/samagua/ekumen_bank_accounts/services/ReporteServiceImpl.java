package ec.dev.samagua.ekumen_bank_accounts.services;

import ec.dev.samagua.ekumen_bank_accounts.infrastructure.clients.clients.ClienteServiceClient;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoCliente;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoClienteCuenta;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoClienteCuentaMovimiento;
import ec.dev.samagua.ekumen_bank_accounts.models_mappers.EstadoClienteCuentaMapper;
import ec.dev.samagua.ekumen_bank_accounts.models_mappers.EstadoClienteCuentaMovimientoMapper;
import ec.dev.samagua.ekumen_bank_accounts.models_mappers.EstadoClienteMapper;
import ec.dev.samagua.ekumen_bank_accounts.repositories.CuentaRepository;
import ec.dev.samagua.ekumen_bank_accounts.repositories.MovimientoCuentaRepository;
import ec.dev.samagua.ekumen_bank_accounts.utils_exceptions.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoCuentaRepository movimientoCuentaRepository;
    private final ClienteServiceClient clienteRepository;
    private final JsonService jsonService;

    private final EstadoClienteMapper estadoClienteMapper;
    private final EstadoClienteCuentaMapper estadoClienteCuentaMapper;
    private final EstadoClienteCuentaMovimientoMapper estadoClienteCuentaMovimientoMapper;

    @Override
    public Mono<List<EstadoClienteCuentaMovimiento>> generarEstadoCliente(String idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        AtomicReference<EstadoCliente> estadoClienteRef = new AtomicReference<>();
        AtomicReference<List<EstadoClienteCuentaMovimiento>> movimientosEstadoClienteRef = new AtomicReference<>(new ArrayList<>());

        return clienteRepository.findByNombreOrClienteId(null, idCliente)
                .flatMap(clientes -> {
                    if (clientes.isEmpty()) {
                        return Mono.error(InvalidDataException.getInstance(Collections.singletonMap("cliente","not found")));
                    } else {
                        clientes.stream().findFirst().ifPresent(cliente -> {
                            EstadoCliente estadoCliente = estadoClienteMapper.entityToModel(cliente);
                            estadoClienteRef.set(estadoCliente);
                        });
                    }

                    return cuentaRepository.findbyClienteId(idCliente);
                })
                .flatMapMany(Flux::fromIterable)
                .flatMap(cuenta -> {
                    estadoClienteRef.updateAndGet(estadoCliente -> {
                        EstadoClienteCuenta estadoCuenta = estadoClienteCuentaMapper.entityToModel(cuenta);
                        estadoCliente.addCuenta(estadoCuenta);
                        return estadoCliente;
                    });

                    return movimientoCuentaRepository.findByCuentaAndFechaBetween(cuenta.getId(), fechaInicio, fechaFin);
                })
                .flatMap(movimientosCuenta -> {
                    if (!movimientosCuenta.isEmpty()) {
                        Long idCuenta = movimientosCuenta.stream().findFirst().get().getCuenta();

                        estadoClienteRef.updateAndGet(estadoCliente -> {
                            List<EstadoClienteCuentaMovimiento> movimientosEstadoCuenta = movimientosCuenta.stream()
                                    .map(estadoClienteCuentaMovimientoMapper::entityToModel)
                                    .toList();

                            estadoCliente.addMovimientosCuenta(idCuenta, movimientosEstadoCuenta);

                            return estadoCliente;
                        });
                    }
                    return Mono.empty();
                })
                .then(Mono.fromRunnable(() -> movimientosEstadoClienteRef.updateAndGet(movimientosEstadoCliente -> {
                    EstadoCliente estadoCliente = estadoClienteRef.get();
                    log.debug("estadoCliente: {}", jsonService.toJson(estadoCliente));

                    List<EstadoClienteCuentaMovimiento> movimientosEstadoClienteToAdd = estadoCliente.getCuentas().stream().flatMap(cuenta -> {
                        Stream<List<EstadoClienteCuentaMovimiento>> objectAttributeAsStream = Stream.ofNullable(cuenta.getMovimientos());

                        List<EstadoClienteCuentaMovimiento> movimientosCuenta = objectAttributeAsStream.flatMap(Collection::stream).toList();

                        movimientosCuenta.forEach(movimiento -> {
                            movimiento.setNombreCliente(estadoCliente.getNombre());
                            movimiento.setNumeroCuenta(cuenta.getNumeroCuenta());
                            movimiento.setTipoCuenta(cuenta.getTipoCuenta());
                            movimiento.setEstadoCuenta(cuenta.getEstado());
                        });

                        return movimientosCuenta.stream();
                    }).sorted(Comparator.comparing(EstadoClienteCuentaMovimiento::getFecha)).toList();

                    movimientosEstadoCliente.addAll(movimientosEstadoClienteToAdd);

                    return movimientosEstadoCliente;
                })))
                .then(Mono.just(movimientosEstadoClienteRef.get()));
    }
}
