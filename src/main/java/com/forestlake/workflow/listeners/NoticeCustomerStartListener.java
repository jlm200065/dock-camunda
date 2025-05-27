package com.forestlake.workflow.listeners;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("noticeCustomer")
public class NoticeCustomerStartListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        System.out.println("进入通知客户任务");
        String homeAddress = String.valueOf(delegateExecution.getVariable("homeAddress"));
        TimeUnit.SECONDS.sleep(1);

        System.out.println("您好，师傅正在赶往"+homeAddress+"为您修理家电");
    }
}
