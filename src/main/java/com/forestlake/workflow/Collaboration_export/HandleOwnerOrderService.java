package com.forestlake.workflow.Collaboration_export;


import com.forestlake.workflow.sendtask.SendTask;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.forestlake.workflow.sendtask.SendTask.endpointUrls;

@Component("handleOwnerOrder")
public class HandleOwnerOrderService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String telephone = (String)execution.getVariable("telephone");

        System.out.println("处理货主的订单，货主电话"+telephone);
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

    }
}
