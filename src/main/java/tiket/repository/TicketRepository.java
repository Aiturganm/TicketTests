package tiket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tiket.entities.Ticket;

import java.math.BigDecimal;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t.carrier, MIN((((HOUR(t.arrival_time) * 60) + MINUTE(t.arrival_time)) - ((HOUR(t.departure_time) * 60) + MINUTE(t.departure_time)))) " +
            "FROM Ticket t WHERE t.origin_name = 'Владивосток' AND t.destination_name = 'Тель-Авив' GROUP BY t.carrier")
    List<Object[]> minimalTime();

    @Query("select t.price from Ticket t where t.origin_name = 'Владивосток' and t.destination_name = 'Тель-Авив' order by t.price asc")
    List<BigDecimal> orderPrice();

    @Query("select avg (t.price) from Ticket t where t.origin_name = 'Владивосток' and t.destination_name = 'Тель-Авив'")
    BigDecimal averagePrice();
}
