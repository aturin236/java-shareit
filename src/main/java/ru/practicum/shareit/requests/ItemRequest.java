package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_requests")
@Data
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id")
    private User requestor;
    private LocalDateTime created;
}
