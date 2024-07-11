package com.challange.forohub.domains.topics.validations;

import com.challange.forohub.domains.topics.TopicDataUpdate;

import com.challange.forohub.domains.users.User;

public interface ValidatorUpdateTopical {
    
    void validate(TopicDataUpdate data, User user);
}
