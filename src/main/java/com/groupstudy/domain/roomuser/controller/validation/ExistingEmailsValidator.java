package com.groupstudy.domain.roomuser.controller.validation;

import com.groupstudy.domain.user.entity.User;
import com.groupstudy.domain.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExistingEmailsValidator implements ConstraintValidator<ExistingEmails, List<String>> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(List<String> emails, ConstraintValidatorContext context) {
        if (emails == null || emails.isEmpty()) return true;

        List<String> existingEmails = userRepository.findByEmails(emails)
                .stream().map(User::getEmail).toList();
        return new HashSet<>(existingEmails).containsAll(emails);
    }
}
