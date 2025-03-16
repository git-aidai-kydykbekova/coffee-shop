package kg.nurtelecom.coffee_sale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @Column(name = "sup_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supId;

    @Column(name = "sup_name", nullable = false)
    @NotBlank(message = "Supplier name is mandatory")
    private String supName;

    @Column(name = "street", nullable = false)
    @NotBlank(message = "Street is mandatory")
    private String street;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "City is mandatory")
    private String city;

    @Column(name = "state", nullable = false)
    @NotBlank(message = "State is mandatory")
    private String state;

    @Column(name = "zip", nullable = false)
    @NotBlank(message = "ZIP is mandatory")
    private String zip;

    @OneToMany(mappedBy = "supplier")
    private List<Coffee> coffees;

    public Supplier() {
    }

    public Supplier(String supName, String street, String city, String state, String zip) {
        this.supName = supName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Integer getSupId() {
        return supId;
    }

    public void setSupId(Integer supId) {
        this.supId = supId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<Coffee> getCoffees() {
        return coffees;
    }

    public void setCoffees(List<Coffee> coffees) {
        this.coffees = coffees;
    }
}