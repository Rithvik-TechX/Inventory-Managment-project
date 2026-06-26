package com.inventory.dao;

import com.inventory.model.Supplier;
import java.util.List;

public interface SupplierDAO {
    boolean addSupplier(Supplier supplier);
    Supplier getSupplierById(int id);
    List<Supplier> getAllSuppliers();
    boolean updateSupplier(Supplier supplier);
    boolean deleteSupplier(int id);
}