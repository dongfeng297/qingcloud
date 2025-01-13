package qingcloud.service;

import qingcloud.dto.Result;

public interface RankingService {
    Result getCourseRanking();

    Result getTeacherRanking();
}
