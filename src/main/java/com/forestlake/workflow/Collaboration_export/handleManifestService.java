package com.forestlake.workflow.Collaboration_export;

import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("handleManifest")
public class handleManifestService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("生成订舱单");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        System.out.println("订舱单发送给海关、货代、集装箱码头");

//        runtimeService.correlateMessage("Message_Manifest_received");
//        runtimeService.correlateMessage("Message_CT_Manifest_received");
//        runtimeService.correlateMessage("Message_CB_Manifest_received");
        String businessKey = execution.getProcessBusinessKey();

        for (String url : endpointUrls) {
            try {
                SendTask.sendPostRequest(url, "Message_FF_Manifest_received", businessKey);
                SendTask.sendPostRequest(url, "Message_CT_Manifest_received", businessKey);
                SendTask.sendPostRequest(url, "Message_CB_Manifest_received", businessKey);
                System.out.println("已发送至: " + url);
            } catch (IOException e) {
                System.err.println("发送失败: " + url + " 错误: " + e.getMessage());
            }
        }
    }
}
