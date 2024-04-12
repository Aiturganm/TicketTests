package tiket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tiket.entities.Ticket;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select t.arrival_time -  t.departure_time as min_flight_duration from Ticket t where t.origin_name = 'Владивосток' and t.destination_name = 'Тель-Авив'")
    List<Duration> minimalTime();

    @Query("select t.price from Ticket t where t.origin_name = 'Владивосток' and t.destination_name = 'Тель-Авив' order by t.price asc")
    List<BigDecimal> orderPrice();

    @Query("select avg (t.price) from Ticket t where t.origin_name = 'Владивосток' and t.destination_name = 'Тель-Авив'")
    BigDecimal averagePrice();
}
