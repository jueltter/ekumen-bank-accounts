package ec.dev.samagua.ekumen_bank_accounts.utils;

import java.math.BigDecimal;

public final class BalanceUtils {
    private BalanceUtils() {
        // Prevent instantiation
    }

    public static BigDecimal apply(BigDecimal balance, BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(value);
        }
        else {
            balance = balance.subtract(value.abs());
        }
        return balance;
    }

    public static boolean canApply(BigDecimal balance, BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0 && balance.compareTo(value.abs()) < 0) {
            return false;
        }

        return true;
    }
}
