package kg.nurtelecom.coffee_sale.controller.api;

import kg.nurtelecom.coffee_sale.payload.request.SupplierRequest;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.SupplierService;
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
public class SupplierRestControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierRestController supplierRestController;

    private SupplierRequest supplierRequest;
    private SupplierResponse supplierResponse;

    @BeforeEach
    public void setUp() {
        supplierRequest = new SupplierRequest(2, "123 Street", "City", "State", "12345", "222");
        supplierResponse = new SupplierResponse(1, "Supplier Name", "123 Street", "City", "State", "12345", List.of("Coffee A", "Coffee B"));
    }

    @Test
    public void testGetAllSuppliers_Success() {
        List<SupplierResponse> supplierList = Collections.singletonList(supplierResponse);
        when(supplierService.findAll()).thenReturn(supplierList);

        ResponseEntity<List<SupplierResponse>> response = supplierRestController.getAllSuppliers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(supplierService, times(1)).findAll();
    }

    @Test
    public void testGetAllSuppliers_NoContent() {
        when(supplierService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<SupplierResponse>> response = supplierRestController.getAllSuppliers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(supplierService, times(1)).findAll();
    }

    @Test
    public void testGetSupplierById_Success() {
        Integer supplierId = 1;
        when(supplierService.findById(supplierId)).thenReturn(supplierResponse);

        ResponseEntity<SupplierResponse> response = supplierRestController.getSupplierById(supplierId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(supplierId, response.getBody().supId());
        verify(supplierService, times(1)).findById(supplierId);
    }

    @Test
    public void testCreateSupplier_Success() {
        when(supplierService.create(any(SupplierRequest.class))).thenReturn(supplierResponse);

        ResponseEntity<String> response = supplierRestController.createSupplier(supplierRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Supplier was created successfully!", response.getBody());
        verify(supplierService, times(1)).create(any(SupplierRequest.class));
    }

    @Test
    public void testUpdateSupplier_Success() {
        Integer supplierId = 1;
        when(supplierService.update(eq(supplierId), any(SupplierRequest.class))).thenReturn(supplierResponse);

        ResponseEntity<String> response = supplierRestController.updateSupplier(supplierId, supplierRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Supplier was updated successfully", response.getBody());
        verify(supplierService, times(1)).update(eq(supplierId), any(SupplierRequest.class));
    }

    @Test
    public void testDeleteSupplier_Success() {
        Integer supplierId = 1;
        doNothing().when(supplierService).delete(supplierId);

        ResponseEntity<String> response = supplierRestController.deleteSupplier(supplierId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Supplier was deleted successfully!", response.getBody());
        verify(supplierService, times(1)).delete(supplierId);
    }

    @Test
    public void testDeleteSupplier_Error() {
        Integer supplierId = 1;
        doThrow(new RuntimeException("Deletion failed")).when(supplierService).delete(supplierId);

        ResponseEntity<String> response = supplierRestController.deleteSupplier(supplierId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting supplier: Deletion failed", response.getBody());
        verify(supplierService, times(1)).delete(supplierId);
    }
}
