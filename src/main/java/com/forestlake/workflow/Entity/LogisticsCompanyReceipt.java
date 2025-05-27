package com.forestlake.workflow.Entity;

import java.util.Date;
import java.util.List;

public class LogisticsCompanyReceipt {

    private String receiptId; // 出库单编号
    private String supplierName; // 供应商名称
    private Address supplierAddress; // 供应商地址
    private String purchaseUnit; // 购货单位名称
    private Address purchaseUnitAddress; // 购货单位地址
    private Date orderDate; // 订单日期
    private Date outboundDate; // 出库日期
    private List<Item> items; // 商品列表
    private double totalCost; // 总金额
    private String createdBy; // 创建人
    private String status; // 订单状态 (如：pending, delivered, canceled)
    private String comments; // 备注或其他信息

    // 地址类
    public static class Address {
        private String street; // 街道
        private String city; // 城市
        private String state; // 省份/州
        private String postalCode; // 邮政编码
        private String country; // 国家

        public Address(String street, String city, String state, String postalCode, String country) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.postalCode = postalCode;
            this.country = country;
        }

        // Getter 和 Setter 方法
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

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

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

    public Address getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(Address supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public Address getPurchaseUnitAddress() {
        return purchaseUnitAddress;
    }

    public void setPurchaseUnitAddress(Address purchaseUnitAddress) {
        this.purchaseUnitAddress = purchaseUnitAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOutboundDate() {
        return outboundDate;
    }

    public void setOutboundDate(Date outboundDate) {
        this.outboundDate = outboundDate;
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
