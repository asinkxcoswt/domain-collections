package com.asinkxcoswt.domain.behavior.i18n_support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface I18NFooRepository extends JpaRepository<I18NFooEntity, UUID> {
    default I18NFooEntity create(String firstName, String lastName) {
        return new I18NFooEntity(firstName, lastName);
    }
}