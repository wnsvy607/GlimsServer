package com.glimps.glimpsserver.review.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.domain.ReviewPhoto;
import com.glimps.glimpsserver.review.infra.ReviewPhotoRepository;

@SpringBootTest
class ReviewPhotoServiceTest {
	private static final Review VALID_REVIEW = Review.builder()
		.id(5L)
		.build();

	private final ReviewPhotoRepository reviewPhotoRepository = mock(ReviewPhotoRepository.class);
	private ReviewPhotoService reviewPhotoService;

	@BeforeEach
	void setUp() {
		reviewPhotoService = new ReviewPhotoService(reviewPhotoRepository);
	}

	@Nested
	@DisplayName("createReviewPhoto 메서드는")
	class Describe_createReviewPhoto {
		@BeforeEach
		void setUp() {
			given(reviewPhotoRepository.saveAll(any(List.class))).will(invocation -> {
				List<ReviewPhoto> source = invocation.getArgument(0);
				List<ReviewPhoto> target = new LinkedList<>();
				long id = 1L;
				for (ReviewPhoto reviewPhoto : source) {
					target.add(ReviewPhoto.builder()
						.id(id)
						.review(reviewPhoto.getReview())
						.url(reviewPhoto.getUrl())
						.build());
					id += 1;
				}
				return target;
			});
		}

		@Nested
		@DisplayName("리뷰와 사진 url이 주어질 때")
		class Context_when_gives_review_and_photo_urls {
			@Test
			@DisplayName("리뷰 사진을 저장한다.")
			void it_saves_review_photo() {
				List<String> photoUrls = List.of("test1url", "test2url");

				List<ReviewPhoto> reviewPhoto = reviewPhotoService.createReviewPhotos(VALID_REVIEW, photoUrls);

				assertThat(reviewPhoto).hasSize(2);
				assertThat(reviewPhoto.get(0).getId()).isEqualTo(1L);
				assertThat(reviewPhoto.get(0).getReview()).isEqualTo(VALID_REVIEW);
				assertThat(reviewPhoto.get(0).getUrl()).isEqualTo("test1url");
				assertThat(reviewPhoto.get(1).getId()).isEqualTo(2L);
				assertThat(reviewPhoto.get(1).getReview()).isEqualTo(VALID_REVIEW);
				assertThat(reviewPhoto.get(1).getUrl()).isEqualTo("test2url");
				
				verify(reviewPhotoRepository).saveAll(any(List.class));
			}
		}
	}
}
