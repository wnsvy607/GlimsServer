package com.glimps.glimpsserver.review.application;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.domain.ReviewPhoto;
import com.glimps.glimpsserver.review.infra.ReviewPhotoRepository;

@Service
@Transactional
public class ReviewPhotoService {
	private final ReviewPhotoRepository reviewPhotoRepository;

	public ReviewPhotoService(ReviewPhotoRepository reviewPhotoRepository) {
		this.reviewPhotoRepository = reviewPhotoRepository;
	}

	public List<ReviewPhoto> createReviewPhotos(Review review, List<String> photoUrls) {
		List<ReviewPhoto> photos = new LinkedList<>();
		for (String url : photoUrls) {
			photos.add(ReviewPhoto.createReviewPhoto(review, url));
		}
		return reviewPhotoRepository.saveAll(photos);
	}

	public List<ReviewPhoto> updateReviewPhotos(Review review, List<String> photoUrls) {
		review.getReviewPhotos().clear();
		return createReviewPhotos(review, photoUrls);
	}

	public void deleteReviewPhotos(Review review) {
		reviewPhotoRepository.deleteAll(review.getReviewPhotos());
	}
}
