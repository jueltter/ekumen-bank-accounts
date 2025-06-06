package ec.dev.samagua.ekumen_bank_accounts.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.dev.samagua.ekumen_bank_accounts.services.JsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonServiceImpl implements JsonService {
    private final ObjectMapper objectMapper;

    @Override
    public <E> String toJson(E object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("An error has occurred while processing object as JSON", e);
        }
    }
}
