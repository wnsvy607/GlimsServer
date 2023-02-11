package com.glimps.glimpsserver.review.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.glimps.glimpsserver.common.error.CustomException;
import com.glimps.glimpsserver.perfume.application.PerfumeService;
import com.glimps.glimpsserver.perfume.domain.Perfume;
import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;
import com.glimps.glimpsserver.review.infra.ReviewRepository;
import com.glimps.glimpsserver.user.application.UserService;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;

@SpringBootTest
class ReviewServiceTest {
	private static final String TITLE = "제목입니다.";
	private static final String BODY = "본문입니다.";
	private static final String EXISTS_EMAIL = "exists@email.com";
	private static final String NOT_EXISTS_EMAIL = "notexists@email.com";
	private static final Long EXISTS_PERFUME_ID = 3L;
	private static final Long NOT_EXISTS_PERFUME_ID = 200L;
	private static final Long NEW_REVIEW_ID = 1L;
	private static final Long EXISTS_REVIEW_ID = 3L;
	private static final Long NOT_EXISTS_REVIEW_ID = 300L;

	private static final Review EXISTS_REVIEW = Review.builder()
		.id(EXISTS_REVIEW_ID)
		.title(TITLE)
		.body(BODY)
		.overallRating(3)
		.longevityRating(3)
		.sillageRating(3)
		.build();

	private static final User EXISTS_USER = User.builder()
		.id(1L)
		.email(EXISTS_EMAIL)
		.nickname("test_nickname")
		.role(RoleType.USER)
		.build();

	private static final Perfume EXISTS_PERFUME = Perfume.builder()
		.id(EXISTS_PERFUME_ID)
		.perfumeName("향수 이름")
		.brand("향수 브랜드")
		.build();

	private final ReviewRepository reviewRepository = mock(ReviewRepository.class);
	private final UserService userService = mock(UserService.class);
	private ReviewService reviewService;
	@MockBean
	private ReviewPhotoService reviewPhotoService;
	@MockBean
	private PerfumeService perfumeService;

	@BeforeEach
	void setUp() {
		reviewService = new ReviewService(reviewRepository, userService, reviewPhotoService, perfumeService);
	}

	@Nested
	@DisplayName("createReview 메서드는")
	class Describe_createReview {
		@Nested
		@DisplayName("사용자가 존재하고, 향수가 존재할 때")
		class Context_when_user_exists_and_perfume_exists {
			@BeforeEach
			void setUp() {
				given(userService.getUser(EXISTS_EMAIL)).willReturn(EXISTS_USER);
				given(perfumeService.getPerfume(EXISTS_PERFUME_ID)).willReturn(EXISTS_PERFUME);
				given(reviewRepository.save(any(Review.class))).will(invocation -> {
					Review source = invocation.getArgument(0);
					return Review.builder()
						.id(NEW_REVIEW_ID)
						.title(source.getTitle())
						.body(source.getBody())
						.overallRating(source.getOverallRating())
						.longevityRating(source.getLongevityRating())
						.sillageRating(source.getSillageRating())
						.user(source.getUser())
						.perfume(source.getPerfume())
						.build();
				});
			}

			@Test
			@DisplayName("리뷰를 생성한다.")
			void It_creates_review() {
				ReviewCreateRequest reviewCreateRequest =
					ReviewCreateRequest.builder()
						.title(TITLE)
						.body(BODY)
						.overallRatings(5.0)
						.longevityRatings(4.5)
						.sillageRatings(4.0)
						.build();

				Review review = reviewService.createReview(reviewCreateRequest, EXISTS_EMAIL);

				assertThat(review.getTitle()).isEqualTo(TITLE);
				assertThat(review.getBody()).isEqualTo(BODY);
				assertThat(review.getOverallRating()).isEqualTo(5.0);
				assertThat(review.getLongevityRating()).isEqualTo(4.5);
				assertThat(review.getSillageRating()).isEqualTo(4.0);
				assertThat(review.getUser()).isEqualTo(EXISTS_USER);
			}
		}

		@Nested
		@DisplayName("사용자가 존재하지 않고, 향수가 존재할 때")
		class Context_when_user_not_exists_and_perfume_exists {
			@BeforeEach
			void setUp() {
				given(userService.getUser(NOT_EXISTS_EMAIL)).willThrow(CustomException.class);
			}

			@Test
			@DisplayName("UserNotFoundException을 던진다.")
			void It_throws_UserNotFoundException() {
				ReviewCreateRequest reviewCreateRequest =
					ReviewCreateRequest.builder()
						.title(TITLE)
						.body(BODY)
						.perfumeId(EXISTS_PERFUME_ID)
						.overallRatings(5.0)
						.longevityRatings(4.5)
						.sillageRatings(4.0)
						.build();

				assertThatThrownBy(() -> reviewService.createReview(reviewCreateRequest, NOT_EXISTS_EMAIL))
					.isInstanceOf(CustomException.class);
			}
		}

		@Nested
		@DisplayName("사용자가 존재하고, 향수가 존재하지 않을 때")
		class Context_when_user_exists_and_perfume_not_exists {

			@BeforeEach
			void setUp() {
				given(userService.getUser(EXISTS_EMAIL)).willReturn(EXISTS_USER);
				given(perfumeService.getPerfume(NOT_EXISTS_PERFUME_ID)).willThrow(CustomException.class);
			}

			@Test
			@DisplayName("PerfumeNotFoundException을 던진다.")
			void It_throws_PerfumeNotFoundException() {
				ReviewCreateRequest reviewCreateRequest =
					ReviewCreateRequest.builder()
						.title(TITLE)
						.body(BODY)
						.perfumeId(NOT_EXISTS_PERFUME_ID)
						.overallRatings(5.0)
						.longevityRatings(4.5)
						.sillageRatings(4.0)
						.build();

				assertThatThrownBy(() -> reviewService.createReview(reviewCreateRequest, EXISTS_EMAIL))
					.isInstanceOf(CustomException.class);
			}
		}
	}

	@Nested
	@DisplayName("getReview 메소드는")
	class Describe_getReview {
		@Nested
		@DisplayName("리뷰가 존재할 때")
		class Context_when_review_exists {
			@BeforeEach
			void setUp() {
				given(reviewRepository.findById(EXISTS_REVIEW_ID)).willReturn(Optional.of(EXISTS_REVIEW));
			}

			@Test
			@DisplayName("리뷰를 반환한다.")
			void It_returns_review() {
				Review review = reviewService.getReview(EXISTS_REVIEW_ID);

				assertThat(review).isEqualTo(EXISTS_REVIEW);
				assertThat(review.getLongevityRating()).isEqualTo(3);
				assertThat(review.getSillageRating()).isEqualTo(3);
				assertThat(review.getOverallRating()).isEqualTo(3);

			}
		}

		@Nested
		@DisplayName("리뷰가 존재하지 않을 때")
		class Context_when_review_not_exists {
			@BeforeEach
			void setUp() {
				given(reviewRepository.findById(NOT_EXISTS_REVIEW_ID)).willReturn(Optional.empty());
			}

			@Test
			@DisplayName("ReviewNotFoundException을 던진다.")
			void It_throws_ReviewNotFoundException() {
				assertThatThrownBy(() -> reviewService.getReview(NOT_EXISTS_REVIEW_ID))
					.isInstanceOf(CustomException.class);
			}
		}
	}

	@Nested
	@DisplayName("getMyReviews메서드는")
	class Describe_getMyReviews {
		private final Pageable pageable = PageRequest.of(0, 2);

		Review element1 = Review.builder()
			.id(5L)
			.user(EXISTS_USER)
			.title("element1 title")
			.body("element1 body")
			.build();

		Review element2 = Review.builder()
			.id(6L)
			.user(EXISTS_USER)
			.title("element2 title")
			.body("element2 body")
			.build();

		Review element3 = Review.builder()
			.id(7L)
			.user(EXISTS_USER)
			.title("element3 title")
			.body("element3 body")
			.build();


		@Nested
		@DisplayName("사용자가 존재하고, 사용자가 작성한 리뷰가 존재할 때")
		class Context_when_user_exists_and_review_exists {
			@BeforeEach
			void setUp() {
				PageImpl<Review> page = new PageImpl<>(List.of(EXISTS_REVIEW, element1, element2, element3), pageable, 4);
				given(userService.getUser(EXISTS_EMAIL)).willReturn(EXISTS_USER);
				given(reviewRepository.findAllByUser(EXISTS_USER.getId(), pageable)).willReturn(page);
			}

			@Test
			@DisplayName("사용자가 작성한 리뷰 목록을 반환한다.")
			void It_returns_review_list() {
				Page<Review> reviews = reviewService.getMyReviews(0, 2, EXISTS_EMAIL);

				assertThat(reviews.getTotalElements()).isEqualTo(4);
				assertThat(reviews.getContent()).contains(EXISTS_REVIEW, element1, element2, element3);
				assertThat(reviews.getTotalPages()).isEqualTo(2);
			}
		}

		@Nested
		@DisplayName("사용자가 존재하고, 사용자가 작성한 리뷰가 존재하지 않을 때")
		class Context_when_user_exists_and_review_not_exists {
			@BeforeEach
			void setUp() {
				given(userService.getUser(EXISTS_EMAIL)).willReturn(EXISTS_USER);
				given(reviewRepository.findAllByUser(EXISTS_USER.getId(), pageable)).willReturn(Page.empty());
			}

			@Test
			@DisplayName("빈 리스트를 반환한다.")
			void It_returns_empty_list() {
				Page<Review> reviews = reviewService.getMyReviews(0, 2, EXISTS_EMAIL);

				assertThat(reviews).isEmpty();
			}
		}

		@Nested
		@DisplayName("사용자가 존재하지 않을 때")
		class Context_when_user_not_exists {
			@BeforeEach
			void setUp() {
				given(userService.getUser(NOT_EXISTS_EMAIL)).willThrow(CustomException.class);
			}

			@Test
			@DisplayName("UserNotFoundException을 던진다.")
			void It_throws_UserNotFoundException() {
				assertThatThrownBy(() -> reviewService.getMyReviews(0, 2, NOT_EXISTS_EMAIL))
					.isInstanceOf(CustomException.class);
			}
		}
	}
}
