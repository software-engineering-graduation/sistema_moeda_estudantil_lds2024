package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.CupomResgate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.EmailEvent;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.EmailStatus;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmailEventRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.configuration.ThymeleafConfig;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailEventRepository emailEventRepository;
    private final String fromEmail;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private TemplateEngine templateEngine;

    public EmailService(
            JavaMailSender mailSender,
            EmailEventRepository emailEventRepository,
            @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.emailEventRepository = emailEventRepository;
        this.fromEmail = fromEmail;
        this.templateEngine = new ThymeleafConfig().templateEngine();
    }

    @Transactional
    public void queueEmail(String to, String subject, String content, byte[] attachmentContent,
            String attachmentName, String attachmentContentType) {
        EmailEvent event = EmailEvent.builder()
                .recipient(to)
                .subject(subject)
                .content(content)
                .attachmentContent(attachmentContent)
                .attachmentName(attachmentName)
                .attachmentContentType(attachmentContentType)
                .status(EmailStatus.AGUARDANDO)
                .retryCount(0)
                .build();

        emailEventRepository.save(event);
    }

    @Scheduled(fixedDelay = 10000) // every 60 seconds
    @Async
    @Transactional
    public void processEmails() {
        Pageable limit = PageRequest.of(0, 20);
        List<EmailEvent> pendingEmails = emailEventRepository.findPendingEmails(EmailStatus.AGUARDANDO,
                LocalDateTime.now(), limit);

        logger.info("Processing pending {} emails", pendingEmails.size());

        for (EmailEvent event : pendingEmails) {
            try {
                sendEmail(event);
                event.setStatus(EmailStatus.ENVIADO);
                event.setNextRetry(null);
                event.setErrorMessage(null);
                event.setRetryCount(null);
            } catch (Exception e) {
                handleFailedEmail(event, e);
            }
            emailEventRepository.save(event);
        }
    }

    private void sendEmail(EmailEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(event.getRecipient());
        helper.setSubject(event.getSubject());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(event.getContent(), "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        if (event.getAttachmentContent() != null) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(
                    event.getAttachmentContent(),
                    event.getAttachmentContentType());
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(event.getAttachmentName());
            multipart.addBodyPart(attachmentPart);
        }

        message.setContent(multipart);
        mailSender.send(message);
        log.info("Email sent to: {}", event.getRecipient());
    }

    private void handleFailedEmail(EmailEvent event, Exception e) {
        event.setStatus(EmailStatus.FALHOU);
        event.setErrorMessage(e.getMessage());
        event.setRetryCount(event.getRetryCount() + 1);
        event.setNextRetry(LocalDateTime.now().plusMinutes((long) Math.pow(2, event.getRetryCount())));
        log.error("Failed to send email to: {}", event.getRecipient(), e);
    }

    public MimeMessage createMimeMessage() {
        return mailSender.createMimeMessage();
    }

    public void sendCupomResgateEmails(CupomResgate cupomResgate) throws MessagingException {

        Context context = new Context();
        context.setVariable("aluno", cupomResgate.getAluno());
        context.setVariable("empresa", cupomResgate.getEmpresa());
        context.setVariable("codigo", cupomResgate.getCodigo());
        context.setVariable("vantagem", cupomResgate.getVantagem());
        context.setVariable("valor", cupomResgate.getValor());
        context.setVariable("data", cupomResgate.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        String alunoEmailContent = templateEngine.process("aluno_email_template", context);
        // sendEmail(cupomResgate.getAluno().getEmail(), "Seu Cupom de Resgate",alunoEmailContent);
        this.queueEmail(cupomResgate.getAluno().getEmail(), "Seu Cupom de Resgate", alunoEmailContent, null, null, null);

        String empresaEmailContent = templateEngine.process("empresa_email_template", context);
        // sendEmail(cupomResgate.getEmpresa().getEmail(), "Novo Cupom Resgatado", empresaEmailContent);
        this.queueEmail(cupomResgate.getEmpresa().getEmail(), "Novo Cupom Resgatado", empresaEmailContent, null, null, null);
    }

}