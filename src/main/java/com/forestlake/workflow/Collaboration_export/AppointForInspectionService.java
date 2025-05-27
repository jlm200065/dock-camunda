package com.forestlake.workflow.Collaboration_export;

import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("appointForInspection")
public class AppointForInspectionService implements JavaDelegate {



    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("报关行向海关预约查验信息");

        String messageName = "Message_Appointment_received";  // 或从 execution.getVariable 中获取
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
