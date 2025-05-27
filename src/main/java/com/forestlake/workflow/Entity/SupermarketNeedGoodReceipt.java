package com.forestlake.workflow.Entity;

import java.util.Date;
import java.util.List;

public class SupermarketNeedGoodReceipt {

    private String receiptId; // 进货单编号
    private String supplierName; // 供应商名称
    private Date orderDate; // 订单日期
    private Date deliveryDate; // 预计送货日期
    private List<Item> items; // 采购商品列表
    private double totalCost; // 总金额
    private String createdBy; // 创建人
    private String status; // 订单状态 (如：pending, delivered, canceled)
    private String comments; // 备注或其他信息

    // 商品类
    public static class Item {
        private String productName; // 商品名称
        private int quantity; // 数量
        private double unitPrice; // 单价
        private String productCode; // 商品编号

        public Item(String productName, int quantity, double unitPrice, String productCode) {
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.productCode = productCode;
        }

        // Getter 和 Setter 方法
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
    }

    // Getter 和 Setter 方法
    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
