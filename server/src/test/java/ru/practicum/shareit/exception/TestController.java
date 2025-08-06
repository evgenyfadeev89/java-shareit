package ru.practicum.shareit.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/not-found")
    public void throwNotFoundException() {
        throw new NotFoundException("Не найдено");
    }

    @GetMapping("/test/validation")
    public void throwValidationException() {
        throw new ValidationException("Значение параметра", "Сообщение");
    }

    @GetMapping("/test/сonditions-not-met")
    public void throwMethodConditionsNotMetException() {
        throw new ConditionsNotMetException("Неверные данные");
    }

    @GetMapping("/test/duplicated-data")
    public void throwMethodDuplicatedDataException() {
        throw new DuplicatedDataException("Дублирующиеся данные");
    }

    @GetMapping("/test/forbidden-exception")
    public void throwMethodForbiddenException() {
        throw new ForbiddenException("Ошибка доступа");
    }
}