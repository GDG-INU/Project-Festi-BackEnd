package com.gdg.festi.match.Config;

import com.gdg.festi.match.Repository.MatchInfoRepository;
import com.gdg.festi.match.Repository.MatchResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DatabaseInit {

    private final MatchInfoRepository matchInfoRepository;
    private final MatchResultRepository matchResultRepository;

    public void init() {
        matchInfoRepository.deleteAll();
        matchResultRepository.deleteAll();
    }
}
