package com.glimps.glimpsserver.perfume.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glimps.glimpsserver.perfume.domain.Perfume;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
}
