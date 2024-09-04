package dev.start.init.service.mail.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import dev.start.init.entity.auth.User;
import dev.start.init.service.mail.EmailService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Time;
import java.time.Year;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of EmailService for sending registration and password reset emails.
 * <p>
 * This service uses JavaMailSender to send emails. Configuration properties are
 * externalized in application properties.
 * </p>
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final Configuration freemarkerConfig;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, Configuration freemarkerConfig) {
        this.javaMailSender = javaMailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    @Value("${spring.mail.from}")
    private String fromAddress;

    @Value("${app.url}")
    private String appUrl;

    @Override
    public void sendVerificationEmail(User user) throws IOException, TemplateException, MessagingException {
        // Create a new MimeMessage and MimeMessageHelper for each email
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String verificationUrl = appUrl + "/api/v1/auth/verify-email?token=" + user.getVerificationToken();
        Map<String, Object> model = new HashMap<>();
        model.put("userName", user.getLastName());
        model.put("verificationLink", verificationUrl);
        model.put("year",Year.now().getValue());

        Template template = freemarkerConfig.getTemplate("verification.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(model, stringWriter);

        helper.setTo(user.getEmail());
        helper.setSubject("Verify your email address");
        MimeMessageHelper(message, helper, stringWriter, fromAddress, javaMailSender);
    }

    @Override
    public void sendPasswordResetEmail(User user) throws IOException, TemplateException, MessagingException {
        // Create a new MimeMessage and MimeMessageHelper for each email
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String resetPasswordUrl = appUrl + "/api/v1/auth/reset-password?token=" + user.getPasswordResetToken();

        Map<String, Object> model = new HashMap<>();
        model.put("userName", user.getLastName());
        model.put("year",Year.now().getValue());
        model.put("resetPasswordUrl", resetPasswordUrl);

        Template template = freemarkerConfig.getTemplate("password-reset.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(model, stringWriter);

        helper.setTo(user.getEmail());
        helper.setSubject("Password Reset Request");
        MimeMessageHelper(message, helper, stringWriter, fromAddress, javaMailSender);
    }

    private static void MimeMessageHelper(MimeMessage message, MimeMessageHelper helper, StringWriter stringWriter, String fromAddress, JavaMailSender javaMailSender) throws MessagingException {
        helper.setText(stringWriter.toString(), true);
        helper.setFrom(fromAddress);

        helper.addInline("facebook", new ClassPathResource("templates/email/images/facebook.png"));
        helper.addInline("twitter", new ClassPathResource("templates/email/images/twitter.png"));
        helper.addInline("instagram", new ClassPathResource("templates/email/images/instagram.png"));
        helper.addInline("linkedin", new ClassPathResource("templates/email/images/linkedin.png"));
        helper.addInline("GIF_password",new ClassPathResource("templates/email/images/GIF_password.gif"));
        //if anything that will not be reference in email count as attachment
        javaMailSender.send(message);
    }
}


