package kg.nurtelecom.coffee_sale.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kg.nurtelecom.coffee_sale.entity.MerchandiseInventory;
import kg.nurtelecom.coffee_sale.payload.request.MerchandiseInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.MerchandiseInventoryResponse;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.MerchandiseInventoryPostgresServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MerchandiseInventoryRepositoryTest {

    @Mock
    private MerchandiseInventoryPostgresServiceJPA merchandiseInventoryServiceJPA;

    @InjectMocks
    private MerchandiseInventoryRepository merchandiseInventoryRepository;

    private MerchandiseInventory inventoryItem;
    private MerchandiseInventoryRequest request;

    @BeforeEach
    void setUp() {
        inventoryItem = new MerchandiseInventory();
        inventoryItem.setItemId("item123");
        inventoryItem.setItemName("Coffee Beans");
        inventoryItem.setSupId(1);
        inventoryItem.setQuantity(10);
        inventoryItem.setDate(LocalDate.now());

        request = new MerchandiseInventoryRequest("item123", "Coffee Beans", 1, 10, LocalDate.now());
    }

    @Test
    void testFindAll() {
        when(merchandiseInventoryServiceJPA.findAll()).thenReturn(List.of(inventoryItem));

        List<MerchandiseInventoryResponse> responses = merchandiseInventoryRepository.findAll();

        assertEquals(1, responses.size());
        assertEquals("item123", responses.get(0).itemId());
    }

    @Test
    void testFindById_ItemExists() {
        when(merchandiseInventoryServiceJPA.findById("item123")).thenReturn(Optional.of(inventoryItem));

        MerchandiseInventoryResponse response = merchandiseInventoryRepository.findById("item123");

        assertNotNull(response);
        assertEquals("item123", response.itemId());
    }

    @Test
    void testFindById_ItemNotFound() {
        when(merchandiseInventoryServiceJPA.findById("item123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> merchandiseInventoryRepository.findById("item123"));
    }

    @Test
    void testCreate_ItemDoesNotExist() {
        when(merchandiseInventoryServiceJPA.existsById("item123")).thenReturn(false);
        when(merchandiseInventoryServiceJPA.save(any())).thenReturn(inventoryItem);

        MerchandiseInventoryResponse response = merchandiseInventoryRepository.create(request);

        assertNotNull(response);
        assertEquals("item123", response.itemId());
    }

    @Test
    void testCreate_ItemAlreadyExists() {
        when(merchandiseInventoryServiceJPA.existsById("item123")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> merchandiseInventoryRepository.create(request));
    }

    @Test
    void testUpdate_ItemExists() {
        when(merchandiseInventoryServiceJPA.findById("item123")).thenReturn(Optional.of(inventoryItem));
        when(merchandiseInventoryServiceJPA.save(any())).thenReturn(inventoryItem);

        MerchandiseInventoryResponse response = merchandiseInventoryRepository.update("item123", request);

        assertNotNull(response);
        assertEquals("item123", response.itemId());
    }

    @Test
    void testUpdate_ItemNotFound() {
        when(merchandiseInventoryServiceJPA.findById("item123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> merchandiseInventoryRepository.update("item123", request));
    }

    @Test
    void testDelete_ItemExists() {
        when(merchandiseInventoryServiceJPA.existsById("item123")).thenReturn(true);
        doNothing().when(merchandiseInventoryServiceJPA).deleteById("item123");

        assertDoesNotThrow(() -> merchandiseInventoryRepository.delete("item123"));
    }

    @Test
    void testDelete_ItemNotFound() {
        when(merchandiseInventoryServiceJPA.existsById("item123")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> merchandiseInventoryRepository.delete("item123"));
    }
}
