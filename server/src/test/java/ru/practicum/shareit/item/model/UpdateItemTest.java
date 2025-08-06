package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UpdateItemTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        UpdateItem updateItem = new UpdateItem("Test name", "Description", true);
        String json = objectMapper.writeValueAsString(updateItem);
        assertThat(json).contains("\"name\":\"Test name\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"name\":\"Test name\",\"description\":\"Description\"}";
        UpdateItem updateItem = objectMapper.readValue(json, UpdateItem.class);
        assertThat(updateItem.getName()).isEqualTo("Test name");
    }

    @Test
    void testHasValidName() {
        UpdateItem valid = new UpdateItem("Name", "Description", true);
        assertThat(valid.hasValidName()).isTrue();

        UpdateItem nullName = new UpdateItem(null, "Description", true);
        assertThat(nullName.hasValidName()).isFalse();

        UpdateItem emptyName = new UpdateItem("", "Description", true);
        assertThat(emptyName.hasValidName()).isFalse();

        UpdateItem blankName = new UpdateItem("   ", "Description", true);
        assertThat(blankName.hasValidName()).isFalse();
    }

    @Test
    void testHasValidDescription() {
        UpdateItem valid = new UpdateItem("Name", "Description", true);
        assertThat(valid.hasValidDescription()).isTrue();

        UpdateItem nullDescription = new UpdateItem("Name", null, true);
        assertThat(nullDescription.hasValidDescription()).isFalse();

        UpdateItem emptyDescription = new UpdateItem("Name", "", true);
        assertThat(emptyDescription.hasValidDescription()).isFalse();

        UpdateItem blankDescription = new UpdateItem("Name", "   ", true);
        assertThat(blankDescription.hasValidDescription()).isFalse();
    }

    @Test
    void testHasValidAvailable() {
        UpdateItem validTrue = new UpdateItem("Name", "Description", true);
        assertThat(validTrue.hasValidAvailable()).isTrue();

        UpdateItem validFalse = new UpdateItem("Name", "Description", false);
        assertThat(validFalse.hasValidAvailable()).isTrue();

        UpdateItem nullAvailable = new UpdateItem("Name", "Description", null);
        assertThat(nullAvailable.hasValidAvailable()).isFalse();
    }
}
