package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.Coffee;
import kg.nurtelecom.coffee_sale.entity.CoffeeInventory;
import kg.nurtelecom.coffee_sale.entity.Supplier;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeInventoryResponse;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeeInventoryPostgresServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeePostgresServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.SupplierPostgresServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoffeeInventoryRepositoryTest {

    @Mock
    private CoffeeInventoryPostgresServiceJPA coffeeInventoryServiceJPA;

    @Mock
    private CoffeePostgresServiceJPA coffeeServiceJPA;

    @Mock
    private SupplierPostgresServiceJPA supplierServiceJPA;

    @InjectMocks
    private CoffeeInventoryRepository coffeeInventoryRepository;

    private CoffeeInventory inventory;
    private CoffeeInventoryRequest request;
    private Coffee coffee;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        coffee = new Coffee();
        coffee.setCofName("Espresso");
        coffee.setPrice(5.0f);

        supplier = new Supplier();
        supplier.setSupId(1);
        supplier.setSupName("Best Supplier");

        inventory = new CoffeeInventory();
        inventory.setId(1L);
        inventory.setWarehouseId(101);
        inventory.setCofName("Espresso");
        inventory.setSupId(1);
        inventory.setQuantity(50);
        inventory.setDate(LocalDate.now());

        request = new CoffeeInventoryRequest(101, "Espresso", 1, 100, LocalDate.now());
    }

    @Test
    void testFindAll() {
        when(coffeeInventoryServiceJPA.findAll()).thenReturn(List.of(inventory));
        List<CoffeeInventoryResponse> responses = coffeeInventoryRepository.findAll();
        assertEquals(1, responses.size());
        assertEquals("Espresso", responses.get(0).cofName());
    }

    @Test
    void testFindById() {
        when(coffeeInventoryServiceJPA.findById(1L)).thenReturn(Optional.of(inventory));
        CoffeeInventoryResponse response = coffeeInventoryRepository.findById(1L);
        assertEquals("Espresso", response.cofName());
    }

    @Test
    void testFindById_NotFound() {
        when(coffeeInventoryServiceJPA.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> coffeeInventoryRepository.findById(2L));
    }

    @Test
    void testCreate() {
        when(coffeeServiceJPA.findById("Espresso")).thenReturn(Optional.of(coffee));
        when(supplierServiceJPA.findById(1)).thenReturn(Optional.of(supplier));
        when(coffeeInventoryServiceJPA.save(any(CoffeeInventory.class))).thenReturn(inventory);

        CoffeeInventoryResponse response = coffeeInventoryRepository.create(request);
        assertEquals("Espresso", response.cofName());
    }

    @Test
    void testCreate_CoffeeNotFound() {
        when(coffeeServiceJPA.findById("Espresso")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> coffeeInventoryRepository.create(request));
    }

    @Test
    void testCreate_SupplierNotFound() {
        when(coffeeServiceJPA.findById("Espresso")).thenReturn(Optional.of(coffee));
        when(supplierServiceJPA.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> coffeeInventoryRepository.create(request));
    }

    @Test
    void testUpdate() {
        when(coffeeInventoryServiceJPA.findById(1L)).thenReturn(Optional.of(inventory));
        when(coffeeServiceJPA.findById("Espresso")).thenReturn(Optional.of(coffee));
        when(supplierServiceJPA.findById(1)).thenReturn(Optional.of(supplier));
        when(coffeeInventoryServiceJPA.save(any(CoffeeInventory.class))).thenReturn(inventory);

        CoffeeInventoryResponse response = coffeeInventoryRepository.update(1L, request);
        assertEquals("Espresso", response.cofName());
    }

    @Test
    void testUpdate_NotFound() {
        when(coffeeInventoryServiceJPA.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> coffeeInventoryRepository.update(1L, request));
    }

    @Test
    void testDelete() {
        when(coffeeInventoryServiceJPA.existsById(1L)).thenReturn(true);
        doNothing().when(coffeeInventoryServiceJPA).deleteById(1L);

        assertDoesNotThrow(() -> coffeeInventoryRepository.delete(1L));
        verify(coffeeInventoryServiceJPA, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(coffeeInventoryServiceJPA.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> coffeeInventoryRepository.delete(1L));
    }
}
