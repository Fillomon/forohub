package com.challange.forohub.domains.users;

import jakarta.validation.constraints.Email;

public record UserAuthenticationData(String mail, @Email String password) {
    
}
