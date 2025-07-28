package com.groupstudy.domain.roomuser.dto.response;

import java.util.List;

public record SingleStudyRoomResponse(
        String studyroomNm,
        Integer userCount,
        List<String> studyTopic
) {
}
