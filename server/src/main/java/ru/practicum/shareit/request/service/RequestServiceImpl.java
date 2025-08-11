package ru.practicum.shareit.request.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.PersonalRequestDto;
import ru.practicum.shareit.request.dto.PublicRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.*;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public RequestDto create(NewRequest newRequest, Long userId, LocalDateTime localDateTime) {
        if (!newRequest.hasValidDescription()) {
            throw new ConditionsNotMetException("Описание должно быть указано");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует");
        }

        newRequest.setRequestor(userId);
        newRequest.setCreated(localDateTime);

        Request request = requestMapper.toRequest(newRequest);
        request = requestRepository.save(request);

        return requestMapper.toRequestDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalRequestDto> findAllPersonal(Long userId) {

        List<Request> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);

        List<PersonalRequestDto> result = new ArrayList<>();

        for (Request request : requests) {
            PersonalRequestDto personalRequestDto = requestMapper.toPersonalRequestDto(request);

            List<Item> items = itemRepository.findByRequestId(request.getId());

            List<AnswerDto> answers = items.stream()
                    .map(requestMapper::toAnswerDto)
                    .collect(Collectors.toList());

            personalRequestDto.setItems(answers);
            result.add(personalRequestDto);
        }

        return result;
    }


    @Override
    @Transactional(readOnly = true)
    public List<PublicRequestDto> findAll(Long userId) {
        List<Request> requests = requestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);

        return requests.stream()
                .map(requestMapper::toPublicRequestDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public PersonalRequestDto getRequestById(Long requestId, Long userId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId + " не найден"));

        PersonalRequestDto requestWithAnswersDto = requestMapper.toPersonalRequestDto(request);

        List<Item> items = itemRepository.findByRequestId(requestId);

        List<AnswerDto> answers = items.stream()
                .map(requestMapper::toAnswerDto)
                .collect(Collectors.toList());

        requestWithAnswersDto.setItems(answers);

        return requestWithAnswersDto;
    }

}
