package com.challange.forohub.domains.answers.validations;

import com.challange.forohub.domains.users.User;

public interface ValidatorUpdateAnswer {
    void validate(Long id, User user);
}
