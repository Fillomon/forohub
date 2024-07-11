package com.challange.forohub.domains.topics;

import jakarta.validation.constraints.NotNull;

public record TopicDataUpdate(@NotNull Long id, String title, String message) {
    
}
