package com.pbteach.order.dao;

import com.pbteach.framework.domain.task.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-20 11:00
 **/
public interface XcTaskRepository extends JpaRepository<XcTask,String> {

    //通过乐观锁校验任务是否可以获取到
    @Modifying
    @Query("update XcTask t set t.version = :version+1 where t.id = :taskId and t.version =:version")
    int updateTaskVersion(@Param("taskId")String taskId,@Param("version")Integer version );

    //查询任务列表，一次查询n条任务，查询某个时间点之前的任务
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable,Date updateTime);

}
