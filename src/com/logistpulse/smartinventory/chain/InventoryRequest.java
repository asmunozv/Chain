package com.logistpulse.smartinventory.chain;

public class InventoryRequest {
    private final String sku;
    private final String warehouse;
    private final String lotId;
    private final int quantity;
    private final String orderId;

    public InventoryRequest(String sku, String warehouse, String lotId, int quantity, String orderId) {
        this.sku = sku;
        this.warehouse = warehouse;
        this.lotId = lotId;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public String getSku() { return sku; }
    public String getWarehouse() { return warehouse; }
    public String getLotId() { return lotId; }
    public int getQuantity() { return quantity; }
    public String getOrderId() { return orderId; }
}
