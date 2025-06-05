package ec.dev.samagua.ekumen_bank_accounts.controllers;

import ec.dev.samagua.ekumen_bank_accounts.dtos.EstadoClienteCuentaMovimientoDto;
import ec.dev.samagua.ekumen_bank_accounts.dtos_mappers.EstadoClienteCuentaMovimientoDtoMapper;
import ec.dev.samagua.ekumen_bank_accounts.models.EstadoClienteCuentaMovimiento;
import ec.dev.samagua.ekumen_bank_accounts.services.ReporteService;
import ec.dev.samagua.ekumen_bank_accounts.utils.CustomDateUtils;
import ec.dev.samagua.ekumen_bank_accounts.utils_controllers_models.ControllerResult;
import ec.dev.samagua.ekumen_bank_accounts.utils_models.DateRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReporteController {
    private final ReporteService service;
    private final EstadoClienteCuentaMovimientoDtoMapper mapper;

    @GetMapping("/reportes")
    public Mono<ResponseEntity<ControllerResult<List<EstadoClienteCuentaMovimientoDto>>>> generarEstadoCuenta(
            @RequestParam(name = "cliente-id") String clienteId,
            @RequestParam(name = "fecha") String rangoFechasAsString) {

        log.debug("executing GET /reportes, clienteId: {}, fecha: {}", clienteId, rangoFechasAsString);

        DateRange rangoFechas = CustomDateUtils.getDateRange(rangoFechasAsString);

        Mono<List<EstadoClienteCuentaMovimiento>> entities = service.generarEstadoCliente(clienteId, rangoFechas.getStartDate(), rangoFechas.getEndDate());
        return entities.map(obj -> {
            ControllerResult<List<EstadoClienteCuentaMovimientoDto>> body = ControllerResult.getSuccessResult(obj.stream()
                    .map(mapper::modelToDto)
                    .toList());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(body);
        }).doOnSuccess(obj -> log.debug("GET /reportes response: {}", obj));
    }
}
