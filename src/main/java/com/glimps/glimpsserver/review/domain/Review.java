package com.glimps.glimpsserver.review.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.glimps.glimpsserver.perfume.domain.Perfume;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;
import com.glimps.glimpsserver.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private UUID uuid;

	@Column(name = "title")
	private String title;

	@Column(name = "body")
	private String body;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@JoinColumn(name = "perfume_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Perfume perfume;

	@Builder.Default
	@OneToMany(mappedBy = "review")
	private List<ReviewPhoto> reviewPhotos = new ArrayList<>();

	@Column(name = "hearts_cnt")
	private int heartsCnt;

	private double overallRating;

	private double longevityRating;

	private double sillageRating;

	public static Review createReview(ReviewCreateRequest reviewCreateRequest, User user, Perfume perfume) {
		return Review.builder()
			.uuid(UUID.randomUUID())
			.title(reviewCreateRequest.getTitle())
			.body(reviewCreateRequest.getBody())
			.user(user)
			.perfume(perfume)
			.overallRating(reviewCreateRequest.getOverallRatings())
			.longevityRating(reviewCreateRequest.getLongevityRatings())
			.sillageRating(reviewCreateRequest.getSillageRatings())
			.build();
	}

	protected void addPhoto(ReviewPhoto photo) {
		if (!this.reviewPhotos.contains(photo)) {
			this.reviewPhotos.add(photo);
		}
	}
}
