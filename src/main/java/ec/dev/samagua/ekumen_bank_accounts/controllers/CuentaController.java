package ec.dev.samagua.ekumen_bank_accounts.controllers;

import ec.dev.samagua.ekumen_bank_accounts.dtos.CuentaDto;
import ec.dev.samagua.ekumen_bank_accounts.dtos.mappers.CuentaDtoMapper;
import ec.dev.samagua.ekumen_bank_accounts.models.Cuenta;
import ec.dev.samagua.ekumen_bank_accounts.services.CuentaService;
import ec.dev.samagua.commons_lib.models.controllers.ControllerResult;
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
public class CuentaController {

    private final CuentaService service;
    private final CuentaDtoMapper mapper;

    @GetMapping("/cuentas")
    public Mono<ControllerResult<List<CuentaDto>>> findAll(@RequestParam(name = "cliente-id", required = false) String clienteId) {
        log.debug("executing GET /clientes, clienteId: {}", clienteId);
        Mono<List<Cuenta>> entities = service.search(clienteId);
        return entities.map(obj -> ControllerResult.getSuccessResult(obj.stream()
                        .map(mapper::entityToDto)
                        .toList()))
                .doOnSuccess(obj -> log.debug("GET /cuentas response: {}", obj));
    }

    @PostMapping("/cuentas")
    public Mono<ResponseEntity<ControllerResult<CuentaDto>>> create(@RequestBody CuentaDto dto) {
        Cuenta entity = mapper.dtoToEntity(dto);
        log.debug("executing POST /cuentas, body: {}", dto);
        log.debug("executing POST /cuentas, bodyToEntity: {}", entity);

        Mono<Cuenta> entityAsMono = service.create(entity);
        return entityAsMono.map(obj -> {
                    ControllerResult<CuentaDto> body = ControllerResult.getSuccessResult(mapper.entityToDto(obj));
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(body);
                })
                .doOnSuccess(obj -> log.debug("POST /cuentas response: {}", obj));
    }

    @PutMapping("/cuentas/{id}")
    public Mono<ResponseEntity<ControllerResult<CuentaDto>>> update(@PathVariable Long id, @RequestBody CuentaDto dto) {
        Cuenta entity = mapper.dtoToEntity(dto);
        log.debug("executing PUT /cuentas, body: {}", dto);
        log.debug("executing PUT /cuentas, bodyToEntity: {}", entity);

        Mono<Cuenta> entityAsMono = service.update(id, entity);
        return entityAsMono.map(obj -> {
                    ControllerResult<CuentaDto> body = ControllerResult.getSuccessResult(mapper.entityToDto(obj));
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(body);
                })
                .doOnSuccess(obj -> log.debug("PUT /cuentas response: {}", obj));
    }

    @PatchMapping("/cuentas/{id}")
    public Mono<ResponseEntity<ControllerResult<CuentaDto>>> patch(@PathVariable Long id, @RequestBody CuentaDto dto) {
        Cuenta entity = mapper.dtoToEntity(dto);
        log.debug("executing PATCH /cuentas, body: {}", dto);
        log.debug("executing PATCH /cuentas, bodyToEntity: {}", entity);

        Mono<Cuenta> entityAsMono = service.patch(id, entity);
        return entityAsMono.map(obj -> {
            ControllerResult<CuentaDto> body = ControllerResult.getSuccessResult(mapper.entityToDto(obj));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(body);
        }).doOnSuccess(obj -> log.debug("PATCH /cuentas response: {}", obj));
    }

    @DeleteMapping("/cuentas/{id}")
    public Mono<ResponseEntity<ControllerResult<Void>>> delete(@PathVariable Long id) {
        log.debug("executing DELETE /clientes, id: {}", id);

        Mono<Void> voidMono = service.delete(id);

        ControllerResult<Void> body = ControllerResult.getSuccessResult();
        ResponseEntity<ControllerResult<Void>> response = ResponseEntity
                .status(HttpStatus.OK)
                .body(body);

        return voidMono.then(Mono.just(response))
                .doOnSuccess(obj -> log.debug("DELETE /cuentas response: {}", obj));
    }

}
