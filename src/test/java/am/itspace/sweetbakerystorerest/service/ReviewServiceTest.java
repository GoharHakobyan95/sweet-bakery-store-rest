package am.itspace.sweetbakerystorerest.service;

import am.itspace.sweetbakerystorecommon.entity.*;
import am.itspace.sweetbakerystorecommon.repository.ReviewRepository;
import am.itspace.sweetbakerystorecommon.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1)
                .name("John")
                .surname("Smith")
                .role(Role.USER)
                .createAt(new Date())
                .isActive(true)
                .email("John@mail.ru")
                .password("john888")
                .phone("77777777")
                .verifyToken("token")
                .address(new Address(5, "165 apt.", new City(1, "Yerevan")))
                .build();
    }

    @Test
    public void findPaginatedTest() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Review> page = Mockito.mock(Page.class);
        Mockito.when(reviewRepository.findAll(pageable)).thenReturn(page);

        Page<Review> actualPage = reviewService.findPaginated(pageable);
        assertEquals(page, actualPage);
    }

    @Test
    public void findAllTest() {
        List<Review> reviews = Mockito.mock(List.class);
        Mockito.when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> actualList = reviewService.findAll();
        assertEquals(reviews, actualList);
    }

    @Test
    public void saveTest() {
        Review review = new Review();

        review.setText("text");
        review.setId(5);
        review.setDate(new Date());
        review.setUser(user);

        Mockito.when(reviewRepository.save(review)).thenReturn(review);
        Review actual = reviewService.save(review, user);
        assertEquals(review, actual);
    }

}


