package com.forestlake.workflow.MockDB;

import com.forestlake.workflow.Entity.LogisticsCompanyReceipt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class LogisticsReceiptConfig {

    @Bean
    public List<LogisticsCompanyReceipt> logisticsCompanyReceiptList() {
        List<LogisticsCompanyReceipt> receiptList = new ArrayList<>();

        // 创建第一个出库单对象
        LogisticsCompanyReceipt receipt1 = new LogisticsCompanyReceipt();
        receipt1.setReceiptId("L001");
        receipt1.setSupplierName("Supplier A");
        receipt1.setSupplierAddress(new LogisticsCompanyReceipt.Address("123 Main St", "New York", "NY", "10001", "USA"));
        receipt1.setPurchaseUnit("Supermarket A");
        receipt1.setPurchaseUnitAddress(new LogisticsCompanyReceipt.Address("456 Market St", "San Francisco", "CA", "94103", "USA"));
        receipt1.setOrderDate(new Date());
        receipt1.setOutboundDate(new Date());
        receipt1.setTotalCost(1500.0);
        receipt1.setCreatedBy("Logistics Manager John");
        receipt1.setStatus("处理中");

        // 添加商品到该出库单
        List<LogisticsCompanyReceipt.Item> items1 = new ArrayList<>();
        items1.add(new LogisticsCompanyReceipt.Item("Apple", 100, 1.5, "A001"));
        items1.add(new LogisticsCompanyReceipt.Item("Banana", 200, 0.8, "B002"));
        receipt1.setItems(items1);

        // 添加出库单到列表
        receiptList.add(receipt1);

        // 再添加一个出库单对象
        LogisticsCompanyReceipt receipt2 = new LogisticsCompanyReceipt();
        receipt2.setReceiptId("L002");
        receipt2.setSupplierName("Supplier B");
        receipt2.setSupplierAddress(new LogisticsCompanyReceipt.Address("789 Supplier Rd", "Los Angeles", "CA", "90001", "USA"));
        receipt2.setPurchaseUnit("Supermarket B");
        receipt2.setPurchaseUnitAddress(new LogisticsCompanyReceipt.Address("123 Buyer Ave", "Chicago", "IL", "60601", "USA"));
        receipt2.setOrderDate(new Date());
        receipt2.setOutboundDate(new Date());
        receipt2.setTotalCost(2500.0);
        receipt2.setCreatedBy("Logistics Manager Mike");
        receipt2.setStatus("已发货");

        // 添加商品到该出库单
        List<LogisticsCompanyReceipt.Item> items2 = new ArrayList<>();
        items2.add(new LogisticsCompanyReceipt.Item("Orange", 150, 1.2, "O003"));
        items2.add(new LogisticsCompanyReceipt.Item("Mango", 100, 2.5, "M004"));
        receipt2.setItems(items2);

        // 添加出库单到列表
        receiptList.add(receipt2);

        return receiptList;
    }
}
