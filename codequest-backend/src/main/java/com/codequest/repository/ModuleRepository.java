package com.codequest.repository;

import com.codequest.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByTrackIdOrderByOrderIndexAsc(Long trackId);

    long countByTrackId(Long trackId);
}
