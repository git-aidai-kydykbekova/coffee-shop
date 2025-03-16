package kg.nurtelecom.coffee_sale.controller.api;

import kg.nurtelecom.coffee_sale.payload.request.MerchandiseInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.MerchandiseInventoryResponse;
import kg.nurtelecom.coffee_sale.service.MerchandiseInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MerchandiseInventoryRestControllerTest {

    @Mock
    private MerchandiseInventoryService merchandiseInventoryService;

    @InjectMocks
    private MerchandiseInventoryRestController merchandiseInventoryRestController;

    private MerchandiseInventoryRequest inventoryRequest;
    private MerchandiseInventoryResponse inventoryResponse;

    @BeforeEach
    public void setUp() {
        inventoryRequest = new MerchandiseInventoryRequest("item1", "Item Name", 1, 10, LocalDate.now());

        inventoryResponse = new MerchandiseInventoryResponse("item1", "Item Name", 1, 10, LocalDate.now(), "Supplier Name");
    }

    @Test
    public void testGetAllInventoryItems_Success() {
        List<MerchandiseInventoryResponse> inventoryList = Collections.singletonList(inventoryResponse);
        when(merchandiseInventoryService.findAll()).thenReturn(inventoryList);

        ResponseEntity<List<MerchandiseInventoryResponse>> response = merchandiseInventoryRestController.getAllInventoryItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(merchandiseInventoryService, times(1)).findAll();
    }

    @Test
    public void testGetAllInventoryItems_NoContent() {
        when(merchandiseInventoryService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<MerchandiseInventoryResponse>> response = merchandiseInventoryRestController.getAllInventoryItems();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(merchandiseInventoryService, times(1)).findAll();
    }

    @Test
    public void testGetInventoryItemById_Success() {
        String itemId = "item1";
        when(merchandiseInventoryService.findById(itemId)).thenReturn(inventoryResponse);

        ResponseEntity<MerchandiseInventoryResponse> response = merchandiseInventoryRestController.getInventoryItemById(itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("item1", response.getBody().itemId());
        verify(merchandiseInventoryService, times(1)).findById(itemId);
    }

    @Test
    public void testGetInventoryItemsBySupplier_Success() {
        Integer supId = 1;
        List<MerchandiseInventoryResponse> inventoryList = Collections.singletonList(inventoryResponse);
        when(merchandiseInventoryService.findBySupplier(supId)).thenReturn(inventoryList);

        ResponseEntity<List<MerchandiseInventoryResponse>> response = merchandiseInventoryRestController.getInventoryItemsBySupplier(supId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(merchandiseInventoryService, times(1)).findBySupplier(supId);
    }

    @Test
    public void testGetInventoryItemsBySupplier_NoContent() {
        Integer supId = 1;
        when(merchandiseInventoryService.findBySupplier(supId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<MerchandiseInventoryResponse>> response = merchandiseInventoryRestController.getInventoryItemsBySupplier(supId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(merchandiseInventoryService, times(1)).findBySupplier(supId);
    }

    @Test
    public void testCreateInventoryItem_Success() {
        when(merchandiseInventoryService.create(any(MerchandiseInventoryRequest.class))).thenReturn(inventoryResponse);

        ResponseEntity<String> response = merchandiseInventoryRestController.createInventoryItem(inventoryRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Inventory item was created successfully!", response.getBody());
        verify(merchandiseInventoryService, times(1)).create(any(MerchandiseInventoryRequest.class));
    }

    @Test
    public void testUpdateInventoryItem_Success() {
        String itemId = "item1";
        when(merchandiseInventoryService.update(eq(itemId), any(MerchandiseInventoryRequest.class))).thenReturn(inventoryResponse);

        ResponseEntity<String> response = merchandiseInventoryRestController.updateInventoryItem(itemId, inventoryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inventory item was updated successfully", response.getBody());
        verify(merchandiseInventoryService, times(1)).update(eq(itemId), any(MerchandiseInventoryRequest.class));
    }

    @Test
    public void testDeleteInventoryItem_Success() {
        String itemId = "item1";
        doNothing().when(merchandiseInventoryService).delete(itemId);

        ResponseEntity<String> response = merchandiseInventoryRestController.deleteInventoryItem(itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inventory item was deleted successfully!", response.getBody());
        verify(merchandiseInventoryService, times(1)).delete(itemId);
    }

    @Test
    public void testDeleteInventoryItem_Error() {
        String itemId = "item1";
        doThrow(new RuntimeException("Deletion failed")).when(merchandiseInventoryService).delete(itemId);

        ResponseEntity<String> response = merchandiseInventoryRestController.deleteInventoryItem(itemId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting inventory item: Deletion failed", response.getBody());
        verify(merchandiseInventoryService, times(1)).delete(itemId);
    }
}
