package kg.nurtelecom.coffee_sale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "coffees")
public class Coffee {

    @Id
    @Column(name = "cof_name", length = 32, nullable = false)
    @NotBlank(message = "Coffee name is mandatory")
    private String cofName;

    @Column(name = "sup_id", nullable = false)
    @NotNull(message = "Supplier ID is mandatory")
    private Integer supId;

    @Column(name = "price", nullable = false)
    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be positive")
    private Float price;

    @Column(name = "sales", nullable = false)
    private Integer sales = 0;

    @Column(name = "total", nullable = false)
    private Integer total = 0;

    @ManyToOne
    @JoinColumn(name = "sup_id", insertable = false, updatable = false)
    private Supplier supplier;

    public Coffee() {
    }

    public Coffee(String cofName, Integer supId, Float price, Integer sales, Integer total) {
        this.cofName = cofName;
        this.supId = supId;
        this.price = price;
        this.sales = sales;
        this.total = total;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

}