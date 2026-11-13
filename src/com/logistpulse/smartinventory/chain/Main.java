package com.logistpulse.smartinventory.chain;

import java.time.LocalDate;

public class Main {
    private static void tryReserve(InventoryService inventory, InventoryRequest request) {
        ValidationResult result = inventory.reserve(request);
        System.out.println(request.getOrderId() + " -> " + (result.isApproved() ? "APROBADA" : "RECHAZADA")
            + " | " + result.getMessage());
    }

    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addProduct("SKU-001");
        inventory.addLot("SKU-001", "Quito", "L-OK", 30, LocalDate.now().plusMonths(6), false);
        inventory.addLot("SKU-001", "Quito", "L-VENCIDO", 10, LocalDate.now().minusDays(1), false);
        inventory.addLot("SKU-001", "Quito", "L-CUARENTENA", 10, LocalDate.now().plusMonths(6), true);
        inventory.setReservationCapacity("Quito", 12);

        tryReserve(inventory, new InventoryRequest("SKU-001", "Quito", "L-OK", 5, "PED-001"));
        tryReserve(inventory, new InventoryRequest("SKU-NO", "Quito", "L-OK", 1, "PED-002"));
        tryReserve(inventory, new InventoryRequest("SKU-001", "Quito", "L-OK", 99, "PED-003"));
        tryReserve(inventory, new InventoryRequest("SKU-001", "Quito", "L-VENCIDO", 1, "PED-004"));
        tryReserve(inventory, new InventoryRequest("SKU-001", "Quito", "L-CUARENTENA", 1, "PED-005"));
        tryReserve(inventory, new InventoryRequest("SKU-001", "Quito", "L-OK", 8, "PED-006"));
        tryReserve(inventory, new InventoryRequest("SKU-001", "Quito", "L-OK", 3, "PED-007"));
        tryReserve(inventory, new InventoryRequest("SKU-001", "Quito", "L-OK", 3, "PED-007"));
    }
}
