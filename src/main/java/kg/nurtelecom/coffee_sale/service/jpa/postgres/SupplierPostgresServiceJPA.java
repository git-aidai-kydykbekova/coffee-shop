package kg.nurtelecom.coffee_sale.service.jpa.postgres;

import kg.nurtelecom.coffee_sale.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierPostgresServiceJPA extends JpaRepository<Supplier, Integer> {
}