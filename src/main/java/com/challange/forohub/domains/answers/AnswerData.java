package com.challange.forohub.domains.answers;

import jakarta.validation.constraints.NotBlank;

public record AnswerData(@NotBlank String message) {
    
}
