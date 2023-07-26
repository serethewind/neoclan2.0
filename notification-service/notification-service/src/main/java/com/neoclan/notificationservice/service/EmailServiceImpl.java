package com.neoclan.notificationservice.service;

import com.neoclan.notificationservice.dto.EmailDetails;
import com.neoclan.notificationservice.dto.Response;
import com.neoclan.notificationservice.model.EmailEntity;
import com.neoclan.notificationservice.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
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
    public Response sendSimpleMessage(EmailDetails emailDetails) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailSender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessage());

           EmailEntity email = modelMapper.map(mailMessage, EmailEntity.class);
           emailRepository.save(email);

            javaMailSender.send(mailMessage);
            return Response.builder().response("Mail sent successfully").build();
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response sendMessageWithAttachment(EmailDetails emailDetails) {
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
            return Response.builder().response("Mail sent successfully").build();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
