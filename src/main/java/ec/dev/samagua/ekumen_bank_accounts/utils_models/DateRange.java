package ec.dev.samagua.ekumen_bank_accounts.utils_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateRange {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}