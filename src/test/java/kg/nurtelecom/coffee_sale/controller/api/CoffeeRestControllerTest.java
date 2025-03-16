package kg.nurtelecom.coffee_sale.controller.api;

import kg.nurtelecom.coffee_sale.payload.request.CoffeeRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeService;
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
public class CoffeeRestControllerTest {

    @Mock
    private CoffeeService coffeeService;

    @InjectMocks
    private CoffeeRestController coffeeRestController;

    private CoffeeRequest coffeeRequest;
    private CoffeeResponse coffeeResponse;

    @BeforeEach
    public void setUp() {
        coffeeRequest = new CoffeeRequest(
                "Espresso",
                1,
                23.99f,
                2,
                45
        );

        coffeeResponse = new CoffeeResponse(
                "Espresso",
                1,
                23.99f,
                2,
                45,
                "Los Angeles"
        );

    }

    @Test
    public void testGetAllCoffees_Success() {
        List<CoffeeResponse> coffeeList = Collections.singletonList(coffeeResponse);
        when(coffeeService.findAll()).thenReturn(coffeeList);

        ResponseEntity<List<CoffeeResponse>> response = coffeeRestController.getAllCoffees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(coffeeService, times(1)).findAll();
    }

    @Test
    public void testGetAllCoffees_NoContent() {
        when(coffeeService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CoffeeResponse>> response = coffeeRestController.getAllCoffees();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(coffeeService, times(1)).findAll();
    }

    @Test
    public void testGetCoffeeByName_Success() {
        String coffeeName = "Espresso";
        when(coffeeService.findById(coffeeName)).thenReturn(coffeeResponse);

        ResponseEntity<CoffeeResponse> response = coffeeRestController.getCoffeeByName(coffeeName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Espresso", response.getBody().cofName());
        verify(coffeeService, times(1)).findById(coffeeName);
    }


    @Test
    public void testCreateCoffee_Success() {
        when(coffeeService.create(any(CoffeeRequest.class))).thenReturn(coffeeResponse);

        ResponseEntity<String> response = coffeeRestController.createCoffee(coffeeRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Coffee was saved successfully!", response.getBody());
        verify(coffeeService, times(1)).create(any(CoffeeRequest.class));
    }

    @Test
    public void testUpdateCoffee_Success() {
        String coffeeName = "Espresso";
        when(coffeeService.update(eq(coffeeName), any(CoffeeRequest.class))).thenReturn(coffeeResponse);

        ResponseEntity<String> response = coffeeRestController.updateCoffee(coffeeName, coffeeRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Coffee was updated successfully", response.getBody());
        verify(coffeeService, times(1)).update(eq(coffeeName), any(CoffeeRequest.class));
    }

    @Test
    public void testDeleteCoffee_Success() {
        String coffeeName = "Espresso";
        doNothing().when(coffeeService).delete(coffeeName);

        ResponseEntity<String> response = coffeeRestController.deleteCoffee(coffeeName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Coffee was deleted successfully!", response.getBody());
        verify(coffeeService, times(1)).delete(coffeeName);
    }

    @Test
    public void testDeleteCoffee_Error() {
        String coffeeName = "Espresso";
        doThrow(new RuntimeException("Deletion failed")).when(coffeeService).delete(coffeeName);

        ResponseEntity<String> response = coffeeRestController.deleteCoffee(coffeeName);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting coffee: Deletion failed", response.getBody());
        verify(coffeeService, times(1)).delete(coffeeName);
    }
}
