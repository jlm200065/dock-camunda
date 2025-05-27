package com.forestlake.workflow.servicetask;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ReserveRepairService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("进入到预约家电修理任务");
        String currentActivityName = execution.getCurrentActivityName();
        // 预约的具体调用
        //...
        String processDefinitionId = execution.getProcessDefinitionId();
        System.out.println("当前活动名称："+currentActivityName+" 流程定义id: "+processDefinitionId);

    }
}
