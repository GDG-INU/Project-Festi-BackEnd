package com.gdg.festi.match.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Dto.request.MatchInfoEnrollRequest;
import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import com.gdg.festi.match.Enums.Status;
import com.gdg.festi.match.Repository.MatchInfoRepository;
import com.gdg.festi.user.entity.User;
import com.gdg.festi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatchServiceTest {

    @Mock
    private MatchInfoRepository matchInfoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MatchService matchService;

    private MatchInfoEnrollRequest matchInfoEnrollRequest;
    private MatchInfo matchInfo;
    private UserDetails userDetails;
    private User user;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);

        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testKakaoId");


        user = new User("testKakaoId", "testNickname", "testAccessToken");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        String matchInfoEnrollRequestJson = """
                    {
                        "groupName": "testGroup",
                        "groupInfo": "test",
                        "people": 4,
                        "matchDate": "2025-01-10",
                        "startTime": "2025-01-10T10:00:00",
                        "gender": "남자",
                        "desiredGender": "여자",
                        "drink": "1병",
                        "mood": "도란도란",
                        "contact": ["@testInstagram", "01012345678"],
                        "groupImg": "image"
                    }
                """;

        matchInfoEnrollRequest = objectMapper.readValue(matchInfoEnrollRequestJson, MatchInfoEnrollRequest.class);

//        matchInfoEnrollRequest = MatchInfoEnrollRequest.builder()
//                .groupName("testGroup")
//                .groupInfo("test")
//                .people(4)
//                .matchDate(LocalDate.now())
//                .startTime(LocalDateTime.now())
//                .gender(Gender.MALE)
//                .desiredGender(Gender.FEMALE)
//                .drink(Drink.ONE)
//                .mood(Mood.LONG)
//                .contact(List.of("test1", "test2"))
//                .groupImg("image")
//                .build();

        matchInfo = MatchInfo.builder()
                .matchInfoId(100L)
                .groupName("testGroup")
                .groupInfo("test")
                .people(4)
                .matchDate(LocalDate.now())
                .startTime(LocalDateTime.now())
                .gender(Gender.MALE)
                .desiredGender(Gender.FEMALE)
                .drink(Drink.ONE)
                .mood(Mood.LONG)
                .contact(List.of("test1", "test2"))
                .groupImg("image")
                .status(Status.WAITING)
                .build();

    }

    @Test
    void enrollMatchInfo() {
        when(userRepository.findByKakaoId(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(matchInfoRepository.existsByUserAndMatchDate(user, matchInfoEnrollRequest.getMatchDate())).thenReturn(false);
        when(matchInfoRepository.save(any(MatchInfo.class))).thenReturn(matchInfo);

        // When
        Long result = matchService.enrollMatchInfo(userDetails, matchInfoEnrollRequest);

        // Then
        assertNotNull(result);
        assertEquals(matchInfo.getMatchInfoId(), result);
        verify(matchInfoRepository, times(1)).save(any(MatchInfo.class));

    }
}