package com.forestlake.workflow.ScheduleService;

import com.alibaba.fastjson.JSONObject;
import groovy.util.logging.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ExecutionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
@Slf4j
@Service
@Transactional
public class MessageEventService {




    public static class MessageWithVariables {
        private String messageName;

        private String businessKey;

        private Map<String, Object> processVariables;

        public MessageWithVariables(String messageName, String businessKey, Map<String, Object> processVariables) {
            this.messageName = messageName;
            this.businessKey = businessKey;
            this.processVariables = processVariables;
        }
    }

    public List<MessageWithVariables>  messageWithVariablesList = new CopyOnWriteArrayList<>();

    public List<MessageWithVariables> findAll(){

        return messageWithVariablesList;
    }

    public void sendMessage(String messageName, String businessKey, Map<String, Object> processVariables) {
        messageWithVariablesList.add(new MessageWithVariables(messageName, businessKey, processVariables));
    }
//

    @KafkaListener(topics = "sbgs", groupId = "spring-kafka-group")
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = (String) kafkaMessage.get();
            JSONObject jsonObject = JSONObject.parseObject(message);
            String messageName = jsonObject.getString("messageName");

            String businessKey = jsonObject.getString("businessKey");

            System.out.println("messageName: " + messageName);
            System.out.println("businessKey: " + businessKey);

            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();

            switch (messageName) {
                case "Message_ask_for_CTN":

                    runtimeService.startProcessInstanceByKey("Process_Depot",  businessKey);
                    break;
                case "Message_SO_received":

                    runtimeService.startProcessInstanceByKey("Process_SA", businessKey);
                    break;
                case "Message_Owner_order_received":

                    runtimeService.startProcessInstanceByKey("Process_FF", businessKey);
                    break;
                case "Message_CrewList_received":

                    runtimeService.startProcessInstanceByKey("Process_SBGS", businessKey);
                    break;
                case "Message_CB_order_received":

                    runtimeService.startProcessInstanceByKey("Process_CB", businessKey);
                    break;
                default:
                    sendMessage(messageName, businessKey, null);
                    break;
            }
        }
    }



    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void receiveMessageWithVariables() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        for (MessageWithVariables messageWithVariables : messageWithVariablesList) {
            System.out.println("ScheduledTask: "+messageWithVariables.messageName + "  "+ messageWithVariables.businessKey);
            ExecutionQuery executionQuery = runtimeService.createExecutionQuery()
                    .messageEventSubscriptionName(messageWithVariables.messageName)
                    .processInstanceBusinessKey(messageWithVariables.businessKey);
            if (executionQuery.count() > 0) {
                Execution execution = executionQuery.list().get(0);
                runtimeService.messageEventReceived(messageWithVariables.messageName, execution.getId(), messageWithVariables.processVariables);
                messageWithVariablesList.remove(messageWithVariables);
                break;
            }
        }
    }
}
