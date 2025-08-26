package com.groupstudy.domain.teammember.dto.response;

import java.util.List;

public record SingleStudyRoomResponse(
        String studyroomNm,
        Integer userCount,
        List<String> studyTopic
) {
}
