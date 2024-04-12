package tiket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import tiket.api.TicketAPI;
import tiket.dto.response.TicketResponse;
import tiket.entities.Ticket;
import tiket.entities.TicketList;
import tiket.repository.TicketRepository;
import tiket.service.TicketService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    @Override
    public void saveTicketsFromJsonFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            InputStream inputStream = TicketAPI.class.getClassLoader().getResourceAsStream("Ticket.json");
            TicketList ticketList = objectMapper.readValue(inputStream, TicketList.class);
            inputStream.close();
            ticketRepository.saveAll(ticketList.getTickets());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TicketResponse ticketResponse() {
        BigDecimal medianPrice = BigDecimal.valueOf(0);
        List<BigDecimal> orderPrice = ticketRepository.orderPrice();
        if(orderPrice.size() % 2 == 0){
            BigDecimal add = orderPrice.get(orderPrice.size() / 2 - 1).add(orderPrice.get(orderPrice.size() / 2));
            medianPrice = add.divide(BigDecimal.valueOf(2));
        }else {
            medianPrice = orderPrice.get((orderPrice.size() - 1) / 2);
        }

        List<Duration> durations = ticketRepository.minimalTime();
        List<String> strings = new ArrayList<>();
        int order = 0;
        for (Duration duration : durations) {
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            order += 1;
            strings.add(order + ". Minimum flight time: " + hours + " hour, " + minutes + " minute and " + seconds + " second.");
        }

        return TicketResponse.builder()
                .minTime(strings)
                .differenceOfPrice(ticketRepository.averagePrice().subtract(medianPrice).abs())
                .build();
    }

}
