package com.glimps.glimpsserver.review.presentation;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.uuid.Generators;
import com.glimps.glimpsserver.common.config.SecurityConfig;
import com.glimps.glimpsserver.common.domain.CustomPage;
import com.glimps.glimpsserver.common.domain.CustomPageImpl;
import com.glimps.glimpsserver.common.error.EntityNotFoundException;
import com.glimps.glimpsserver.common.error.ErrorCode;
import com.glimps.glimpsserver.perfume.domain.Perfume;
import com.glimps.glimpsserver.review.application.ReviewService;
import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.session.application.AuthenticationService;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;

@WebMvcTest(controllers = ReviewController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
	})
@TestPropertySource(properties = {
	"spring.config.location=classpath:application.yml"
})
class ReviewControllerTest {
	private static final String TITLE = "제목입니다.";
	private static final String BODY = "본문입니다.";
	private static final String EXISTS_EMAIL = "exists@email.com";
	private static final Long NOT_EXISTS_USER_ID = 200L;
	private static final UUID EXISTS_REVIEW_UUID = Generators.timeBasedGenerator().generate();
	private static final UUID EXISTS_PERFUME_UUID = Generators.timeBasedGenerator().generate();
	private static final UUID NOT_EXISTS_REVIEW_UUID = Generators.timeBasedGenerator().generate();
	private static final UUID NOT_EXISTS_PERFUME_UUID = UUID.randomUUID();
	private static final Perfume EXISTS_PERFUME = Perfume.builder()
		.uuid(Generators.timeBasedGenerator().generate())
		.perfumeName("향수 이름")
		.brand("향수 브랜드")
		.build();

	private static final User EXISTS_USER = User.builder()
		.email(EXISTS_EMAIL)
		.nickname("test_nickname")
		.role(RoleType.USER)
		.build();

	private final Review EXISTS_REVIEW = Review.builder()
		.title(TITLE)
		.body(BODY)
		.uuid(EXISTS_REVIEW_UUID)
		.overallRatings(5)
		.longevityRatings(3)
		.sillageRatings(3)
		.heartsCnt(5)
		.perfume(EXISTS_PERFUME)
		.user(EXISTS_USER)
		.build();

	private final Review SECOND_REVIEW = Review.builder()
		.title(TITLE + "2")
		.body(BODY)
		.uuid(Generators.timeBasedGenerator().generate())
		.overallRatings(5)
		.longevityRatings(3)
		.sillageRatings(3)
		.heartsCnt(10)
		.perfume(EXISTS_PERFUME)
		.user(EXISTS_USER)
		.build();

	private final Review THIRD_REVIEW = Review.builder()
		.title(TITLE + "3")
		.body(BODY)
		.uuid(Generators.timeBasedGenerator().generate())
		.overallRatings(5)
		.longevityRatings(3)
		.sillageRatings(3)
		.heartsCnt(7)
		.perfume(EXISTS_PERFUME)
		.user(EXISTS_USER)
		.build();

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ReviewService reviewService;

	@MockBean
	private AuthenticationService authenticationService;

	@Value("${spring.mvc.servlet.path}")
	private String contextPath;

	@BeforeEach
	void setUp() {
		assertThat(contextPath).isNotBlank();
		((MockServletContext)mvc.getDispatcherServlet().getServletContext()).setContextPath(contextPath);
	}

	protected MockHttpServletRequestBuilder createGetRequest(String request) {
		return get(contextPath + request).contextPath(contextPath);
	}

	// GET 요청 테스트

	// 인증이 필요없는 로직
	@Nested
	@WithMockUser
	@DisplayName("GET /api/v1/reviews")
	class Describe_list {
		@Nested
		@DisplayName("리뷰가 존재한다면")
		class Context_when_review_exists {
			@BeforeEach
			void setUp() {
				List<Review> reviews = List.of(EXISTS_REVIEW, SECOND_REVIEW, THIRD_REVIEW);
				CustomPage<Review> results = new CustomPageImpl<>(reviews, 1, 10, 3);
				given(reviewService.getReviews(any())).willReturn(results);
			}

			@Test
			@DisplayName("상태코드 200과 리뷰 목록을 페이지로 나누어 응답한다.")
			void It_responds_200_and_reviews_by_pagination() throws Exception {
				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
				map.add("offset", "1");
				map.add("limit", "10");

				mvc.perform(createGetRequest("/reviews")
						.params(map)
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString(TITLE)))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}

		@Nested
		@DisplayName("리뷰가 존재하지 않는다면")
		class Context_when_review_not_exists {
			@BeforeEach
			void setUp() {
				List<Review> reviews = List.of();
				CustomPage<Review> results = new CustomPageImpl<>(reviews, 1, 10, 0);
				given(reviewService.getReviews(any())).willReturn(results);
			}

			@Test
			@DisplayName("상태코드 200과 빈 목록을 페이지로 나누어 응답한다.")
			void It_responds_200_and_empty_list_by_pagination() throws Exception {
				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
				map.add("offset", "1");
				map.add("limit", "10");

				mvc.perform(createGetRequest("/reviews")
						.params(map)
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString("[]")))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("GET /api/v1/reviews/{reviewId}")
	class Describe_detail {
		@Nested
		@DisplayName("리뷰가 존재한다면")
		class Context_when_review_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getReviewById(EXISTS_REVIEW_UUID)).willReturn(EXISTS_REVIEW);
			}

			@Test
			@DisplayName("상태코드 200과 리뷰를 응답한다.")
			void It_responds_200_and_review() throws Exception {
				mvc.perform(createGetRequest("/reviews/" + EXISTS_REVIEW_UUID)
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString(BODY)))
					.andExpect(content().string(containsString(TITLE)))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}

		@Nested
		@DisplayName("리뷰가 존재하지 않는다면")
		class Context_when_review_not_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getReviewById(any())).willThrow(new EntityNotFoundException(
					ErrorCode.REVIEW_NOT_FOUND, NOT_EXISTS_REVIEW_UUID));
			}

			@Test
			@DisplayName("상태코드 404를 응답한다.")
			void It_responds_404() throws Exception {
				mvc.perform(createGetRequest("/reviews/" + NOT_EXISTS_REVIEW_UUID))
					.andExpect(status().isNotFound())
					.andDo(print());
			}
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("GET /api/v1/reviews/recentReviews")
	class Describe_recentReviews {
		@Nested
		@DisplayName("최근 리뷰가 존재한다면")
		class Context_when_recent_review_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getRecentReviews()).willReturn(List.of(EXISTS_REVIEW, SECOND_REVIEW, THIRD_REVIEW));
			}

			@Test
			@DisplayName("상태코드 200과 최근 리뷰를 응답한다")
			void It_responds_200_and_recent_reviews_by_pagination() throws Exception {
				mvc.perform(createGetRequest("/reviews/recentReviews")
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString(TITLE)))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}

		@Nested
		@DisplayName("최근 리뷰가 존재하지 않는다면")
		class Context_when_recent_review_not_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getRecentReviews()).willReturn(List.of());
			}

			@Test
			@DisplayName("상태코드 200과 빈 목록을 응답한다.")
			void It_responds_200_and_empty_list_by_pagination() throws Exception {
				mvc.perform(createGetRequest("/reviews/recentReviews")
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString("[]")))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("GET /api/v1/reviews/reviews/perfumeReviews?perfumeId={perfumeUuid}")
	class Describe_perfumeReviews {
		@Nested
		@DisplayName("향수가 존재하고 리뷰가 존재한다면")
		class Context_when_perfume_exists_and_review_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getPerfumeReviews(EXISTS_PERFUME_UUID)).willReturn(
					List.of(EXISTS_REVIEW, SECOND_REVIEW, THIRD_REVIEW));
			}

			@Test
			@DisplayName("상태코드 200과 향수 리뷰를 응답한다.")
			void It_responds_200_and_perfume_reviews() throws Exception {
				mvc.perform(createGetRequest("/reviews/perfumeReviews")
						.param("perfumeUuid", EXISTS_PERFUME_UUID.toString())
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString(TITLE)))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}

		@Nested
		@DisplayName("향수가 존재하고 리뷰가 존재하지 않는다면")
		class Context_when_perfume_exists_and_review_not_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getPerfumeReviews(EXISTS_PERFUME_UUID)).willReturn(List.of());
			}

			@Test
			@DisplayName("상태코드 200과 빈 목록을 응답한다.")
			void It_responds_200_and_empty_list() throws Exception {
				mvc.perform(createGetRequest("/reviews/perfumeReviews")
						.param("perfumeUuid", EXISTS_PERFUME_UUID.toString())
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString("[]")))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}

		@Nested
		@DisplayName("향수가 존재하지 않는다면")
		class Context_when_perfume_not_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getPerfumeReviews(NOT_EXISTS_PERFUME_UUID)).willThrow(new EntityNotFoundException(
					ErrorCode.PERFUME_NOT_FOUND, NOT_EXISTS_PERFUME_UUID));
			}

			@Test
			@DisplayName("상태코드 404를 응답한다.")
			void It_responds_404() throws Exception {
				mvc.perform(createGetRequest("/reviews/perfumeReviews")
						.param("perfumeUuid", NOT_EXISTS_PERFUME_UUID.toString())
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(status().isNotFound())
					.andDo(print());
			}
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("GET /api/v1/reviews/reviews/bestReviews?amountOfBestReview={amountOfBestReview}")
	class Describe_bestReviews {
		@Nested
		@DisplayName("리뷰가 존재한다면")
		class Context_when_review_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getBestReviews(3)).willReturn(List.of(SECOND_REVIEW, THIRD_REVIEW, EXISTS_REVIEW));
			}

			@Test
			@DisplayName("상태코드 200과 베스트 리뷰를 추천 순으로 응답한다.")
			void It_responds_200_and_best_reviews_by_heart_cnt() throws Exception {
				mvc.perform(createGetRequest("/reviews/bestReviews")
						.param("amountOfBestReview", "3")
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString("10")))
					.andExpect(content().string(containsString("7")))
					.andExpect(content().string(containsString("5")))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}

		@Nested
		@DisplayName("리뷰가 존재하지 않는다면")
		class Context_when_review_not_exists {
			@BeforeEach
			void setUp() {
				given(reviewService.getBestReviews(3)).willReturn(List.of());
			}

			@Test
			@DisplayName("상태코드 200과 빈 목록을 응답한다.")
			void It_responds_200_and_empty_list() throws Exception {
				mvc.perform(createGetRequest("/reviews/bestReviews")
						.param("amountOfBestReview", "3")
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
					.andExpect(content().string(containsString("[]")))
					.andExpect(status().isOk())
					.andDo(print());
			}
		}
	}
}
