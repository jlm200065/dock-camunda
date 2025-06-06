package com.forestlake.workflow.MockDB;

import com.forestlake.workflow.Entity.SupplierGoodOutboundReceipt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class SupplierReceiptConfig {

    @Bean
    public List<SupplierGoodOutboundReceipt> supplierGoodOutboundReceiptList() {
        List<SupplierGoodOutboundReceipt> receiptList = new ArrayList<>();

        // 创建几个出库单对象
        SupplierGoodOutboundReceipt receipt1 = new SupplierGoodOutboundReceipt();
        receipt1.setReceiptId("S001");
        receipt1.setPurchaseUnit("Supermarket A"); // 购货单位
        receipt1.setOrderDate(new Date());
        receipt1.setOutboundDate(new Date());
        receipt1.setTotalCost(1500.0);
        receipt1.setCreatedBy("Manager Sarah");
        receipt1.setStatus("处理中");

        // 添加商品到该出库单
        List<SupplierGoodOutboundReceipt.Item> items1 = new ArrayList<>();
        items1.add(new SupplierGoodOutboundReceipt.Item("Apple", 100, 1.5, "A001"));
        items1.add(new SupplierGoodOutboundReceipt.Item("Banana", 200, 0.8, "B002"));
        receipt1.setItems(items1);

        // 添加出库单到列表
        receiptList.add(receipt1);

        // 再添加一个出库单对象
        SupplierGoodOutboundReceipt receipt2 = new SupplierGoodOutboundReceipt();
        receipt2.setReceiptId("S002");
        receipt2.setPurchaseUnit("Supermarket B"); // 购货单位
        receipt2.setOrderDate(new Date());
        receipt2.setOutboundDate(new Date());
        receipt2.setTotalCost(2500.0);
        receipt2.setCreatedBy("Manager David");
        receipt2.setStatus("已发货");

        // 添加商品到该出库单
        List<SupplierGoodOutboundReceipt.Item> items2 = new ArrayList<>();
        items2.add(new SupplierGoodOutboundReceipt.Item("Orange", 150, 1.2, "O003"));
        items2.add(new SupplierGoodOutboundReceipt.Item("Mango", 100, 2.5, "M004"));
        receipt2.setItems(items2);

        // 添加出库单到列表
        receiptList.add(receipt2);

        return receiptList;
    }
}
