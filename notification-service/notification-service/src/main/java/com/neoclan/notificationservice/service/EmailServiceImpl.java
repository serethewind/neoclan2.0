package com.neoclan.notificationservice.service;

import com.neoclan.notificationservice.dto.EmailDetails;
import com.neoclan.notificationservice.dto.Response;
import com.neoclan.notificationservice.model.EmailEntity;
import com.neoclan.notificationservice.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private EmailRepository emailRepository;
    private JavaMailSender javaMailSender;
    private ModelMapper modelMapper;

    @Value("${spring.mail.username}")
    private String mailSender;


    public EmailServiceImpl(EmailRepository emailRepository, JavaMailSender javaMailSender, ModelMapper modelMapper) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public String sendSimpleMessage(EmailDetails emailDetails) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailSender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessage());

            javaMailSender.send(mailMessage);

            EmailEntity email = EmailEntity.builder()
                    .message(mailMessage.getText())
                    .subject(mailMessage.getSubject())
                    .attachment(null)
                    .build();

            emailRepository.save(email);

            log.info("Message sent successfully");
            return "Mail sent successfully";
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public String sendMessageWithAttachment(EmailDetails emailDetails) {
        try {
            //first tap into javaMailSender.createMimeMessage() to create Mime Message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // tap into  MimeMessageHelper.
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(mailSender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            mimeMessageHelper.setText(emailDetails.getMessage());

            //to handle attachment
            FileSystemResource fileSystemResource = new FileSystemResource(new File(emailDetails.getAttachment()));
            mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);

            //tap into javaMailSender to send message
            javaMailSender.send(mimeMessage);
            return "Mail sent successfully";

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
