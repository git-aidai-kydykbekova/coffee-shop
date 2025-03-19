package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.CoffeeHouse;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeHouseRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeHouseResponse;
import kg.nurtelecom.coffee_sale.service.jpa.h2.CoffeeHouseH2ServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeeHousePostgresServiceJPA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoffeeHouseRepositoryTest {

    @Mock
    private CoffeeHousePostgresServiceJPA coffeeHouseServiceJPA;

    @Mock
    private CoffeeHouseH2ServiceJPA coffeeHouseH2ServiceJPA;

    @InjectMocks
    private CoffeeHouseRepository coffeeHouseRepository;

    @Test
    void findAll_ShouldReturnAllCoffeeHouses() {
        List<CoffeeHouse> coffeeHouses = List.of(
                new CoffeeHouse(1, "New York", 100, 50, 150),
                new CoffeeHouse(2, "London", 80, 40, 120)
        );

        when(coffeeHouseServiceJPA.findAll()).thenReturn(coffeeHouses);

        List<CoffeeHouseResponse> result = coffeeHouseRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("New York", result.get(0).city());
        assertEquals(150, result.get(0).total());
    }

    @Test
    void findById_ShouldReturnCoffeeHouse_WhenExists() {
        CoffeeHouse coffeeHouse = new CoffeeHouse(1, "New York", 100, 50, 150);

        when(coffeeHouseServiceJPA.findById(1)).thenReturn(Optional.of(coffeeHouse));

        CoffeeHouseResponse result = coffeeHouseRepository.findById(1);

        assertNotNull(result);
        assertEquals("New York", result.city());
    }

    @Test
    void findById_ShouldThrowException_WhenNotExists() {
        when(coffeeHouseServiceJPA.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> coffeeHouseRepository.findById(1));
    }

    @Test
    void create_ShouldSaveNewCoffeeHouse_WhenNotExists() {
        CoffeeHouseRequest request = new CoffeeHouseRequest(1, "New York", 100, 50, null);
        CoffeeHouse savedCoffeeHouse = new CoffeeHouse(1, "New York", 100, 50, 150);

        when(coffeeHouseServiceJPA.existsById(1)).thenReturn(false);
        when(coffeeHouseServiceJPA.save(any(CoffeeHouse.class))).thenReturn(savedCoffeeHouse);

        CoffeeHouseResponse result = coffeeHouseRepository.create(request);

        assertNotNull(result);
        assertEquals(150, result.total());
        verify(coffeeHouseH2ServiceJPA).save(any(CoffeeHouse.class));
    }

    @Test
    void create_ShouldThrowException_WhenCoffeeHouseAlreadyExists() {
        CoffeeHouseRequest request = new CoffeeHouseRequest(1, "New York", 100, 50, null);

        when(coffeeHouseServiceJPA.existsById(1)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> coffeeHouseRepository.create(request));
    }

    @Test
    void update_ShouldModifyExistingCoffeeHouse_WhenExists() {
        CoffeeHouse existing = new CoffeeHouse(1, "New York", 100, 50, 150);
        CoffeeHouseRequest request = new CoffeeHouseRequest(1, "Los Angeles", 200, 100, null);
        CoffeeHouse updated = new CoffeeHouse(1, "Los Angeles", 200, 100, 300);

        when(coffeeHouseServiceJPA.findById(1)).thenReturn(Optional.of(existing));
        when(coffeeHouseServiceJPA.save(any(CoffeeHouse.class))).thenReturn(updated);

        CoffeeHouseResponse result = coffeeHouseRepository.update(1, request);

        assertEquals("Los Angeles", result.city());
        assertEquals(300, result.total());
        verify(coffeeHouseH2ServiceJPA).save(any(CoffeeHouse.class));
    }

    @Test
    void update_ShouldThrowException_WhenCoffeeHouseNotFound() {
        CoffeeHouseRequest request = new CoffeeHouseRequest(1, "Los Angeles", 200, 100, null);

        when(coffeeHouseServiceJPA.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> coffeeHouseRepository.update(1, request));
    }

    @Test
    void delete_ShouldRemoveCoffeeHouse_WhenExists() {
        when(coffeeHouseServiceJPA.existsById(1)).thenReturn(true);

        coffeeHouseRepository.delete(1);

        verify(coffeeHouseServiceJPA).deleteById(1);
        verify(coffeeHouseH2ServiceJPA).deleteById(1);
    }

    @Test
    void delete_ShouldThrowException_WhenCoffeeHouseNotFound() {
        when(coffeeHouseServiceJPA.existsById(1)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> coffeeHouseRepository.delete(1));
    }
}

