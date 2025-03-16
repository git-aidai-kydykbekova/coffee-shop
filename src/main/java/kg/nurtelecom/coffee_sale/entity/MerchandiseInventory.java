package kg.nurtelecom.coffee_sale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "merch_inventory")
public class MerchandiseInventory {

    @Id
    @Column(name = "item_id", nullable = false)
    @NotBlank(message = "Item ID is mandatory")
    private String itemId;

    @Column(name = "item_name", nullable = false)
    @NotBlank(message = "Item name is mandatory")
    private String itemName;

    @Column(name = "sup_id", nullable = false)
    @NotNull(message = "Supplier ID is mandatory")
    private Integer supId;

    @Column(name = "quan", nullable = false)
    private Integer quantity = 0;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "sup_id", referencedColumnName = "sup_id", insertable = false, updatable = false)
    private Supplier supplier;

    public MerchandiseInventory() {
    }

    public MerchandiseInventory(String itemId, String itemName, Integer supId, Integer quantity, LocalDate date) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.supId = supId;
        this.quantity = quantity;
        this.date = date;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}