package tiket.api;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tiket.dto.response.TicketResponse;
import tiket.entities.Ticket;
import tiket.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketAPI {
    private final TicketService ticketService;

    @PostConstruct
    public void saveTickets(){
        ticketService.saveTicketsFromJsonFile();
    }

    @GetMapping("/response")
    public TicketResponse ticketResponse(){
        return ticketService.ticketResponse();
    }
}
