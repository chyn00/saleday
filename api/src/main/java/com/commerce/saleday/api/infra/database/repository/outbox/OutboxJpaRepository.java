package com.commerce.saleday.api.infra.database.repository.outbox;

import com.commerce.saleday.domain.outbox.model.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxJpaRepository extends JpaRepository<OutboxMessage, Long> {

}
