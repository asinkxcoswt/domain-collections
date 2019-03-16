package com.asinkxcoswt.domain.behavior.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FooRepository extends JpaRepository<FooEntity, UUID> {
    FooEntity findByUuid(UUID uuid);
    default FooEntity create() {
        return new FooEntity();
    }
}
