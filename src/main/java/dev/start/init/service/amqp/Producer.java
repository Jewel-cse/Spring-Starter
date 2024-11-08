package dev.start.init.service.amqp;

import dev.start.init.web.payload.pojo.PromotionPayload;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static dev.start.init.dto.response.error.CustomErrorAdvice.logger;

@Service
public class Producer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public Producer(
            @Value("${rabbitmq.exchange.name}") String exchange,
            @Value("${rabbitmq.routing.key}") String routingKey,
            RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void  sendInvitation(PromotionPayload payload){
        logger.debug("Sending Invitation: {}", payload);
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }

}
