package org.example.expert.domain.common.log.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    public Log(String code, String message) {
        this.code = code;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
