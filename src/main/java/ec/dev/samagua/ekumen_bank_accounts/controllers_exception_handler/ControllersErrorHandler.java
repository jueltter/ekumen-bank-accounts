package ec.dev.samagua.ekumen_bank_accounts.controllers_exception_handler;

import ec.dev.samagua.ekumen_bank_accounts.utils_controllers_models.ControllerResult;
import ec.dev.samagua.ekumen_bank_accounts.utils_exceptions.InvalidDataException;
import ec.dev.samagua.ekumen_bank_accounts.utils_exceptions.RepositoryException;
import ec.dev.samagua.ekumen_bank_accounts.utils_models.KeyValuePair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ControllersErrorHandler {

    @ExceptionHandler(RepositoryException.class)
    public Mono<ResponseEntity<ControllerResult<Void>>> handleRepositoryException(RepositoryException ex) {
        ControllerResult<Void> body = ControllerResult.getErrorResultFromRepositoryException(ex);

        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body));
    }

    @ExceptionHandler(InvalidDataException.class)
    public Mono<ResponseEntity<ControllerResult<Void>>> handleInvalidDataException(InvalidDataException ex) {
        ControllerResult<Void> body = ControllerResult.getErrorResultFromInvalidDataException(ex);

        return Mono.just(ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(body))
                .doFinally(signal -> log.error("An error has occurred while validating data", ex));
    }

    //@ExceptionHandler({ServerWebInputException.class, DecodingException.class, Exception.class})
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ControllerResult<Void>>> handleGeneric(Exception ex) {
        ControllerResult<Void> body = ControllerResult.getErrorResult("UNHANDLED_EXCEPTION",  "An unhandled error has occurred while processing your request", KeyValuePair.<String, String>builder()
                .key(ex.getClass().getCanonicalName())
                .value(ex.getMessage())
                .build());

        return Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(body))
                .doFinally(signal -> log.error("An unhandled error has occurred while processing your request", ex));
    }

}
