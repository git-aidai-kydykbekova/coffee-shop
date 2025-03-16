package kg.nurtelecom.coffee_sale.controller.spring_mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeInventoryResponse;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeResponse;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeInventoryService;
import kg.nurtelecom.coffee_sale.service.CoffeeService;
import kg.nurtelecom.coffee_sale.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/inventory")
public class CoffeeInventoryController {

    private final CoffeeInventoryService coffeeInventoryService;
    private final CoffeeService coffeeService;
    private final SupplierService supplierService;

    public CoffeeInventoryController(CoffeeInventoryService coffeeInventoryService,
                                     CoffeeService coffeeService,
                                     SupplierService supplierService) {
        this.coffeeInventoryService = coffeeInventoryService;
        this.coffeeService = coffeeService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listInventory(Model model) {
        List<CoffeeInventoryResponse> inventoryList = coffeeInventoryService.findAll();
        model.addAttribute("inventoryList", inventoryList);
        return "inventory/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<CoffeeResponse> coffees = coffeeService.findAll();
        List<SupplierResponse> suppliers = supplierService.findAll();

        model.addAttribute("coffees", coffees);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("inventoryRequest", new CoffeeInventoryRequest(
                null, "", null, 0, LocalDate.now()));

        return "inventory/create";
    }

    @PostMapping
    public String createInventory(@Valid @ModelAttribute("inventoryRequest") CoffeeInventoryRequest request,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<CoffeeResponse> coffees = coffeeService.findAll();
            List<SupplierResponse> suppliers = supplierService.findAll();

            model.addAttribute("coffees", coffees);
            model.addAttribute("suppliers", suppliers);

            return "inventory/create";
        }

        try {
            coffeeInventoryService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory record added successfully");
            return "redirect:/inventory";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/inventory/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            CoffeeInventoryResponse inventory = coffeeInventoryService.findById(id);

            CoffeeInventoryRequest request = new CoffeeInventoryRequest(
                    inventory.warehouseId(),
                    inventory.cofName(),
                    inventory.supId(),
                    inventory.quantity(),
                    inventory.date()
            );

            List<CoffeeResponse> coffees = coffeeService.findAll();
            List<SupplierResponse> suppliers = supplierService.findAll();

            model.addAttribute("inventoryId", id);
            model.addAttribute("coffees", coffees);
            model.addAttribute("suppliers", suppliers);
            model.addAttribute("inventoryRequest", request);

            return "inventory/edit";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found", e);
        }
    }

    @PostMapping("/{id}")
    public String updateInventory(@PathVariable Long id,
                                  @Valid @ModelAttribute("inventoryRequest") CoffeeInventoryRequest request,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<CoffeeResponse> coffees = coffeeService.findAll();
            List<SupplierResponse> suppliers = supplierService.findAll();

            model.addAttribute("inventoryId", id);
            model.addAttribute("coffees", coffees);
            model.addAttribute("suppliers", suppliers);

            return "inventory/edit";
        }

        try {
            coffeeInventoryService.update(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory updated successfully");
            return "redirect:/inventory";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/inventory/" + id + "/edit";
        }
    }


    @PostMapping("/{id}/delete")
    public String deleteInventory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            coffeeInventoryService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory record deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/inventory";
    }
}