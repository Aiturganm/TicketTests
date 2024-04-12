package tiket.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(generator = "id_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ticket_gen", allocationSize = 1)
    private Long id;
    private String origin;
    private String origin_name;
    private String destination;
    private String destination_name;
    private LocalDate departure_date;
    private LocalTime departure_time;
    private LocalDate arrival_date;
    private LocalTime arrival_time;
    private String carrier;
    private int stops;
    private BigDecimal price;
}
