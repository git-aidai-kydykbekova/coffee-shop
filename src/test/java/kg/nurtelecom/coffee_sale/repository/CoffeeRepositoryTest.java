package kg.nurtelecom.coffee_sale.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import kg.nurtelecom.coffee_sale.entity.Coffee;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeResponse;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeePostgresServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.SupplierPostgresServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class CoffeeRepositoryTest {

    @Mock
    private CoffeePostgresServiceJPA coffeeServiceJPA;

    @Mock
    private SupplierPostgresServiceJPA supplierServiceJPA;

    @InjectMocks
    private CoffeeRepository coffeeRepository;

    private Coffee coffee;
    private CoffeeRequest coffeeRequest;

    @BeforeEach
    void setUp() {
        coffee = new Coffee();
        coffee.setCofName("Espresso");
        coffee.setSupId(1);
        coffee.setPrice(5.0f);
        coffee.setSales(100);
        coffee.setTotal(200);

        coffeeRequest = new CoffeeRequest("Espresso", 1, 5.0f, 100, 200);
    }

    @Test
    void testFindAll() {
        List<Coffee> coffeeList = new ArrayList<>();
        coffeeList.add(coffee);
        when(coffeeServiceJPA.findAll()).thenReturn(coffeeList);

        List<CoffeeResponse> result = coffeeRepository.findAll();
        assertEquals(1, result.size());
        assertEquals("Espresso", result.get(0).cofName());
    }

    @Test
    void testFindById_CoffeeExists() {
        when(coffeeServiceJPA.findById("Espresso")).thenReturn(Optional.of(coffee));

        CoffeeResponse result = coffeeRepository.findById("Espresso");
        assertNotNull(result);
        assertEquals("Espresso", result.cofName());
    }

    @Test
    void testFindById_CoffeeNotFound() {
        when(coffeeServiceJPA.findById("Espresso")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> coffeeRepository.findById("Espresso"));
        assertEquals("Coffee not found with name: Espresso", exception.getMessage());
    }

    @Test
    void testCreate_CoffeeSuccess() {
        when(supplierServiceJPA.existsById(1)).thenReturn(true);
        when(coffeeServiceJPA.existsById("Espresso")).thenReturn(false);
        when(coffeeServiceJPA.save(any(Coffee.class))).thenReturn(coffee);

        CoffeeResponse result = coffeeRepository.create(coffeeRequest);
        assertNotNull(result);
        assertEquals("Espresso", result.cofName());
    }

    @Test
    void testCreate_SupplierNotFound() {
        when(supplierServiceJPA.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> coffeeRepository.create(coffeeRequest));
        assertEquals("Supplier not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdate_CoffeeExists() {
        when(supplierServiceJPA.existsById(1)).thenReturn(true);
        when(coffeeServiceJPA.findById("Espresso")).thenReturn(Optional.of(coffee));
        when(coffeeServiceJPA.save(any(Coffee.class))).thenReturn(coffee);

        CoffeeResponse result = coffeeRepository.update("Espresso", coffeeRequest);
        assertNotNull(result);
        assertEquals("Espresso", result.cofName());
    }

    @Test
    void testDelete_CoffeeExists() {
        when(coffeeServiceJPA.existsById("Espresso")).thenReturn(true);
        doNothing().when(coffeeServiceJPA).deleteById("Espresso");

        assertDoesNotThrow(() -> coffeeRepository.delete("Espresso"));
    }

    @Test
    void testDelete_CoffeeNotFound() {
        when(coffeeServiceJPA.existsById("Espresso")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> coffeeRepository.delete("Espresso"));
        assertEquals("Coffee not found with name: Espresso", exception.getMessage());
    }
}

