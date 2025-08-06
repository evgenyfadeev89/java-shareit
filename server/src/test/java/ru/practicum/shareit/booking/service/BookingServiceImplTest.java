package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.dto.NewBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private NewBooking newBooking;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner User", "owner@example.com");
        owner = userRepository.save(owner);

        booker = new User(null, "Booker User", "booker@example.com");
        booker = userRepository.save(booker);

        item = new Item(null, "Test Item", "Description", true, owner, null);
        item = itemRepository.save(item);

        newBooking = new NewBooking(item.getId(),
                booker.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                Status.WAITING);
    }

    @Test
    void createBooking() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        assertNotNull(bookingDto.getId());
        assertEquals(Status.WAITING, bookingDto.getStatus());
    }

    @Test
    void createBookingStartEqualsEndShouldThrowValidationException() {
        LocalDateTime now = LocalDateTime.now();
        NewBooking invalidBooking = new NewBooking(item.getId(), booker.getId(),
                now.plusDays(1),
                now.plusDays(1),
                Status.WAITING);
        assertThrows(ValidationException.class, () -> bookingService.create(invalidBooking, booker.getId()));
    }

    @Test
    void createBookingStartIsNullShouldThrowValidationException() {
        NewBooking invalidBooking = new NewBooking(item.getId(), booker.getId(),
                null,
                LocalDateTime.now().plusDays(2),
                Status.WAITING);
        assertThrows(ValidationException.class, () -> bookingService.create(invalidBooking, booker.getId()));
    }

    @Test
    void createBookingWithNullUserId() {
        assertThrows(NotFoundException.class, () -> bookingService.create(newBooking, null));
    }

    @Test
    void createBookingEndIsNullShouldThrowValidationException() {
        NewBooking invalidBooking = new NewBooking(item.getId(), booker.getId(),
                LocalDateTime.now().plusDays(1),
                null,
                Status.WAITING);
        assertThrows(ValidationException.class, () -> bookingService.create(invalidBooking, booker.getId()));
    }

    @Test
    void approveBooking() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        BookingDto approvedBooking = bookingService.approveBooking(bookingDto.getId(), owner.getId(), true);
        assertEquals(Status.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void approveBookingNullApprovedShouldHandleGracefully() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        assertThrows(NullPointerException.class, () -> bookingService.approveBooking(bookingDto.getId(), owner.getId(), null));
    }

    @Test
    void getBookingById() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        BookingDto foundBooking = bookingService.getBookingById(bookingDto.getId(), booker.getId());
        assertEquals(bookingDto.getId(), foundBooking.getId());
    }

    @Test
    void getUserBookings() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.ALL);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookings() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), BookingState.ALL);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingByIdNotFound() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(999L, booker.getId()));
    }

    @Test
    void createBookingItemNotAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);

        assertThrows(ConditionsNotMetException.class, () ->
                bookingService.create(newBooking, booker.getId()));
    }

    @Test
    void createBookingOwnerBookingOwnItem() {
        assertThrows(DuplicatedDataException.class, () -> bookingService.create(newBooking, owner.getId()));
    }

    @Test
    void approveBookingNotOwner() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        User tempUser = new User(null, "Another User", "another@example.com");
        final User anotherUser = userRepository.save(tempUser);

        assertThrows(ConditionsNotMetException.class, () -> bookingService.approveBooking(bookingDto.getId(), anotherUser.getId(), true));
    }

    @Test
    void getBookingByIdNotAuthorized() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        User tempUser = new User(null, "Another User", "another@example.com");
        final User anotherUser = userRepository.save(tempUser);

        assertThrows(ConditionsNotMetException.class, () -> bookingService.getBookingById(bookingDto.getId(), anotherUser.getId()));
    }

    @Test
    void getUserBookingsInvalidState() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.getUserBookings(booker.getId(), BookingState.valueOf("INVALID")));
    }

    @Test
    void getOwnerBookingsUserHasNoItemsShouldThrowNotFound() {
        assertThrows(NotFoundException.class, () -> bookingService.getOwnerBookings(999L, BookingState.ALL));
    }

    @Test
    void getOwnerBookingsInvalidState() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.getOwnerBookings(owner.getId(), BookingState.valueOf("INVALID")));
    }

    @Test
    void getUserBookingsCurrentState() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.CURRENT);
        assertTrue(bookings.isEmpty());
    }

    @Test
    void getUserBookingsPastState() {
        newBooking = new NewBooking(item.getId(),
                booker.getId(),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                Status.WAITING);
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.PAST);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getUserBookingsFutureState() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.FUTURE);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getUserBookingsWaitingState() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.WAITING);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getUserBookingsRejectedState() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        bookingService.approveBooking(bookingDto.getId(), owner.getId(), false);
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.REJECTED);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookingsCurrentState() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), BookingState.CURRENT);
        assertTrue(bookings.isEmpty());
    }

    @Test
    void getOwnerBookingsPastState() {
        newBooking = new NewBooking(item.getId(),
                owner.getId(),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                Status.WAITING);
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), BookingState.PAST);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookingsFutureState() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), BookingState.FUTURE);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookingsWaitingState() {
        bookingService.create(newBooking, booker.getId());
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), BookingState.WAITING);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookingsRejectedState() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        bookingService.approveBooking(bookingDto.getId(), owner.getId(),  false);
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), BookingState.REJECTED);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getUserBookingsCurrentStateShouldIncludeBookingsWithStartBeforeNowAndEndAfterNow() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking bookingCurrent = new NewBooking(item.getId(), booker.getId(),
                now.minusDays(1), now.plusDays(1), Status.APPROVED);

        bookingService.create(bookingCurrent, booker.getId());

        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.CURRENT);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().anyMatch(b -> b.getStart().isBefore(now) &&
                (b.getEnd().isAfter(now) || b.getEnd() == null)));
    }

    @Test
    void getUserBookingsPastStateShouldIncludeBookingsWithEndBeforeNow() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking bookingPast = new NewBooking(item.getId(), booker.getId(),
                now.minusDays(5), now.minusDays(1), Status.APPROVED);

        bookingService.create(bookingPast, booker.getId());

        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.PAST);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getEnd().isBefore(now)));
    }

    @Test
    void getUserBookingsFutureStateShouldIncludeBookingsWithStartAfterNow() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking bookingFuture = new NewBooking(item.getId(), booker.getId(),
                now.plusDays(1), now.plusDays(2), Status.WAITING);

        bookingService.create(bookingFuture, booker.getId());

        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.FUTURE);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getStart().isAfter(now)));
    }

    @Test
    void getUserBookingsWaitingStateShouldIncludeBookingsWithStatusWaiting() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking bookingWaiting = new NewBooking(item.getId(), booker.getId(),
                now.plusDays(1), now.plusDays(2), Status.WAITING);

        bookingService.create(bookingWaiting, booker.getId());

        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.WAITING);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getStatus() == Status.WAITING));
    }

    @Test
    void getUserBookingsRejectedStateShouldIncludeBookingsWithStatusRejected() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        bookingService.approveBooking(bookingDto.getId(), owner.getId(), false);

        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), BookingState.REJECTED);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getStatus() == Status.REJECTED));
    }

    @Test
    void createBookingWithNonexistentUserShouldThrowNotFoundException() {
        NewBooking booking = new NewBooking(item.getId(), 999L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                Status.WAITING);

        assertThrows(NotFoundException.class, () -> bookingService.create(booking, 999L));
    }

    @Test
    void createBookingWithNonexistentItemShouldThrowNotFoundException() {
        NewBooking booking = new NewBooking(999L, booker.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                Status.WAITING);

        assertThrows(NotFoundException.class, () -> bookingService.create(booking, booker.getId()));
    }

    @Test
    void getBookingByIdNotAuthorizedShouldThrowConditionsNotMetException() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());

        User otherUser = userRepository.save(new User(null, "Other", "other@example.com"));

        assertThrows(ConditionsNotMetException.class, () ->
                bookingService.getBookingById(bookingDto.getId(), otherUser.getId()));
    }

    @Test
    void getUserBookingsCurrentStateShouldReturnOnlyCurrentBookings() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking currentBooking = new NewBooking(item.getId(), booker.getId(),
                now.minusHours(1), now.plusHours(1), Status.APPROVED);
        bookingService.create(currentBooking, booker.getId());

        NewBooking pastBooking = new NewBooking(item.getId(), booker.getId(),
                now.minusDays(5), now.minusDays(2), Status.APPROVED);
        bookingService.create(pastBooking, booker.getId());

        List<BookingDto> currentBookings = bookingService.getUserBookings(booker.getId(), BookingState.CURRENT);

        assertFalse(currentBookings.isEmpty());
        assertTrue(currentBookings.stream().allMatch(b ->
                b.getStart().isBefore(now) && (b.getEnd().isAfter(now) || b.getEnd() == null)
        ));
    }

    @Test
    void getUserBookings_PastState_ShouldReturnOnlyPastBookings() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking pastBooking = new NewBooking(item.getId(), booker.getId(),
                now.minusDays(3), now.minusDays(1), Status.APPROVED);
        bookingService.create(pastBooking, booker.getId());

        NewBooking futureBooking = new NewBooking(item.getId(), booker.getId(),
                now.plusDays(1), now.plusDays(3), Status.APPROVED);
        bookingService.create(futureBooking, booker.getId());

        List<BookingDto> pastBookings = bookingService.getUserBookings(booker.getId(), BookingState.PAST);

        assertFalse(pastBookings.isEmpty());
        assertTrue(pastBookings.stream().allMatch(b -> b.getEnd().isBefore(now)));
    }

    @Test
    void getUserBookings_FutureState_ShouldReturnOnlyFutureBookings() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking futureBooking = new NewBooking(item.getId(), booker.getId(),
                now.plusDays(2), now.plusDays(4), Status.APPROVED);
        bookingService.create(futureBooking, booker.getId());

        NewBooking currentBooking = new NewBooking(item.getId(), booker.getId(),
                now.minusHours(1), now.plusHours(1), Status.APPROVED);
        bookingService.create(currentBooking, booker.getId());

        List<BookingDto> futureBookings = bookingService.getUserBookings(booker.getId(), BookingState.FUTURE);

        assertFalse(futureBookings.isEmpty());
        assertTrue(futureBookings.stream().allMatch(b -> b.getStart().isAfter(now)));
    }

    @Test
    void getUserBookings_WaitingState_ShouldReturnOnlyWaitingBookings() {
        LocalDateTime now = LocalDateTime.now();

        NewBooking waitingBooking = new NewBooking(item.getId(), booker.getId(),
                now.plusDays(1), now.plusDays(2), Status.WAITING);
        bookingService.create(waitingBooking, booker.getId());

        NewBooking approvedBooking = new NewBooking(item.getId(), booker.getId(),
                now.plusDays(1), now.plusDays(2), Status.APPROVED);
        bookingService.create(approvedBooking, booker.getId());

        List<BookingDto> waitingBookings = bookingService.getUserBookings(booker.getId(), BookingState.WAITING);

        assertFalse(waitingBookings.isEmpty());
        assertTrue(waitingBookings.stream().allMatch(b -> b.getStatus() == Status.WAITING));
    }

    @Test
    void getUserBookings_RejectedState_ShouldReturnOnlyRejectedBookings() {
        BookingDto bookingDto = bookingService.create(newBooking, booker.getId());
        bookingService.approveBooking(bookingDto.getId(), owner.getId(), false);

        List<BookingDto> rejectedBookings = bookingService.getUserBookings(booker.getId(), BookingState.REJECTED);

        assertFalse(rejectedBookings.isEmpty());
        assertTrue(rejectedBookings.stream().allMatch(b -> b.getStatus() == Status.REJECTED));
    }

}