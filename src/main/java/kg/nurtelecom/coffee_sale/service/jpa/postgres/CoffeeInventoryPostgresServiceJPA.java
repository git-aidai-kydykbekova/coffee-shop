package kg.nurtelecom.coffee_sale.service.jpa.postgres;


import kg.nurtelecom.coffee_sale.entity.CoffeeInventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoffeeInventoryPostgresServiceJPA extends JpaRepository<CoffeeInventory, Long> {

}