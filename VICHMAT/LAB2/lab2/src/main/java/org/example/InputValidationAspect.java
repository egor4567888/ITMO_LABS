package org.example;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InputValidationAspect {

    @AfterThrowing(pointcut = "execution(* org.example.UserInputHandler.process*(..))", throwing = "ex")
    public void logInputError(Exception ex) {
        System.err.println("Ошибка ввода: " + ex.getMessage());
    }
}