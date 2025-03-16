package kg.nurtelecom.coffee_sale.repository;


import kg.nurtelecom.coffee_sale.entity.Coffee;
import kg.nurtelecom.coffee_sale.entity.Supplier;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeService;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeePostgresServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.SupplierPostgresServiceJPA;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CoffeeRepository implements CoffeeService {

    private final CoffeePostgresServiceJPA coffeeServiceJPA;
    private final SupplierPostgresServiceJPA supplierServiceJPA;

    public CoffeeRepository(CoffeePostgresServiceJPA coffeeServiceJPA, SupplierPostgresServiceJPA supplierServiceJPA) {
        this.coffeeServiceJPA = coffeeServiceJPA;
        this.supplierServiceJPA = supplierServiceJPA;
    }

    @Override
    public List<CoffeeResponse> findAll() {
        List<Coffee> coffees = coffeeServiceJPA.findAll();
        List<CoffeeResponse> coffeeResponses = new ArrayList<>();
        for (Coffee coffee : coffees) {
            CoffeeResponse response = mapToCoffeeResponse(coffee);
            coffeeResponses.add(response);
        }

        return coffeeResponses;
    }

    @Override
    public CoffeeResponse findById(String coffeeName) {
        Optional<Coffee> coffeeOptional = coffeeServiceJPA.findById(coffeeName);
        if (coffeeOptional.isPresent()) {
            return mapToCoffeeResponse(coffeeOptional.get());
        }
        throw new RuntimeException("Coffee not found with name: " + coffeeName);
    }

    @Override
    public CoffeeResponse create(CoffeeRequest coffeeRequest) {
        if (!supplierServiceJPA.existsById(coffeeRequest.supId())) {
            throw new RuntimeException("Supplier not found with id: " + coffeeRequest.supId());
        }

        if (coffeeServiceJPA.existsById(coffeeRequest.cofName())) {
            throw new RuntimeException("Coffee with name '" + coffeeRequest.cofName() + "' already exists");
        }

        Coffee coffee = new Coffee();
        updateCoffeeFromRequest(coffee, coffeeRequest);

        Coffee savedCoffee = coffeeServiceJPA.save(coffee);
        return mapToCoffeeResponse(savedCoffee);
    }

    @Override
    public CoffeeResponse update(String coffeeName, CoffeeRequest coffeeRequest) {
        if (!supplierServiceJPA.existsById(coffeeRequest.supId())) {
            throw new RuntimeException("Supplier not found with id: " + coffeeRequest.supId());
        }

        Optional<Coffee> coffeeOptional = coffeeServiceJPA.findById(coffeeName);
        if (coffeeOptional.isPresent()) {
            Coffee coffee = coffeeOptional.get();
            if (!coffeeName.equals(coffeeRequest.cofName()) &&
                    coffeeServiceJPA.existsById(coffeeRequest.cofName())) {
                throw new RuntimeException("Coffee with name '" + coffeeRequest.cofName() + "' already exists");
            }

            if (!coffeeName.equals(coffeeRequest.cofName())) {
                Coffee newCoffee = new Coffee();
                updateCoffeeFromRequest(newCoffee, coffeeRequest);
                Coffee savedCoffee = coffeeServiceJPA.save(newCoffee);
                coffeeServiceJPA.deleteById(coffeeName);
                return mapToCoffeeResponse(savedCoffee);
            } else {
                updateCoffeeFromRequest(coffee, coffeeRequest);
                Coffee updatedCoffee = coffeeServiceJPA.save(coffee);
                return mapToCoffeeResponse(updatedCoffee);
            }
        }
        throw new RuntimeException("Coffee not found with name: " + coffeeName);
    }

    @Override
    public void delete(String coffeeName) {
        if (coffeeServiceJPA.existsById(coffeeName)) {
            coffeeServiceJPA.deleteById(coffeeName);
        } else {
            throw new RuntimeException("Coffee not found with name: " + coffeeName);
        }
    }

    private void updateCoffeeFromRequest(Coffee coffee, CoffeeRequest request) {
        coffee.setCofName(request.cofName());
        coffee.setSupId(request.supId());
        coffee.setPrice(request.price());
        if (request.sales() != null) {
            coffee.setSales(request.sales());
        }

        if (request.total() != null) {
            coffee.setTotal(request.total());
        }
    }

    private CoffeeResponse mapToCoffeeResponse(Coffee coffee) {
        String supplierName = null;
        if (coffee.getSupplier() != null) {
            supplierName = coffee.getSupplier().getSupName();
        } else {
            Optional<Supplier> supplierOptional = supplierServiceJPA.findById(coffee.getSupId());
            if (supplierOptional.isPresent()) {
                supplierName = supplierOptional.get().getSupName();
            }
        }
        return new CoffeeResponse(
                coffee.getCofName(),
                coffee.getSupId(),
                coffee.getPrice(),
                coffee.getSales(),
                coffee.getTotal(),
                supplierName
        );
    }
}