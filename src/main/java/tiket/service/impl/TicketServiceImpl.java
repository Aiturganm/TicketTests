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
import java.time.Duration;
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

        List<Object[]> durations = ticketRepository.minimalTime();
        List<String> strings = new ArrayList<>();
        for (Object[] duration : durations) {
            Integer minFlightDuration = (Integer) duration[1];
            String carrier = (String) duration[0];
            LocalTime minTime = LocalTime.of(minFlightDuration/60, minFlightDuration%60);
            strings.add(carrier + ": Minimum flight time: " + minTime.getHour() + " hours, " + minTime.getMinute() + " minutes.");
        }

        return TicketResponse.builder()
                .minTime(strings)
                .differenceOfPrice(ticketRepository.averagePrice().subtract(medianPrice).abs())
                .build();
    }
}
