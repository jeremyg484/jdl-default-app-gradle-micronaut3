package tech.jhipster.sample.service;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

@Factory
public class MailSenderFactory {

    @Value("${mail.host:localhost}")
    private String host;

    @Value("${mail.port:25}")
    private int port;

    @Value("${mail.username:''}")
    private String username;

    @Value("${mail.password:''}")
    private String password;

    @Singleton
    Mailer mailSender() {
        return MailerBuilder.withSMTPServer(host, port, username, password).buildMailer();
    }
}
