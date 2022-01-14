package com.dooapp.petitdej.service;

import com.dooapp.petitdej.domain.PetitDej;
import com.dooapp.petitdej.domain.User;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

@Service
@Primary
public class ExtendedMailService extends MailService {

    private final Logger log = LoggerFactory.getLogger(ExtendedMailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private JHipsterProperties jHipsterProperties;
    private MessageSource messageSource;
    private SpringTemplateEngine templateEngine;

    public ExtendedMailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        super(jHipsterProperties, javaMailSender, messageSource, templateEngine);
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    public void sendReminderEmail(User user, PetitDej petitDej) {
        log.debug("Sending reminder email to '{}'", user.getEmail());
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("user", user);
        emailVariables.put("petitDej", petitDej);
        sendEmailFromTemplate(user, "mail/reminderEmail", "email.reminder.title", emailVariables);
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey, Map<String, Object> emailVariables) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        if ((emailVariables != null) && !(emailVariables.isEmpty())) {
            for (Map.Entry<String, Object> emailVariable : emailVariables.entrySet()) {
                context.setVariable(emailVariable.getKey(), emailVariable.getValue());
            }
        }
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
}
