package com.asinkxcoswt.domain.behavior.entity_manager_support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FooRepository extends JpaRepository<FooEntity, UUID> {
    default FooEntity create() {
        return new FooEntity();
    }
}
