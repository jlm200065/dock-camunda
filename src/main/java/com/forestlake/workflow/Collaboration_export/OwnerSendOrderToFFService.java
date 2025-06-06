package com.forestlake.workflow.Collaboration_export;


import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("ownerSendOrderToFF")
public class OwnerSendOrderToFFService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String telephone = (String)execution.getVariable("telephone");

        System.out.println("给货代发送订单");
//        runtimeService.correlateMessage("Message_Owner_order_received");
        String businessKey = execution.getProcessBusinessKey();

        for (String url : endpointUrls) {
            try {
                SendTask.sendPostRequest(url, "Message_Owner_order_received", businessKey);
                System.out.println("已发送至: " + url);
            } catch (IOException e) {
                System.err.println("发送失败: " + url + " 错误: " + e.getMessage());
            }
        }
    }
}
