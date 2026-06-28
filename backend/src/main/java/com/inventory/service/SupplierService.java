package com.inventory.service;

import com.inventory.entity.Supplier;
import com.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier createSupplier(Supplier supplier) {
        if (supplierRepository.existsByName(supplier.getName())) {
            throw new RuntimeException("Supplier with name '" + supplier.getName() + "' already exists");
        }
        return supplierRepository.save(supplier);
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() { return supplierRepository.findAll(); }

    @Transactional(readOnly = true)
    public List<Supplier> getActiveSuppliers() { return supplierRepository.findByActiveTrue(); }

    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) { return supplierRepository.findById(id); }

    @Transactional(readOnly = true)
    public List<Supplier> searchSuppliers(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    public Supplier updateSupplier(Long id, Supplier updated) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        if (updated.getName() != null) supplier.setName(updated.getName());
        if (updated.getContactPerson() != null) supplier.setContactPerson(updated.getContactPerson());
        if (updated.getEmail() != null) supplier.setEmail(updated.getEmail());
        if (updated.getPhone() != null) supplier.setPhone(updated.getPhone());
        if (updated.getAddress() != null) supplier.setAddress(updated.getAddress());
        if (updated.getActive() != null) supplier.setActive(updated.getActive());
        return supplierRepository.save(supplier);
    }

    public Supplier deactivateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        supplier.setActive(false);
        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }
}
