package com.forestlake.workflow.Collaboration_export;

import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("CTNandShipAllArrival")
public class CustomReceiveCTNandShipAllArrivalService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("通知海关出口货物CTN与运输船已到达");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
//        runtimeService.correlateMessage("Message_CTN_and_ship_arrive");
        String messageName = "Message_CTN_and_ship_arrive";  // 或从 execution.getVariable 中获取
        String businessKey = execution.getProcessBusinessKey();

        for (String url : endpointUrls) {
            try {
                SendTask.sendPostRequest(url, messageName, businessKey);
                System.out.println("已发送至: " + url);
            } catch (IOException e) {
                System.err.println("发送失败: " + url + " 错误: " + e.getMessage());
            }
        }

    }
}