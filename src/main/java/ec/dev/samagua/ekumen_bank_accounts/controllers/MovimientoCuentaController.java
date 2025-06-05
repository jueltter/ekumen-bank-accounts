package ec.dev.samagua.ekumen_bank_accounts.controllers;

import ec.dev.samagua.ekumen_bank_accounts.dtos.MovimientoCuentaDto;
import ec.dev.samagua.ekumen_bank_accounts.dtos_mappers.MovimientoCuentaDtoMapper;
import ec.dev.samagua.ekumen_bank_accounts.entities.MovimientoCuenta;
import ec.dev.samagua.ekumen_bank_accounts.services.MovimientoCuentaService;
import ec.dev.samagua.ekumen_bank_accounts.utils_controllers_models.ControllerResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MovimientoCuentaController {

    private final MovimientoCuentaService service;
    private final MovimientoCuentaDtoMapper mapper;

    @GetMapping("/movimientos")
    public Mono<ControllerResult<List<MovimientoCuentaDto>>> findAll() {
        Mono<List<MovimientoCuenta>> entities = service.findAll();
        return entities.map(obj -> ControllerResult.getSuccessResult(obj.stream()
                        .map(mapper::entityToDto)
                        .toList()))
                .doOnSuccess(obj -> log.debug("GET /movimientos response: {}", obj));
    }

    @PostMapping("/movimientos")
    public Mono<ResponseEntity<ControllerResult<MovimientoCuentaDto>>> create(@RequestBody MovimientoCuentaDto dto) {
        MovimientoCuenta entity = mapper.dtoToEntity(dto);
        log.debug("executing POST /movimientos, body: {}", dto);
        log.debug("executing POST /movimientos, bodyToEntity: {}", entity);

        Mono<MovimientoCuenta> entityAsMono = service.create(entity);
        return entityAsMono.map(obj -> {
                    ControllerResult<MovimientoCuentaDto> body = ControllerResult.getSuccessResult(mapper.entityToDto(obj));
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(body);
                })
                .doOnSuccess(obj -> log.debug("POST /movimientos response: {}", obj));
    }

    @PutMapping("/movimientos/{id}")
    public Mono<ResponseEntity<ControllerResult<MovimientoCuentaDto>>> update(@PathVariable Long id, @RequestBody MovimientoCuentaDto dto) {
        MovimientoCuenta entity = mapper.dtoToEntity(dto);
        log.debug("executing PUT /movimientos, body: {}", dto);
        log.debug("executing PUT /movimientos, bodyToEntity: {}", entity);

        Mono<MovimientoCuenta> entityAsMono = service.update(id, entity);
        return entityAsMono.map(obj -> {
                    ControllerResult<MovimientoCuentaDto> body = ControllerResult.getSuccessResult(mapper.entityToDto(obj));
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(body);
                })
                .doOnSuccess(obj -> log.debug("PUT /movimientos response: {}", obj));
    }

    @PatchMapping("/movimientos/{id}")
    public Mono<ResponseEntity<ControllerResult<MovimientoCuentaDto>>> patch(@PathVariable Long id, @RequestBody MovimientoCuentaDto dto) {
        MovimientoCuenta entity = mapper.dtoToEntity(dto);
        log.debug("executing PATCH /movimientos, body: {}", dto);
        log.debug("executing PATCH /movimientos, bodyToEntity: {}", entity);

        Mono<MovimientoCuenta> entityAsMono = service.patch(id, entity);
        return entityAsMono.map(obj -> {
            ControllerResult<MovimientoCuentaDto> body = ControllerResult.getSuccessResult(mapper.entityToDto(obj));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(body);
        }).doOnSuccess(obj -> log.debug("PATCH /cuentas response: {}", obj));
    }

    @DeleteMapping("/movimientos/{id}")
    public Mono<ResponseEntity<ControllerResult<Void>>> delete(@PathVariable Long id) {
        log.debug("executing DELETE /movimientos, id: {}", id);

        Mono<Void> voidMono = service.delete(id);

        ControllerResult<Void> body = ControllerResult.getSuccessResult();
        ResponseEntity<ControllerResult<Void>> response = ResponseEntity
                .status(HttpStatus.OK)
                .body(body);

        return voidMono.then(Mono.just(response))
                .doOnSuccess(obj -> log.debug("DELETE /movimientos response: {}", obj));
    }

}
