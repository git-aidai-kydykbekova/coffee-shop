package kg.nurtelecom.coffee_sale.service.jpa.postgres;


import kg.nurtelecom.coffee_sale.entity.MerchandiseInventory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface MerchandiseInventoryPostgresServiceJPA extends JpaRepository<MerchandiseInventory, String> {
    List<MerchandiseInventory> findBySupId(Integer supId);
}