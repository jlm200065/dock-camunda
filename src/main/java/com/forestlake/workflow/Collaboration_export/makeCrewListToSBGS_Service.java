package com.forestlake.workflow.Collaboration_export;

import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("makeCrewListToSBGS")
public class makeCrewListToSBGS_Service implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("船代把船员信息移交边防");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
//        runtimeService.correlateMessage("Message_CrewList_received");

        String businessKey = execution.getProcessBusinessKey();

        for (String url : endpointUrls) {
            try {
                SendTask.sendPostRequest(url, "Message_CrewList_received", businessKey);
                System.out.println("已发送至: " + url);
            } catch (IOException e) {
                System.err.println("发送失败: " + url + " 错误: " + e.getMessage());
            }
        }

    }
}