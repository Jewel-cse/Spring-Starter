package dev.start.init.service.amqp;

import dev.start.init.mapper.UserMapper;
import dev.start.init.service.mail.EmailService;
import dev.start.init.web.payload.pojo.PromotionPayload;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static dev.start.init.dto.response.error.CustomErrorAdvice.logger;

@Service
@RequiredArgsConstructor
public class Consumer {

    private final EmailService emailService;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeInvitation(PromotionPayload payload) throws TemplateException, MessagingException, IOException {
        logger.debug("Consumed Email invitation {}",payload.toString());
        emailService.sendMarketingEmail(UserMapper.MAPPER.toUserDto(payload.getUser()), payload.getProductImageUrl(),payload.getFeatures(),payload.getCtaLink());
    }
}
