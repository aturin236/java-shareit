package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date_time")
    private LocalDateTime start;
    @Column(name = "end_date_time")
    private LocalDateTime end;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    private StatusOfBooking status;
}
