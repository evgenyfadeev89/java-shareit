package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.AllItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NewItem;
import ru.practicum.shareit.item.model.UpdateItem;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Test
    void testToItemDto() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Request request = new Request(2L, "RequestDesc", owner, null);
        Item item = new Item(10L, "ItemName", "ItemDescription", true, owner, request);

        ItemDto dto = itemMapper.toItemDto(item);

        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());
        assertEquals(owner.getId(), dto.getOwner());
        assertEquals(request.getId(), dto.getRequest());
    }

    @Test
    void testToAllItemDto() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Request request = new Request(2L, "RequestDesc", owner, null);
        Item item = new Item(20L,
                "ItemAllName",
                "ItemAllDescription",
                false,
                owner,
                request);

        AllItemDto dto = itemMapper.toAllItemDto(item);

        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());
        assertEquals(owner.getId(), dto.getOwner());
        assertEquals(request.getId(), dto.getRequest());
    }

    @Test
    void testToItemFromNewItem() {
        NewItem newItem = new NewItem("NewItemName",
                "NewItemDescription",
                true,
                1L,
                2L);

        Item item = itemMapper.toItem(newItem);

        assertNull(item.getId());
        assertEquals(newItem.getName(), item.getName());
        assertEquals(newItem.getDescription(), item.getDescription());
        assertEquals(newItem.getAvailable(), item.getAvailable());
        assertNotNull(item.getOwner());
        assertEquals(newItem.getOwner(), item.getOwner().getId());
        assertNotNull(item.getRequest());
        assertEquals(newItem.getRequestId(), item.getRequest().getId());
    }

    @Test
    void testUpdateItemFieldsWithAfterMapping() {
        Item item = new Item();
        item.setId(100L);
        item.setName("OldName");
        item.setDescription("OldDescription");
        item.setAvailable(true);

        UpdateItem update = new UpdateItem("NewName", "NewDescription", false);

        itemMapper.updateItemFields(update, item);

        assertEquals("NewName", item.getName());
        assertEquals("NewDescription", item.getDescription());
        assertFalse(item.getAvailable());

        assertEquals(100L, item.getId());
        assertNull(item.getRequest());
    }

    @Test
    void testUpdateItemFieldsIgnoresNullValues() {
        Item item = new Item();
        item.setName("OldName");
        item.setDescription("OldDescription");
        item.setAvailable(true);

        UpdateItem update = new UpdateItem(null, null, null);

        itemMapper.updateItemFields(update, item);

        assertEquals("OldName", item.getName());
        assertEquals("OldDescription", item.getDescription());
        assertTrue(item.getAvailable());
    }

    @Test
    void testMapOwnerReturnsNullWhenIdIsNull() {
        User user = itemMapper.mapOwner(null);
        assertNull(user, "mapOwner должен вернуть null при передаче null");
    }

    @Test
    void testMapOwnerReturnsUserWithId() {
        Long id = 5L;
        User user = itemMapper.mapOwner(id);
        assertNotNull(user, "mapOwner не должен вернуть null");
        assertEquals(id, user.getId(), "mapOwner должен установить корректный id");
    }

    @Test
    void testMapRequestReturnsNullWhenIdIsNull() {
        Request request = itemMapper.mapRequest(null);
        assertNull(request, "mapRequest должен вернуть null при передаче null");
    }

    @Test
    void testMapRequestReturnsRequestWithId() {
        Long id = 10L;
        Request request = itemMapper.mapRequest(id);
        assertNotNull(request, "mapRequest не должен вернуть null");
        assertEquals(id, request.getId(), "mapRequest должен установить корректный id");
    }
}
