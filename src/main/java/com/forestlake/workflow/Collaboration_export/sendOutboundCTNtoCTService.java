package com.forestlake.workflow.Collaboration_export;

import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("sendOutboundCTNtoCT")
public class sendOutboundCTNtoCTService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("场站的出口货物CTN被调到码头");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
//        runtimeService.correlateMessage("Message_depot_outbound_CTN_received");
        String businessKey = execution.getProcessBusinessKey();

        for (String url : endpointUrls) {
            try {
                SendTask.sendPostRequest(url, "Message_depot_outbound_CTN_received", businessKey);
                System.out.println("已发送至: " + url);
            } catch (IOException e) {
                System.err.println("发送失败: " + url + " 错误: " + e.getMessage());
            }
        }

    }
}