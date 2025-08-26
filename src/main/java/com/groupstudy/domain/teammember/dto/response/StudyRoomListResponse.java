package com.groupstudy.domain.teammember.dto.response;

import java.util.List;

public record StudyRoomListResponse (
        List<SingleStudyRoomResponse> studyRoomResponseList
){
}
