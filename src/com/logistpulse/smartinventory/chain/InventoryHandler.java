package com.logistpulse.smartinventory.chain;

public abstract class InventoryHandler {
    private InventoryHandler next;

    public InventoryHandler setNext(InventoryHandler next) {
        this.next = next;
        return next;
    }

    public final ValidationResult handle(InventoryRequest request, InventoryService inventory) {
        ValidationResult current = validate(request, inventory);
        if (!current.isApproved()) {
            return current; // Detiene la cadena en el primer rechazo
        }
        return next != null ? next.handle(request, inventory) : ValidationResult.approved();
    }

    protected abstract ValidationResult validate(InventoryRequest request, InventoryService inventory);
}
