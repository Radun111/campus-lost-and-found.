package service;

import com.example.lostandfound.dto.ItemRequest;
import com.example.lostandfound.dto.ItemResponse;
import com.example.lostandfound.entity.Item;
import com.example.lostandfound.entity.ItemStatus;
import com.example.lostandfound.entity.User;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.repository.UserRepository;
import com.example.lostandfound.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_ShouldReturnItemResponse() {
        // 1. Setup test data
        ItemRequest request = new ItemRequest();
        request.setName("Lost Phone");
        request.setDescription("iPhone 13 Pro Max");
        request.setStatus("LOST");
        request.setReportedById(1L);

        User user = new User();
        user.setId(1L);

        Item savedItem = Item.builder()
                .id(1L)
                .name("Lost Phone")
                .status(ItemStatus.LOST)
                .reportedBy(user)
                .build();

        // 2. Define mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // 3. Execute the test
        ItemResponse response = itemService.createItem(request);

        // 4. Verify results
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Lost Phone", response.getName());
        assertEquals("LOST", response.getStatus());
    }
}