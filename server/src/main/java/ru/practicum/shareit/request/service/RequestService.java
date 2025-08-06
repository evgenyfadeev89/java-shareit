package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.NewRequest;
import ru.practicum.shareit.request.dto.PersonalRequestDto;
import ru.practicum.shareit.request.dto.PublicRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface RequestService {

    RequestDto create(NewRequest newRequest, Long userId, LocalDateTime localDateTime);

    List<PersonalRequestDto> findAllPersonal(Long userId);

    List<PublicRequestDto> findAll(Long userId);

    PersonalRequestDto getRequestById(Long requestId, Long userId);
}
