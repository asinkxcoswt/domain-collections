package com.asinkxcoswt.domain.behavior.setting_support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FooRepository extends JpaRepository<FooEntity, UUID> {
    default FooEntity create() {
        return new FooEntity();
    }
    List<FooEntity> findByPriceGreaterThan(BigDecimal price);
    Optional<FooEntity> findByUuid(UUID uuid);
}