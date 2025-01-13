package qingcloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import qingcloud.entity.CourseRanking;

import java.util.List;

@Mapper
public interface CourseRankingMapper {

    void insertBatch(List<CourseRanking> rankingList);
}
