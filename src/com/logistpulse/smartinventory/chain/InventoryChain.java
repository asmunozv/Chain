package com.logistpulse.smartinventory.chain;

public class InventoryChain {
    private final InventoryHandler first;

    public InventoryChain() {
        InventoryHandler sku = new SkuHandler();
        InventoryHandler stock = new StockHandler();
        InventoryHandler lot = new LotHandler();
        InventoryHandler capacity = new CapacityHandler();

        sku.setNext(stock).setNext(lot).setNext(capacity);
        this.first = sku;
    }

    public ValidationResult validate(InventoryRequest request, InventoryService inventory) {
        return first.handle(request, inventory);
    }
}
