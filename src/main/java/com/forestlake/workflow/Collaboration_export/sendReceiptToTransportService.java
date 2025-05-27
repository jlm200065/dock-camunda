package com.forestlake.workflow.Collaboration_export;

import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("sendReceiptToTransport")
public class sendReceiptToTransportService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("货代交给车队设备交接单");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
//        runtimeService.correlateMessage("Message_FF_Equipment_Receipt_received");
        String businessKey = execution.getProcessBusinessKey();

        for (String url : endpointUrls) {
            try {
                SendTask.sendPostRequest(url, "Message_FF_Equipment_Receipt_received", businessKey);
                System.out.println("已发送至: " + url);
            } catch (IOException e) {
                System.err.println("发送失败: " + url + " 错误: " + e.getMessage());
            }
        }

    }
}