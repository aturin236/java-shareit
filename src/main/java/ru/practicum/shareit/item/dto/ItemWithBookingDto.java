package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class ItemWithBookingDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    private Optional<@NotNull Boolean> available;
    private Long requestId;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDtoOut> comments;
}
