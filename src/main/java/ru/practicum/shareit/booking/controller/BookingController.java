package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.NewBooking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingDto> create(
            @RequestBody NewBooking newBooking,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Creating booking for user ID: {}", userId);
        return ResponseEntity.ok(bookingService.create(newBooking, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestParam("approved") Boolean approved,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Updating booking status ID: {}, approved: {} by user ID: {}", bookingId, approved, userId);
        return ResponseEntity.ok(bookingService.approveBooking(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting booking ID: {} by user ID: {}", bookingId, userId);
        return ResponseEntity.ok(bookingService.getBookingById(bookingId, userId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting bookings for user ID: {} with state: {}", userId, state);
        return ResponseEntity.ok(bookingService.getUserBookings(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(
            @RequestParam(defaultValue = "ALL") BookingState state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting bookings for owner ID: {} with state: {}", userId, state);
        return ResponseEntity.ok(bookingService.getOwnerBookings(userId, state));
    }
}