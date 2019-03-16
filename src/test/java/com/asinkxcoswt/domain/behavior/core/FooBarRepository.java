package com.asinkxcoswt.domain.behavior.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FooBarRepository extends JpaRepository<FooBarEntity, UUID> {
    FooBarEntity findByUuid(UUID uuid);

    default FooBarEntity create() {
        return new FooBarEntity();
    }
}
