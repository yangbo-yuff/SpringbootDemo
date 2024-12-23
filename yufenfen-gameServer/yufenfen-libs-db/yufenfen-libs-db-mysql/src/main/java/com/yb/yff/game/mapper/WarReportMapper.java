package com.yb.yff.game.mapper;

import com.yb.yff.game.data.entity.WarReportEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 战报表 Mapper 接口
 * </p>
 *
 * @author yangbo
 * @since 2024-11-01
 */
public interface WarReportMapper extends BaseMapper<WarReportEntity> {
	/**
	 * 获取最近几条未读的战报
	 * @param rid
	 * @param lastNum
	 * @return
	 */
	@Select("SELECT * FROM tb_war_report " +
			"WHERE (a_rid = #{rid} AND  a_is_read = 0 ) OR (d_rid = #{rid} AND d_is_read = 0 ) " +
			"ORDER BY ctime DESC " +
			"LIMIT #{lastNum}")
	List<WarReportEntity> selectWarReportUR(@Param("rid") Integer rid, @Param("lastNum") Integer lastNum);

	/**
	 * 阅读战报
	 * @param rid
	 * @param reportId
	 * @return
	 */
	@Update("UPDATE tb_war_report " +
			"SET " +
			"a_is_read = CASE WHEN a_rid = #{rid} THEN 1 ELSE a_is_read END, " +
			"d_is_read = CASE WHEN d_rid = #{rid} THEN 1 ELSE d_is_read END " +
			"WHERE id = #{reportId}")
	int readWarReport(@Param("rid") Integer rid, @Param("reportId") Integer reportId);

	/**
	 * 阅读全部战报
	 * @param rid
	 * @return
	 */
	@Update("UPDATE tb_war_report " +
			"SET " +
			"a_is_read = CASE WHEN a_rid = #{rid} THEN 1 ELSE a_is_read END, " +
			"d_is_read = CASE WHEN d_rid = #{rid} THEN 1 ELSE d_is_read END")
	int readWarReportAll(@Param("rid") Integer rid);
}
