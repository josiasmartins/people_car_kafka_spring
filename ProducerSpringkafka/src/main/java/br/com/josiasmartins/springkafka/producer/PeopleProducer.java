package br.com.josiasmartins.springkafka.producer;

import br.com.springkafka.People;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PeopleProducer {

    private static final Logger logger = LoggerFactory.getLogger(PeopleProducer.class);

    private final String topicName;
    private final KafkaTemplate<String, People> kafkaTemplate;

    public PeopleProducer(@Value("${topic.name}") String topicName, KafkaTemplate<String, People> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(People people) {
        kafkaTemplate.send(topicName, people).addCallback(
                success -> logger.info("Mensagem Enviada com sucesso"),
                fail -> logger.error("Falha ao enviar mensagem")
        );
    }



}
