package ec.dev.samagua.ekumen_bank_accounts.utils_exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class InvalidDataException extends RuntimeException {

    private String code;
    private Map<String, String> errors;

    public InvalidDataException(String message) {
        super(message);
    }

    public static InvalidDataException getInstance(Map<String, String> errors) {
        InvalidDataException ex = new InvalidDataException("An error has occurred while validating the object");
        ex.setCode("INVALID_DATA");
        ex.setErrors(errors);
        return ex;
    }

}
