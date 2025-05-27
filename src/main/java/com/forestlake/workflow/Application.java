package com.forestlake.workflow;

import com.forestlake.workflow.Entity.LogisticsCompanyReceipt;
import com.forestlake.workflow.Entity.SupermarketNeedGoodReceipt;
import com.forestlake.workflow.Entity.SupplierGoodOutboundReceipt;
import com.forestlake.workflow.ScheduleService.MessageEvent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ExecutionQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.forestlake.workflow.ScheduleService.MessageEvent;
import com.forestlake.workflow.ScheduleService.MessageEventService;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@RestController
@EnableScheduling
public class Application {

  @Autowired
  private IdentityService identityService;
  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private MessageEventService messageEventService;

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }



  @GetMapping("/start/{processKey}")
  public void start(@PathVariable(value = "processKey") String processKey) {
    identityService.setAuthenticatedUserId("admin");
    runtimeService.startProcessInstanceByKey(processKey);

  }

  @GetMapping("/start/{processKey}/{businessKey}")
  public void startWithBusinessKey(@PathVariable(value = "processKey") String processKey, @PathVariable(value = "businessKey") String businessKey) {
    identityService.setAuthenticatedUserId("admin");
    runtimeService.startProcessInstanceByKey(processKey, businessKey);

  }


  @GetMapping("/find-all")
  public List<MessageEventService.MessageWithVariables> findAll() {
    identityService.setAuthenticatedUserId("admin");
    return messageEventService.findAll();
  }

  @PostMapping(value = "/sendMessage")
  public void sendMessage(@RequestBody JSONObject params){
    System.out.println("收到消息");
    String messageName = params.getString("messageName");
    System.out.println(messageName);
    String businessKey = params.getString("businessKey");
    System.out.println(businessKey);


    switch (messageName) {
      case "test":
        runtimeService.startProcessInstanceByKey("Process_test",  businessKey);
        break;

      case "Message_ask_for_CTN":

        runtimeService.startProcessInstanceByKey("Process_Depot",  businessKey);
        break;
      case "Message_SO_received":

        runtimeService.startProcessInstanceByKey("Process_SA", businessKey);
        break;
      case "Message_Owner_order_received":

        runtimeService.startProcessInstanceByKey("Process_FF", businessKey);
        break;
      case "Message_CrewList_received":

        runtimeService.startProcessInstanceByKey("Process_SBGS", businessKey);
        break;
      case "Message_CB_order_received":

        runtimeService.startProcessInstanceByKey("Process_CB", businessKey);
        break;
      default:
        messageEventService.sendMessage(messageName, businessKey, null);
        break;
    }

    System.out.println(messageEventService.findAll().toString());

  }


  //以下是CBPM的demo


  @Autowired
  private List<SupermarketNeedGoodReceipt> receiptList;

  @GetMapping("/receipts")
  public List<SupermarketNeedGoodReceipt> getReceipts() {
    return  receiptList;
  }
  // 处理超市进货请求
  @PostMapping(value = "/supermarketNeed")
  public void supermarketNeed(@RequestBody JSONObject params) {
    System.out.println("收到客户端发来的进货信息");
    String messageName = params.getString("messageName");
    String businessKey = params.getString("businessKey");
    String supplierName = params.getString("supplierName");
    double totalCost = params.getDouble("totalCost");
    String createdBy = params.getString("createdBy");
    // 手动解析商品列表
    JSONArray itemsArray = params.getJSONArray("items");
    List<SupermarketNeedGoodReceipt.Item> items = new ArrayList<>();
    for (int i = 0; i < itemsArray.size(); i++) {
      JSONObject itemObject = itemsArray.getJSONObject(i);
      String productName = itemObject.getString("productName");
      // 通过 Object 转换为 Integer 或 Double，再进行类型转换
      Object quantityObj = itemObject.get("quantity");
      int quantity = 0;
      if (quantityObj instanceof Integer) {
        quantity = (Integer) quantityObj;
      } else if (quantityObj instanceof Number) {
        quantity = ((Number) quantityObj).intValue();
      }
      // 处理 unitPrice
      Object unitPriceObj = itemObject.get("unitPrice");
      double unitPrice = 0.0;
      if (unitPriceObj instanceof Number) {
        unitPrice = ((Number) unitPriceObj).doubleValue();
      }
      String productCode = itemObject.getString("productCode");
      SupermarketNeedGoodReceipt.Item item = new SupermarketNeedGoodReceipt.Item(productName, quantity, unitPrice, productCode);
      items.add(item);
    }

    // 组装SupermarketNeedGoodReceipt对象
    SupermarketNeedGoodReceipt receipt = new SupermarketNeedGoodReceipt();
    receipt.setReceiptId(businessKey); // 使用businessKey作为进货单ID
    receipt.setSupplierName(supplierName);
    receipt.setOrderDate(new Date());
    receipt.setDeliveryDate(new Date()); // 示例中用当前时间，你可以用params中的具体日期
    receipt.setTotalCost(totalCost);
    receipt.setCreatedBy(createdBy);
    receipt.setItems(items);
    receipt.setStatus("处理中");
    // 将新创建的进货单添加到列表
    receiptList.add(receipt);
    System.out.println("新建进货单: " + receipt);
    // 启动流程
    if (messageName.equals("supermarket_need")) {
      runtimeService.startProcessInstanceByKey("Process_Supermarket", businessKey);
    }
  }

  // 处理超市收货请求
  @PostMapping(value = "/supermarketReceiving")
  public void supermarketReceiving(@RequestBody JSONObject params) {
    System.out.println("收到客户端发来的收货信息");

    String messageName = params.getString("messageName");
    String businessKey = params.getString("businessKey");

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    RuntimeService runtimeService = processEngine.getRuntimeService();

    // 根据businessKey查找进货单
    for (SupermarketNeedGoodReceipt receipt : receiptList) {
      if (receipt.getReceiptId().equals(businessKey)) {
        // 更新进货单状态为“已收货”
        receipt.setStatus("已收货");
        System.out.println("更新进货单状态为已收货: " + receipt);
        break;
      }
    }

    // 查询并处理Camunda的流程实例
    ExecutionQuery executionQuery = runtimeService.createExecutionQuery()
            .messageEventSubscriptionName(messageName)
            .processInstanceBusinessKey(businessKey);

    if (executionQuery.count() > 0) {
      Execution execution = executionQuery.list().get(0);
      runtimeService.messageEventReceived(messageName, execution.getId(), null);
    }

    System.out.println("超市进货流程结束");
  }




  @Autowired
  private List<SupplierGoodOutboundReceipt> supplierGoodOutboundReceiptList;
  // 获取所有供应商出库单
  @GetMapping("/supplierReceipts")
  public List<SupplierGoodOutboundReceipt> getSupplierReceipts() {
    return supplierGoodOutboundReceiptList;
  }

  // 处理供应商出库请求
  @PostMapping(value = "/supplierOutbound")
  public void supplierOutbound(@RequestBody JSONObject params) {
    System.out.println("收到客户端发来的供应商出库请求");

    String messageName = params.getString("messageName");
    String businessKey = params.getString("businessKey");
    String purchaseUnit = params.getString("purchaseUnit");
    double totalCost = params.getDouble("totalCost");
    String createdBy = params.getString("createdBy");

    // 手动解析商品列表
    JSONArray itemsArray = params.getJSONArray("items");
    List<SupplierGoodOutboundReceipt.Item> items = new ArrayList<>();
    for (int i = 0; i < itemsArray.size(); i++) {
      JSONObject itemObject = itemsArray.getJSONObject(i);
      String productName = itemObject.getString("productName");

      // 通过 Object 转换为 Integer 或 Double，再进行类型转换
      Object quantityObj = itemObject.get("quantity");
      int quantity = 0;
      if (quantityObj instanceof Integer) {
        quantity = (Integer) quantityObj;
      } else if (quantityObj instanceof Number) {
        quantity = ((Number) quantityObj).intValue();
      }

      // 处理 unitPrice
      Object unitPriceObj = itemObject.get("unitPrice");
      double unitPrice = 0.0;
      if (unitPriceObj instanceof Number) {
        unitPrice = ((Number) unitPriceObj).doubleValue();
      }

      String productCode = itemObject.getString("productCode");
      SupplierGoodOutboundReceipt.Item item = new SupplierGoodOutboundReceipt.Item(productName, quantity, unitPrice, productCode);
      items.add(item);
    }

    // 组装 SupplierGoodOutboundReceipt 对象
    SupplierGoodOutboundReceipt receipt = new SupplierGoodOutboundReceipt();
    receipt.setReceiptId(businessKey); // 使用businessKey作为出库单ID
    receipt.setPurchaseUnit(purchaseUnit);
    receipt.setOrderDate(new Date());
    receipt.setOutboundDate(new Date()); // 示例中用当前时间
    receipt.setTotalCost(totalCost);
    receipt.setCreatedBy(createdBy);
    receipt.setItems(items);
    receipt.setStatus("处理中");

    // 将新创建的出库单添加到列表
    supplierGoodOutboundReceiptList.add(receipt);
    System.out.println("新建供应商出库单: " + receipt);

    // 启动流程
    if (messageName.equals("supplier_outbound")) {
      runtimeService.startProcessInstanceByKey("Process_Supplier", businessKey);
    }
  }

  // 处理供应商确认请求
  @PostMapping(value = "/supplierConfirm")
  public void supplierConfirm(@RequestBody JSONObject params) {
    System.out.println("收到客户端发来的供应商确认请求");

    String messageName = params.getString("messageName");
    String businessKey = params.getString("businessKey");

    // 根据businessKey查找出库单
    for (SupplierGoodOutboundReceipt receipt : supplierGoodOutboundReceiptList) {
      if (receipt.getReceiptId().equals(businessKey)) {
        // 更新出库单状态为“已确认”
        receipt.setStatus("已确认");
        System.out.println("更新出库单状态为已确认: " + receipt);
        break;
      }
    }

    // 查询并处理Camunda的流程实例
    ExecutionQuery executionQuery = runtimeService.createExecutionQuery()
            .messageEventSubscriptionName(messageName)
            .processInstanceBusinessKey(businessKey);

    if (executionQuery.count() > 0) {
      Execution execution = executionQuery.list().get(0);
      runtimeService.messageEventReceived(messageName, execution.getId(), null);
    }

    System.out.println("供应商出库流程结束");
  }



  @Autowired
  private List<LogisticsCompanyReceipt> logisticsCompanyReceiptList;

  // 获取所有物流公司出库单
  @GetMapping("/logisticsReceipts")
  public List<LogisticsCompanyReceipt> getLogisticsReceipts() {
    return logisticsCompanyReceiptList;
  }

  // 处理物流公司发货请求
  @PostMapping(value = "/logisticsOutbound")
  public void logisticsOutbound(@RequestBody JSONObject params) {
    System.out.println("收到客户端发来的物流公司发货请求");

    String messageName = params.getString("messageName");
    String businessKey = params.getString("businessKey");
    String supplierName = params.getString("supplierName");
    String purchaseUnit = params.getString("purchaseUnit");
    double totalCost = params.getDouble("totalCost");
    String createdBy = params.getString("createdBy");

    // 解析供应商地址
    JSONObject supplierAddressObj = params.getJSONObject("supplierAddress");
    LogisticsCompanyReceipt.Address supplierAddress = new LogisticsCompanyReceipt.Address(
            supplierAddressObj.getString("street"),
            supplierAddressObj.getString("city"),
            supplierAddressObj.getString("state"),
            supplierAddressObj.getString("postalCode"),
            supplierAddressObj.getString("country")
    );

    // 解析购货单位地址
    JSONObject purchaseUnitAddressObj = params.getJSONObject("purchaseUnitAddress");
    LogisticsCompanyReceipt.Address purchaseUnitAddress = new LogisticsCompanyReceipt.Address(
            purchaseUnitAddressObj.getString("street"),
            purchaseUnitAddressObj.getString("city"),
            purchaseUnitAddressObj.getString("state"),
            purchaseUnitAddressObj.getString("postalCode"),
            purchaseUnitAddressObj.getString("country")
    );

    // 手动解析商品列表
    JSONArray itemsArray = params.getJSONArray("items");
    List<LogisticsCompanyReceipt.Item> items = new ArrayList<>();
    for (int i = 0; i < itemsArray.size(); i++) {
      JSONObject itemObject = itemsArray.getJSONObject(i);
      String productName = itemObject.getString("productName");

      // 通过 Object 转换为 Integer 或 Double，再进行类型转换
      Object quantityObj = itemObject.get("quantity");
      int quantity = 0;
      if (quantityObj instanceof Integer) {
        quantity = (Integer) quantityObj;
      } else if (quantityObj instanceof Number) {
        quantity = ((Number) quantityObj).intValue();
      }

      // 处理 unitPrice
      Object unitPriceObj = itemObject.get("unitPrice");
      double unitPrice = 0.0;
      if (unitPriceObj instanceof Number) {
        unitPrice = ((Number) unitPriceObj).doubleValue();
      }

      String productCode = itemObject.getString("productCode");
      LogisticsCompanyReceipt.Item item = new LogisticsCompanyReceipt.Item(productName, quantity, unitPrice, productCode);
      items.add(item);
    }

    // 组装 LogisticsCompanyReceipt 对象
    LogisticsCompanyReceipt receipt = new LogisticsCompanyReceipt();
    receipt.setReceiptId(businessKey); // 使用businessKey作为出库单ID
    receipt.setSupplierName(supplierName);
    receipt.setSupplierAddress(supplierAddress);
    receipt.setPurchaseUnit(purchaseUnit);
    receipt.setPurchaseUnitAddress(purchaseUnitAddress);
    receipt.setOrderDate(new Date());
    receipt.setOutboundDate(new Date()); // 示例中用当前时间
    receipt.setTotalCost(totalCost);
    receipt.setCreatedBy(createdBy);
    receipt.setItems(items);
    receipt.setStatus("处理中");

    // 将新创建的出库单添加到列表
    logisticsCompanyReceiptList.add(receipt);
    System.out.println("新建物流公司出库单: " + receipt);

    // 启动流程
    if (messageName.equals("logistics_outbound")) {
      runtimeService.startProcessInstanceByKey("Process_Logistics", businessKey);
    }
  }


  // 处理物流公司确认送达请求
  @PostMapping(value = "/logisticsConfirm")
  public void logisticsConfirm(@RequestBody JSONObject params) {
    System.out.println("收到客户端发来的物流公司确认送达请求");

    String messageName = params.getString("messageName");
    String businessKey = params.getString("businessKey");

    // 根据businessKey查找出库单
    for (LogisticsCompanyReceipt receipt : logisticsCompanyReceiptList) {
      if (receipt.getReceiptId().equals(businessKey)) {
        // 更新出库单状态为“已送达”
        receipt.setStatus("已送达");
        System.out.println("更新出库单状态为已送达: " + receipt);
        break;
      }
    }

    // 查询并处理Camunda的流程实例
    ExecutionQuery executionQuery = runtimeService.createExecutionQuery()
            .messageEventSubscriptionName(messageName)
            .processInstanceBusinessKey(businessKey);

    if (executionQuery.count() > 0) {
      Execution execution = executionQuery.list().get(0);
      runtimeService.messageEventReceived(messageName, execution.getId(), null);
    }

    System.out.println("物流公司发货流程结束");
  }










  @Autowired
  private RestTemplate restTemplate;

  // 超市、供应商、物流公司接口地址
  private static final String SUPPLIER_OUTBOUND_URL = "http://localhost:10006/supplierOutbound";
  private static final String LOGISTICS_OUTBOUND_URL = "http://localhost:10006/logisticsOutbound";
  @PostMapping(value = "/supermarketNeedAlliance")
  public void supermarketNeedAlliance(@RequestBody JSONObject params) {
    System.out.println("收到联盟进货请求");

    // 超市的处理
    String messageName = params.getString("messageName");
    String businessKey = params.getString("businessKey");
    String supplierName = params.getString("supplierName");
    double totalCost = params.getDouble("totalCost");
    String createdBy = params.getString("createdBy");

    // 解析商品列表
    JSONArray itemsArray = params.getJSONArray("items");
    List<SupermarketNeedGoodReceipt.Item> items = new ArrayList<>();
    for (int i = 0; i < itemsArray.size(); i++) {
      JSONObject itemObject = itemsArray.getJSONObject(i);
      String productName = itemObject.getString("productName");
      // 通过 Object 转换为 Integer 或 Double，再进行类型转换
      Object quantityObj = itemObject.get("quantity");
      int quantity = 0;
      if (quantityObj instanceof Integer) {
        quantity = (Integer) quantityObj;
      } else if (quantityObj instanceof Number) {
        quantity = ((Number) quantityObj).intValue();
      }
      double unitPrice = itemObject.getDouble("unitPrice");
      String productCode = itemObject.getString("productCode");
      SupermarketNeedGoodReceipt.Item item = new SupermarketNeedGoodReceipt.Item(productName, quantity, unitPrice, productCode);
      items.add(item);
    }

    // 创建超市的进货单
    SupermarketNeedGoodReceipt receipt = new SupermarketNeedGoodReceipt();
    receipt.setReceiptId(businessKey); // 使用businessKey作为进货单ID
    receipt.setSupplierName(supplierName);
    receipt.setOrderDate(new Date());
    receipt.setDeliveryDate(new Date());
    receipt.setTotalCost(totalCost);
    receipt.setCreatedBy(createdBy);
    receipt.setItems(items);
    receipt.setStatus("处理中");

    receiptList.add(receipt);
    System.out.println("新建超市进货单: " + receipt);

    // 启动超市流程
    if (messageName.equals("supermarket_need")) {
      runtimeService.startProcessInstanceByKey("Process_Supermarket", businessKey);
    }

    // 供应商出库请求
    JSONObject supplierParams = new JSONObject();
    supplierParams.put("messageName", "supplier_outbound");
    supplierParams.put("businessKey", businessKey);
    supplierParams.put("purchaseUnit", "Supermarket A"); // 模拟购货单位
    supplierParams.put("totalCost", totalCost);
    supplierParams.put("createdBy", createdBy);
    supplierParams.put("items", itemsArray);

    try {
      restTemplate.postForObject(SUPPLIER_OUTBOUND_URL, supplierParams, String.class);
      System.out.println("已发送供应商出库请求");
    } catch (Exception e) {
      System.err.println("发送供应商出库请求失败: " + e.getMessage());
    }

    // 物流发货请求
    JSONObject logisticsParams = new JSONObject();
    logisticsParams.put("messageName", "logistics_outbound");
    logisticsParams.put("businessKey", businessKey);
    logisticsParams.put("supplierName", supplierName);
    logisticsParams.put("purchaseUnit", "Supermarket A");
    logisticsParams.put("totalCost", totalCost);
    logisticsParams.put("createdBy", createdBy);

    // 供应商地址（模拟）
    JSONObject supplierAddress = new JSONObject();
    supplierAddress.put("street", "Supplier Street");
    supplierAddress.put("city", "Supplier City");
    supplierAddress.put("state", "Supplier State");
    supplierAddress.put("postalCode", "123456");
    supplierAddress.put("country", "Supplier Country");
    logisticsParams.put("supplierAddress", supplierAddress);

    // 购货单位地址（模拟）
    JSONObject purchaseUnitAddress = new JSONObject();
    purchaseUnitAddress.put("street", "Supermarket Street");
    purchaseUnitAddress.put("city", "Supermarket City");
    purchaseUnitAddress.put("state", "Supermarket State");
    purchaseUnitAddress.put("postalCode", "654321");
    purchaseUnitAddress.put("country", "Supermarket Country");
    logisticsParams.put("purchaseUnitAddress", purchaseUnitAddress);

    logisticsParams.put("items", itemsArray);

    try {
      restTemplate.postForObject(LOGISTICS_OUTBOUND_URL, logisticsParams, String.class);
      System.out.println("已发送物流发货请求");
    } catch (Exception e) {
      System.err.println("发送物流发货请求失败: " + e.getMessage());
    }
  }

}
