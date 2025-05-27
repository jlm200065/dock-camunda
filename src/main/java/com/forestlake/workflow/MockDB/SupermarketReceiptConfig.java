package com.forestlake.workflow.MockDB;

import com.forestlake.workflow.Entity.SupermarketNeedGoodReceipt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class SupermarketReceiptConfig {

    @Bean
    public List<SupermarketNeedGoodReceipt> supermarketNeedGoodReceiptList() {
        List<SupermarketNeedGoodReceipt> receiptList = new ArrayList<>();

        // 创建几个进货单对象
        SupermarketNeedGoodReceipt receipt1 = new SupermarketNeedGoodReceipt();
        receipt1.setReceiptId("R001");
        receipt1.setSupplierName("Supplier A");
        receipt1.setOrderDate(new Date());
        receipt1.setDeliveryDate(new Date());
        receipt1.setTotalCost(1500.0);
        receipt1.setCreatedBy("Manager John");
        receipt1.setStatus("处理中");

        // 添加商品到该进货单
        List<SupermarketNeedGoodReceipt.Item> items1 = new ArrayList<>();
        items1.add(new SupermarketNeedGoodReceipt.Item("Apple", 100, 1.5, "A001"));
        items1.add(new SupermarketNeedGoodReceipt.Item("Banana", 200, 0.8, "B002"));
        receipt1.setItems(items1);

        // 添加进货单到列表
        receiptList.add(receipt1);

        // 再添加一个进货单对象
        SupermarketNeedGoodReceipt receipt2 = new SupermarketNeedGoodReceipt();
        receipt2.setReceiptId("R002");
        receipt2.setSupplierName("Supplier B");
        receipt2.setOrderDate(new Date());
        receipt2.setDeliveryDate(new Date());
        receipt2.setTotalCost(2500.0);
        receipt2.setCreatedBy("Manager Mike");
        receipt2.setStatus("已完成");

        // 添加商品到该进货单
        List<SupermarketNeedGoodReceipt.Item> items2 = new ArrayList<>();
        items2.add(new SupermarketNeedGoodReceipt.Item("Orange", 150, 1.2, "O003"));
        items2.add(new SupermarketNeedGoodReceipt.Item("Mango", 100, 2.5, "M004"));
        receipt2.setItems(items2);

        // 添加进货单到列表
        receiptList.add(receipt2);

        return receiptList;
    }
}
