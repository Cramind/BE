package com.groupstudy.domain.roomuser.dto.response;

import java.util.List;

public record StudyRoomListResponse (
        List<SingleStudyRoomResponse> studyRoomResponseList
){
}
