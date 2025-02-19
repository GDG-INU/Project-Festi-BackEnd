package com.gdg.festi.Match.Dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchInfoListRequest {

    private List<MatchInfoResponse> matchInfoEnrollResponseList;

}
