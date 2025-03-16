package kg.nurtelecom.coffee_sale.service.jpa.postgres;


import kg.nurtelecom.coffee_sale.entity.CoffeeHouse;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoffeeHousePostgresServiceJPA extends JpaRepository<CoffeeHouse, Integer> {

}