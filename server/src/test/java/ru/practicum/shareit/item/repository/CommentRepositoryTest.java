package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Item item1;
    private Item item2;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;

    @BeforeEach
    void setUp() {
        user = new User(null, "Test User", "user@example.com");
        user = userRepository.save(user);

        item1 = new Item(null, "Item 1", "Description 1", true, user, null);
        item1 = itemRepository.save(item1);

        item2 = new Item(null, "Item 2", "Description 2", true, user, null);
        item2 = itemRepository.save(item2);

        comment1 = new Comment();
        comment1.setItem(item1);
        comment1.setAuthor(user);
        comment1.setText("Comment 1 for Item 1");
        comment1.setCreated(LocalDateTime.now());
        commentRepository.save(comment1);

        comment2 = new Comment();
        comment2.setItem(item1);
        comment2.setAuthor(user);
        comment2.setText("Comment 2 for Item 1");
        comment2.setCreated(LocalDateTime.now());
        commentRepository.save(comment2);

        comment3 = new Comment();
        comment3.setItem(item2);
        comment3.setAuthor(user);
        comment3.setText("Comment 1 for Item 2");
        comment3.setCreated(LocalDateTime.now());
        commentRepository.save(comment3);
    }

    @Test
    void findByItemId_ShouldReturnCommentsForGivenItem() {
        List<Comment> commentsForItem1 = commentRepository.findByItemId(item1.getId());

        assertNotNull(commentsForItem1);
        assertEquals(2, commentsForItem1.size());
        assertTrue(commentsForItem1.stream().allMatch(c -> c.getItem().getId().equals(item1.getId())));
    }

    @Test
    void findByItemId_In_ShouldReturnCommentsForMultipleItems() {
        List<Long> itemIds = List.of(item1.getId(), item2.getId());
        List<Comment> comments = commentRepository.findByItemIdIn(itemIds);

        assertNotNull(comments);
        assertEquals(3, comments.size());
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Comment 1 for Item 2")));
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Comment 1 for Item 1")));
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Comment 2 for Item 1")));
    }

    @Test
    void findByItemId_ShouldReturnEmptyListWhenNoComments() {
        List<Comment> comments = commentRepository.findByItemId(9999L);
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void findByItemId_In_ShouldReturnEmptyListWhenNoMatchingItems() {
        List<Comment> comments = commentRepository.findByItemIdIn(List.of(9999L, 8888L));
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }
}
