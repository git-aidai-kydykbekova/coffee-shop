package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.Supplier;
import kg.nurtelecom.coffee_sale.payload.request.SupplierRequest;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.jpa.h2.SupplierH2ServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.SupplierPostgresServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierRepositoryTest {

    @Mock
    private SupplierPostgresServiceJPA supplierPostgresServiceJPA;

    @Mock
    private SupplierH2ServiceJPA supplierH2ServiceJPA;

    @InjectMocks
    private SupplierRepository supplierRepository;

    private Supplier supplier;
    private SupplierRequest supplierRequest;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setSupId(1);
        supplier.setSupName("Test Supplier");
        supplier.setStreet("Test Street");
        supplier.setCity("Test City");
        supplier.setState("Test State");
        supplier.setZip("12345");

        supplierRequest = new SupplierRequest(1, "Test Supplier", "Test Street", "Test City", "Test State", "12345");
    }

    @Test
    void findAll_ShouldReturnListOfSuppliers() {
        when(supplierPostgresServiceJPA.findAll()).thenReturn(List.of(supplier));
        List<SupplierResponse> responses = supplierRepository.findAll();
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals("Test Supplier", responses.get(0).supName());
    }

    @Test
    void findById_ShouldReturnSupplier_WhenExists() {
        when(supplierPostgresServiceJPA.findById(1)).thenReturn(Optional.of(supplier));
        SupplierResponse response = supplierRepository.findById(1);
        assertNotNull(response);
        assertEquals("Test Supplier", response.supName());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(supplierPostgresServiceJPA.findById(2)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> supplierRepository.findById(2));
    }

    @Test
    void create_ShouldSaveSupplierAndReturnResponse() {
        when(supplierPostgresServiceJPA.save(any(Supplier.class))).thenReturn(supplier);
        SupplierResponse response = supplierRepository.create(supplierRequest);
        assertNotNull(response);
        assertEquals("Test Supplier", response.supName());
    }

    @Test
    void update_ShouldUpdateExistingSupplier() {
        when(supplierPostgresServiceJPA.findById(1)).thenReturn(Optional.of(supplier));
        when(supplierPostgresServiceJPA.save(any(Supplier.class))).thenReturn(supplier);

        SupplierResponse response = supplierRepository.update(1, supplierRequest);
        assertNotNull(response);
        assertEquals("Test Supplier", response.supName());
        verify(supplierH2ServiceJPA, times(1)).save(any(Supplier.class));
    }

    @Test
    void delete_ShouldRemoveSupplier_WhenExists() {
        when(supplierPostgresServiceJPA.existsById(1)).thenReturn(true);
        doNothing().when(supplierPostgresServiceJPA).deleteById(1);
        doNothing().when(supplierH2ServiceJPA).deleteById(1);

        assertDoesNotThrow(() -> supplierRepository.delete(1));
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(supplierPostgresServiceJPA.existsById(2)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> supplierRepository.delete(2));
    }
}
