package com.glimps.glimpsserver.review.domain;

import java.util.ArrayList;
import java.util.List;

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
@AllArgsConstructor
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	protected Review(
		String title, String body, User user,
		Perfume perfume, double overallRating,
		double longevityRating, double sillageRatings
	) {
		this.title = title;
		this.body = body;
		this.user = user;
		this.perfume = perfume;
		this.overallRating = overallRating;
		this.longevityRating = longevityRating;
		this.sillageRating = sillageRatings;
	}

	public static Review createReview(ReviewCreateRequest reviewCreateRequest, User user, Perfume perfume) {
		String title = reviewCreateRequest.getTitle();
		String body = reviewCreateRequest.getBody();
		double overallRating = reviewCreateRequest.getOverallRatings();
		double longevityRating = reviewCreateRequest.getLongevityRatings();
		double sillageRating = reviewCreateRequest.getSillageRatings();
		return new Review(title, body, user, perfume, overallRating, longevityRating, sillageRating);
	}

	protected void addPhoto(ReviewPhoto photo) {
		if (!this.reviewPhotos.contains(photo)) {
			this.reviewPhotos.add(photo);
		}
	}
}
