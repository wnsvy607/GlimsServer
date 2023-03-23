package com.glimps.glimpsserver.review.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.domain.ReviewPhoto;
import com.glimps.glimpsserver.review.infra.ReviewPhotoRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ReviewPhotoService {
	private final ReviewPhotoRepository reviewPhotoRepository;

	public ReviewPhotoService(ReviewPhotoRepository reviewPhotoRepository) {
		this.reviewPhotoRepository = reviewPhotoRepository;
	}

	public List<ReviewPhoto> createReviewPhotos(Review review, List<String> photoUrls, MultipartFile file) {
		List<ReviewPhoto> photos = new LinkedList<>();
		for (String url : photoUrls) {
//			uploadFile("glimps", UUID.randomUUID().toString(), file);
			photos.add(ReviewPhoto.createReviewPhoto(review, url));
		}
		return reviewPhotoRepository.saveAll(photos);
	}

	private String uploadFile(String bucketName, String keyName, File file) {
		PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
		s3Client.putObject(request);

		return s3Client.getUrl(bucketName, keyName).toString();
	}

	public List<ReviewPhoto> updateReviewPhotos(Review review, List<String> photoUrls) {
		review.getReviewPhotos().clear();
		return createReviewPhotos(review, photoUrls);
	}

	public void deleteReviewPhotos(Review review) {
		reviewPhotoRepository.deleteAll(review.getReviewPhotos());
	}
}
