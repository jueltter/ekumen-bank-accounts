package ec.dev.samagua.ekumen_bank_accounts.services;

public interface JsonService {
    <E> String toJson(E object);
}