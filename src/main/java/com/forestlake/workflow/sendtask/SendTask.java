package com.forestlake.workflow.sendtask;


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

@Component("sendTask")
public class SendTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("sendTask发送消息");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Message_receive_task_test")
                .processInstanceBusinessKey("message_bussinessKey")
                .correlate();
    }

    // 可根据实际需要替换为配置文件注入
    public static final List<String> endpointUrls = Arrays.asList(
            "http://localhost:10006/sendMessage"
            // 添加更多服务地址
    );
    public static void sendPostRequest(String url, String messageName, String businessKey) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setDoOutput(true);

        String jsonPayload = String.format(
                "{\"messageName\":\"%s\",\"businessKey\":\"%s\"}",
                messageName, businessKey
        );

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HTTP响应码: " + responseCode);
        }
    }
}
