package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.CoffeeHouse;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeHouseRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeHouseResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeHouseService;
import kg.nurtelecom.coffee_sale.service.jpa.h2.CoffeeHouseH2ServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeeHousePostgresServiceJPA;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CoffeeHouseRepository implements CoffeeHouseService {

    private final CoffeeHousePostgresServiceJPA coffeeHouseServiceJPA;
    private final CoffeeHouseH2ServiceJPA coffeeHouseH2ServiceJPA;

    public CoffeeHouseRepository(CoffeeHousePostgresServiceJPA coffeeHouseServiceJPA, CoffeeHouseH2ServiceJPA coffeeHouseH2ServiceJPA) {
        this.coffeeHouseServiceJPA = coffeeHouseServiceJPA;
        this.coffeeHouseH2ServiceJPA = coffeeHouseH2ServiceJPA;
    }

    @Override
    public List<CoffeeHouseResponse> findAll() {
        List<CoffeeHouse> coffeeHouses = coffeeHouseServiceJPA.findAll();
        List<CoffeeHouseResponse> responses = new ArrayList<>();

        for (CoffeeHouse coffeeHouse : coffeeHouses) {
            responses.add(mapToCoffeeHouseResponse(coffeeHouse));
        }

        return responses;
    }

    @Override
    public CoffeeHouseResponse findById(Integer id) {
        Optional<CoffeeHouse> coffeeHouseOptional = coffeeHouseServiceJPA.findById(id);
        if (coffeeHouseOptional.isPresent()) {
            return mapToCoffeeHouseResponse(coffeeHouseOptional.get());
        }
        throw new RuntimeException("Coffee house not found with id: " + id);
    }

    @Override
    public CoffeeHouseResponse create(CoffeeHouseRequest request) {
        if (coffeeHouseServiceJPA.existsById(request.storeId())) {
            throw new RuntimeException("Coffee house with ID " + request.storeId() + " already exists");
        }

        CoffeeHouse coffeeHouse = new CoffeeHouse();
        updateCoffeeHouseFromRequest(coffeeHouse, request);

        if (coffeeHouse.getTotal() == null || coffeeHouse.getTotal() == 0) {
            int coffee = coffeeHouse.getCoffee() != null ? coffeeHouse.getCoffee() : 0;
            int merch = coffeeHouse.getMerch() != null ? coffeeHouse.getMerch() : 0;
            coffeeHouse.setTotal(coffee + merch);
        }

        CoffeeHouse savedCoffeeHouse = coffeeHouseServiceJPA.save(coffeeHouse);
        coffeeHouseH2ServiceJPA.save(coffeeHouse);

        return mapToCoffeeHouseResponse(savedCoffeeHouse);
    }

    @Override
    public CoffeeHouseResponse update(Integer id, CoffeeHouseRequest request) {
        Optional<CoffeeHouse> coffeeHouseOptional = coffeeHouseServiceJPA.findById(id);
        if (coffeeHouseOptional.isPresent()) {
            CoffeeHouse coffeeHouse = coffeeHouseOptional.get();
            updateCoffeeHouseFromRequest(coffeeHouse, request);

            if (request.total() == null) {
                int coffee = coffeeHouse.getCoffee() != null ? coffeeHouse.getCoffee() : 0;
                int merch = coffeeHouse.getMerch() != null ? coffeeHouse.getMerch() : 0;
                coffeeHouse.setTotal(coffee + merch);
            }

            CoffeeHouse updatedCoffeeHouse = coffeeHouseServiceJPA.save(coffeeHouse);
            coffeeHouseH2ServiceJPA.save(coffeeHouse);
            return mapToCoffeeHouseResponse(updatedCoffeeHouse);
        }
        throw new RuntimeException("Coffee house not found with id: " + id);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (coffeeHouseServiceJPA.existsById(id)) {
            coffeeHouseServiceJPA.deleteById(id);
            coffeeHouseH2ServiceJPA.deleteById(id);
        } else {
            throw new RuntimeException("Coffee house not found with id: " + id);
        }
    }

    private void updateCoffeeHouseFromRequest(CoffeeHouse coffeeHouse, CoffeeHouseRequest request) {
        coffeeHouse.setStoreId(request.storeId());
        coffeeHouse.setCity(request.city());

        if (request.coffee() != null) {
            coffeeHouse.setCoffee(request.coffee());
        }

        if (request.merch() != null) {
            coffeeHouse.setMerch(request.merch());
        }

        if (request.total() != null) {
            coffeeHouse.setTotal(request.total());
        }
    }

    private CoffeeHouseResponse mapToCoffeeHouseResponse(CoffeeHouse coffeeHouse) {
        return new CoffeeHouseResponse(
                coffeeHouse.getStoreId(),
                coffeeHouse.getCity(),
                coffeeHouse.getCoffee(),
                coffeeHouse.getMerch(),
                coffeeHouse.getTotal()
        );
    }
}