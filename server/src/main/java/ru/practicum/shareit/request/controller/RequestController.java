package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.NewRequest;
import ru.practicum.shareit.request.dto.PersonalRequestDto;
import ru.practicum.shareit.request.dto.PublicRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RequestDto> create(@RequestBody NewRequest newRequest,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(requestService.create(newRequest, userId, LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<List<PersonalRequestDto>> getAllPersonal(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        List<PersonalRequestDto> requests = requestService.findAllPersonal(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(requests);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PublicRequestDto>> getAll(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        List<PublicRequestDto> requests = requestService.findAll(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(requests);
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<PersonalRequestDto> getRequestById(
            @PathVariable Long requestId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {

        PersonalRequestDto request = requestService.getRequestById(requestId, userId);
        return ResponseEntity.ok(request);
    }

}
