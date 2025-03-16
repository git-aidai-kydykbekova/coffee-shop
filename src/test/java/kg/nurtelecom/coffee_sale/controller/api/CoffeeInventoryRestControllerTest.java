package kg.nurtelecom.coffee_sale.controller.api;

import kg.nurtelecom.coffee_sale.payload.request.CoffeeInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeInventoryResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeInventoryService;
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
public class CoffeeInventoryRestControllerTest {

    @Mock
    private CoffeeInventoryService coffeeInventoryService;

    @InjectMocks
    private CoffeeInventoryRestController coffeeInventoryRestController;

    private CoffeeInventoryRequest coffeeInventoryRequest;
    private CoffeeInventoryResponse coffeeInventoryResponse;

    @BeforeEach
    public void setUp() {
        coffeeInventoryRequest = new CoffeeInventoryRequest(
                1,
                "Cuppuchino",
                2,
                23,
                LocalDate.now()
        );

        coffeeInventoryResponse = new CoffeeInventoryResponse(
                1l,
                3,
                "Cuppuchino",
                2,
                2,
                LocalDate.now(),
                23f,
                "Aidai"
        );
    }

    @Test
    public void testGetAllInventory_Success() {
        List<CoffeeInventoryResponse> inventoryList = Collections.singletonList(coffeeInventoryResponse);
        when(coffeeInventoryService.findAll()).thenReturn(inventoryList);

        ResponseEntity<List<CoffeeInventoryResponse>> response = coffeeInventoryRestController.getAllInventory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(coffeeInventoryService, times(1)).findAll();
    }

    @Test
    public void testGetAllInventory_NoContent() {
        when(coffeeInventoryService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CoffeeInventoryResponse>> response = coffeeInventoryRestController.getAllInventory();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(coffeeInventoryService, times(1)).findAll();
    }

    @Test
    public void testGetInventoryById_Success() {
        long inventoryId = 1L;
        when(coffeeInventoryService.findById(inventoryId)).thenReturn(coffeeInventoryResponse);

        ResponseEntity<CoffeeInventoryResponse> response = coffeeInventoryRestController.getInventoryById(inventoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(coffeeInventoryService, times(1)).findById(inventoryId);
    }


    @Test
    public void testCreateInventory_Success() {
        when(coffeeInventoryService.create(any(CoffeeInventoryRequest.class))).thenReturn(coffeeInventoryResponse);

        ResponseEntity<String> response = coffeeInventoryRestController.createInventory(coffeeInventoryRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Inventory record was created successfully!", response.getBody());
        verify(coffeeInventoryService, times(1)).create(any(CoffeeInventoryRequest.class));
    }

    @Test
    public void testUpdateInventory_Success() {
        long inventoryId = 1L;
        when(coffeeInventoryService.update(eq(inventoryId), any(CoffeeInventoryRequest.class))).thenReturn(coffeeInventoryResponse);

        ResponseEntity<String> response = coffeeInventoryRestController.updateInventory(inventoryId, coffeeInventoryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inventory record was updated successfully", response.getBody());
        verify(coffeeInventoryService, times(1)).update(eq(inventoryId), any(CoffeeInventoryRequest.class));
    }

    @Test
    public void testDeleteInventory_Success() {
        long inventoryId = 1L;
        doNothing().when(coffeeInventoryService).delete(inventoryId);

        ResponseEntity<String> response = coffeeInventoryRestController.deleteInventory(inventoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inventory record was deleted successfully!", response.getBody());
        verify(coffeeInventoryService, times(1)).delete(inventoryId);
    }

    @Test
    public void testDeleteInventory_Error() {
        long inventoryId = 1L;
        doThrow(new RuntimeException("Deletion failed")).when(coffeeInventoryService).delete(inventoryId);

        ResponseEntity<String> response = coffeeInventoryRestController.deleteInventory(inventoryId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting inventory record: Deletion failed", response.getBody());
        verify(coffeeInventoryService, times(1)).delete(inventoryId);
    }
}
