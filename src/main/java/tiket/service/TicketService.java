package tiket.service;

import tiket.dto.response.TicketResponse;

public interface TicketService {
    TicketResponse ticketResponse();

    void saveTicketsFromJsonFile();
}
