package kg.nurtelecom.coffee_sale.service;

import kg.nurtelecom.coffee_sale.payload.request.SupplierRequest;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;

import java.util.List;

public interface SupplierService {
    List<SupplierResponse> findAll();

    SupplierResponse findById(Integer id);

    SupplierResponse create(SupplierRequest supplierRequest);

    SupplierResponse update(Integer id, SupplierRequest supplierRequest);

    void delete(Integer id);


}