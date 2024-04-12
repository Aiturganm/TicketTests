package tiket.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Builder
public record TicketResponse(
        List<String> minTime,
        BigDecimal differenceOfPrice
) {
}
