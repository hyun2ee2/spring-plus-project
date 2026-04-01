package org.example.expert.domain.common.log.repository;

import org.example.expert.domain.common.log.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LogRepository extends JpaRepository<Log, Long> {

}
