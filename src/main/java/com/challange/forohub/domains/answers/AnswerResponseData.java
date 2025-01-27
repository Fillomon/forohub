package com.challange.forohub.domains.answers;

import java.time.LocalDateTime;

public record AnswerResponseData(Long id, Long topicId, Long authorId, String message, LocalDateTime creation) {
    
    public AnswerResponseData(Answer data){
        this(data.getId(),data.getTopic().getId(),data.getAuthor().getId(),data.getMessage(),data.getCreation());
    }
}
