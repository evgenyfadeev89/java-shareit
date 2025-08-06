package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;



@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;


    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                    @Valid @RequestBody RequestDto requestDto) {
        log.info("Creating item request {}, userId={}", requestDto, userId);
        return requestClient.create(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllPersonal(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Get item requests, userId={}", userId);
        return requestClient.findAllPersonal(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all item requests, userId={}, from={}, size={}", userId, from, size);
        return requestClient.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                     @PathVariable @Positive Long requestId) {
        log.info("Get item request {}, userId={}", requestId, userId);
        return requestClient.getItemRequestById(userId, requestId);
    }
}
