package com.challange.forohub.domains.topics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataTopic(@NotNull String title,
                        @NotBlank
                        String message,
                        @NotNull
                        Long courseId) {

} 