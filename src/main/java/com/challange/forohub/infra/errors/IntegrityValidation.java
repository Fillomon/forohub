package com.challange.forohub.infra.errors;

public class IntegrityValidation  extends RuntimeException  {
    public IntegrityValidation(String s) {
        super(s);
    }
}
