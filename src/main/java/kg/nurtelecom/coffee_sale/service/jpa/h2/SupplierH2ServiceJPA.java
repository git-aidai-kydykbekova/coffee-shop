package kg.nurtelecom.coffee_sale.service.jpa.h2;

import kg.nurtelecom.coffee_sale.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierH2ServiceJPA extends JpaRepository<Supplier, Integer> {
}