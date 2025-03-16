package kg.nurtelecom.coffee_sale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "cof_inventory")
public class CoffeeInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_id", nullable = false)
    @NotNull(message = "Warehouse ID is mandatory")
    private Integer warehouseId;

    @Column(name = "cof_name", nullable = false)
    @NotBlank(message = "Coffee name is mandatory")
    private String cofName;

    @Column(name = "sup_id", nullable = false)
    @NotNull(message = "Supplier ID is mandatory")
    private Integer supId;

    @Column(name = "quan", nullable = false)
    private Integer quantity = 0;

    @Column(name = "date_val", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "cof_name", referencedColumnName = "cof_name", insertable = false, updatable = false)
    private Coffee coffee;

    @ManyToOne
    @JoinColumn(name = "sup_id", referencedColumnName = "sup_id", insertable = false, updatable = false)
    private Supplier supplier;

    public CoffeeInventory() {
    }

    public CoffeeInventory(Integer warehouseId, String cofName, Integer supId, Integer quantity, LocalDate date) {
        this.warehouseId = warehouseId;
        this.cofName = cofName;
        this.supId = supId;
        this.quantity = quantity;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getCofName() {
        return cofName;
    }

    public void setCofName(String cofName) {
        this.cofName = cofName;
    }

    public Integer getSupId() {
        return supId;
    }

    public void setSupId(Integer supId) {
        this.supId = supId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Coffee getCoffee() {
        return coffee;
    }

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}