package kg.nurtelecom.coffee_sale.controller.spring_mvc;

import jakarta.validation.Valid;

import kg.nurtelecom.coffee_sale.payload.request.SupplierRequest;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @GetMapping
    public String listSuppliers(Model model) {
        List<SupplierResponse> suppliers = supplierService.findAll();
        model.addAttribute("suppliers", suppliers);
        return "suppliers/list";
    }

    @GetMapping("/add")
    public String showCreateForm(Model model) {
        model.addAttribute("supplier", new SupplierRequest(null, "", "", "", "", ""));
        return "suppliers/add";
    }

    @PostMapping("/add")
    public String createSupplier(@Valid @ModelAttribute("supplier") SupplierRequest supplierRequest,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "suppliers/add";
        }
        try {
            supplierService.create(supplierRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier created successfully");
            return "redirect:/suppliers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating supplier: " + e.getMessage());
            return "suppliers/add";
        }
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        try {
            SupplierResponse supplier = supplierService.findById(id);
            SupplierRequest supplierRequest = new SupplierRequest(
                    supplier.supId(),
                    supplier.supName(),
                    supplier.street(),
                    supplier.city(),
                    supplier.state(),
                    supplier.zip()
            );
            model.addAttribute("supplier", supplierRequest);
            return "suppliers/edit";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found", e);
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSupplier(@PathVariable Integer id,
                                 @Valid @ModelAttribute("supplier") SupplierRequest supplierRequest,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "suppliers/edit";
        }

        try {
            supplierService.update(id, supplierRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier updated successfully");
            return "redirect:/suppliers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating supplier: " + e.getMessage());
            return "suppliers/edit";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting supplier: " + e.getMessage());
        }
        return "redirect:/suppliers";
    }


}