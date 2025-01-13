package qingcloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import qingcloud.entity.TeacherRanking;

import java.util.List;

@Mapper
public interface TeacherRankingMapper {
    void insertBatch(List<TeacherRanking> rankingList);
}
