package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestController.class)
@Import(ErrorHandler.class)
@ImportAutoConfiguration({
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Не найдено"));
    }

    @Test
    void handleValidationException() throws Exception {
        mockMvc.perform(get("/test/validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректное значение параметра " +
                        "Значение параметра"));
    }

    @Test
    void handleMethodConditionsNotMetException() throws Exception {
        mockMvc.perform(get("/test/сonditions-not-met"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Неверные данные"));
    }

    @Test
    void handleMethodDuplicatedDataException() throws Exception {
        mockMvc.perform(get("/test/duplicated-data"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Дублирующиеся данные"));
    }

    @Test
    void handleMethodForbiddenException() throws Exception {
        mockMvc.perform(get("/test/forbidden-exception"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Ошибка доступа"));
    }
}