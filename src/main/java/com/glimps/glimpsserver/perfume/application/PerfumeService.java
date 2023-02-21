package com.glimps.glimpsserver.perfume.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glimps.glimpsserver.common.error.EntityNotFoundException;
import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.perfume.domain.Perfume;
import com.glimps.glimpsserver.perfume.infra.PerfumeRepository;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;

@Service
@Transactional(readOnly = true)
public class PerfumeService {
	private final PerfumeRepository perfumeRepository;

	public PerfumeService(PerfumeRepository perfumeRepository) {
		this.perfumeRepository = perfumeRepository;
	}

	@Transactional
	public void updateRatings(Perfume perfume, ReviewCreateRequest reviewCreateRequest) {
		perfume.updateRatings(reviewCreateRequest.getOverallRatings(), reviewCreateRequest.getLongevityRatings(),
			reviewCreateRequest.getSillageRatings());
	}

	public Perfume getPerfumeById(Long perfumeId) {
		return findPerfume(perfumeId);
	}

	private Perfume findPerfume(Long perfumeId) {
		return perfumeRepository.findById(perfumeId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.PERFUME_NOT_FOUND, perfumeId));
	}
}
