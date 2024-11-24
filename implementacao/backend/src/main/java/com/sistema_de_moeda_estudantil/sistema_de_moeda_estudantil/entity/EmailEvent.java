package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import java.time.LocalDateTime;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.EmailStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "email_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmailEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_event_generator")
    @SequenceGenerator(name = "email_event_generator", sequenceName = "email_event_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Lob
    private byte[] attachmentContent;

    private String attachmentName;

    private String attachmentContentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "next_retry")
    private LocalDateTime nextRetry;

    @Column(name = "template_name")
    private String templateName;
}
