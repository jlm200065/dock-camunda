package com.forestlake.workflow.transaction;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class GenerateInvoice implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        throw new RuntimeException("抛异常测试回滚");
    }
}