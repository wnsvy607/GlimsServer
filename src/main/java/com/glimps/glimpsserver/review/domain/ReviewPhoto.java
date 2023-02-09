package com.glimps.glimpsserver.review.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewPhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;

	private String url;

	public ReviewPhoto(Review review, String url) {
		this.review = review;
		this.url = url;
	}

	@Builder
	public ReviewPhoto(Long id, Review review, String url) {
		this.id = id;
		this.review = review;
		this.url = url;
	}

	public static ReviewPhoto createReviewPhoto(Review review, String url) {
		ReviewPhoto reviewPhoto = new ReviewPhoto(review, url);
		review.addPhoto(reviewPhoto);
		return reviewPhoto;
	}
}
