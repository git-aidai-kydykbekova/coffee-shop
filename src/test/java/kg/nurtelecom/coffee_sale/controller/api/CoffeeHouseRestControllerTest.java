package kg.nurtelecom.coffee_sale.controller.api;

import kg.nurtelecom.coffee_sale.payload.request.CoffeeHouseRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeHouseResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeHouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CoffeeHouseRestControllerTest {

    @Mock
    private CoffeeHouseService coffeeHouseService;

    @InjectMocks
    private CoffeeHouseRestController coffeeHouseRestController;

    private CoffeeHouseRequest coffeeHouseRequest;
    private CoffeeHouseResponse coffeeHouseResponse;

    @BeforeEach
    public void setUp() {
        coffeeHouseRequest = new CoffeeHouseRequest(
                3,
                "Bishkek",
                1,
                2,
                23
        );


        coffeeHouseResponse = new CoffeeHouseResponse(
                1,
                "Bishkek",
                1,
                2,
                23
        );

    }

    @Test
    public void testGetAllCoffeeHouses_Success() {
        List<CoffeeHouseResponse> coffeeHouseList = Collections.singletonList(coffeeHouseResponse);
        when(coffeeHouseService.findAll()).thenReturn(coffeeHouseList);

        ResponseEntity<List<CoffeeHouseResponse>> response = coffeeHouseRestController.getAllCoffeeHouses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(coffeeHouseService, times(1)).findAll();
    }

    @Test
    public void testGetAllCoffeeHouses_NoContent() {
        when(coffeeHouseService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CoffeeHouseResponse>> response = coffeeHouseRestController.getAllCoffeeHouses();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(coffeeHouseService, times(1)).findAll();
    }

    @Test
    public void testGetCoffeeHouseById_Success() {
        int coffeeHouseId = 1;
        when(coffeeHouseService.findById(coffeeHouseId)).thenReturn(coffeeHouseResponse);

        ResponseEntity<CoffeeHouseResponse> response = coffeeHouseRestController.getCoffeeHouseById(coffeeHouseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(coffeeHouseService, times(1)).findById(coffeeHouseId);
    }

    @Test
    public void testCreateCoffeeHouse_Success() {
        when(coffeeHouseService.create(any(CoffeeHouseRequest.class))).thenReturn(coffeeHouseResponse);

        ResponseEntity<String> response = coffeeHouseRestController.createCoffeeHouse(coffeeHouseRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Coffee house was saved successfully!", response.getBody());
        verify(coffeeHouseService, times(1)).create(any(CoffeeHouseRequest.class));
    }

    @Test
    public void testUpdateCoffeeHouse_Success() {
        int coffeeHouseId = 1;
        when(coffeeHouseService.update(eq(coffeeHouseId), any(CoffeeHouseRequest.class))).thenReturn(coffeeHouseResponse);

        ResponseEntity<String> response = coffeeHouseRestController.updateCoffeeHouse(coffeeHouseId, coffeeHouseRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Coffee house was updated successfully", response.getBody());
        verify(coffeeHouseService, times(1)).update(eq(coffeeHouseId), any(CoffeeHouseRequest.class));
    }

    @Test
    public void testDeleteCoffeeHouse_Success() {
        int coffeeHouseId = 1;
        doNothing().when(coffeeHouseService).delete(coffeeHouseId);

        ResponseEntity<String> response = coffeeHouseRestController.deleteCoffeeHouse(coffeeHouseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Coffee house was deleted successfully!", response.getBody());
        verify(coffeeHouseService, times(1)).delete(coffeeHouseId);
    }

    @Test
    public void testDeleteCoffeeHouse_Error() {
        int coffeeHouseId = 1;
        doThrow(new RuntimeException("Deletion failed")).when(coffeeHouseService).delete(coffeeHouseId);

        ResponseEntity<String> response = coffeeHouseRestController.deleteCoffeeHouse(coffeeHouseId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting coffee house: Deletion failed", response.getBody());
        verify(coffeeHouseService, times(1)).delete(coffeeHouseId);
    }
}
