package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.Coffee;
import kg.nurtelecom.coffee_sale.entity.Supplier;
import kg.nurtelecom.coffee_sale.payload.request.SupplierRequest;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.SupplierService;
import kg.nurtelecom.coffee_sale.service.jpa.h2.SupplierH2ServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.SupplierPostgresServiceJPA;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SupplierRepository implements SupplierService {

    private final SupplierPostgresServiceJPA supplierPostgresServiceJPA;
    private final SupplierH2ServiceJPA supplierH2ServiceJPA;

    public SupplierRepository(SupplierPostgresServiceJPA supplierPostgresServiceJPA, SupplierH2ServiceJPA supplierH2ServiceJPA) {
        this.supplierPostgresServiceJPA = supplierPostgresServiceJPA;
        this.supplierH2ServiceJPA = supplierH2ServiceJPA;
    }


    @Override
    public List<SupplierResponse> findAll() {
        List<Supplier> suppliers = supplierPostgresServiceJPA.findAll();
        List<SupplierResponse> supplierResponses = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            SupplierResponse response = mapToSupplierResponse(supplier);
            supplierResponses.add(response);
        }

        return supplierResponses;
    }

    @Override
    public SupplierResponse findById(Integer id) {
        Optional<Supplier> supplierOptional = supplierPostgresServiceJPA.findById(id);
        if (supplierOptional.isPresent()) {
            return mapToSupplierResponse(supplierOptional.get());
        }
        throw new RuntimeException("Supplier not found with id: " + id);
    }

    @Override
    public SupplierResponse create(SupplierRequest supplierRequest) {
        Supplier supplier = new Supplier();
        updateSupplierFromRequest(supplier, supplierRequest);
        Supplier savedSupplier = supplierPostgresServiceJPA.save(supplier);
        return mapToSupplierResponse(savedSupplier);
    }

    @Override
    public SupplierResponse update(Integer id, SupplierRequest supplierRequest) {
        Optional<Supplier> supplierOptional = supplierPostgresServiceJPA.findById(id);
        if (supplierOptional.isPresent()) {
            Supplier supplier = supplierOptional.get();
            updateSupplierFromRequest(supplier, supplierRequest);

            Supplier updatedSupplier = supplierPostgresServiceJPA.save(supplier);
            supplierH2ServiceJPA.save(supplier);
            return mapToSupplierResponse(updatedSupplier);
        }
        throw new RuntimeException("Supplier not found with id: " + id);
    }

    @Override
    public void delete(Integer id) {
        if (supplierPostgresServiceJPA.existsById(id)) {
            supplierPostgresServiceJPA.deleteById(id);
            supplierH2ServiceJPA.deleteById(id);
        } else {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
    }

    private SupplierResponse mapToSupplierResponse(Supplier supplier) {
        List<String> coffeeNames = new ArrayList<>();

        if (supplier.getCoffees() != null) {
            for (Coffee coffee : supplier.getCoffees()) {
                coffeeNames.add(coffee.getCofName());
            }
        }

        return new SupplierResponse(
                supplier.getSupId(),
                supplier.getSupName(),
                supplier.getStreet(),
                supplier.getCity(),
                supplier.getState(),
                supplier.getZip(),
                coffeeNames
        );
    }

    private void updateSupplierFromRequest(Supplier supplier, SupplierRequest request) {
        if (request.supId() != null) {
            supplier.setSupId(request.supId());
        }
        supplier.setSupName(request.supName());
        supplier.setStreet(request.street());
        supplier.setCity(request.city());
        supplier.setState(request.state());
        supplier.setZip(request.zip());
    }
}