package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.form.PageWrapper;

import java.util.List;
import java.util.Map;

/**
 * 任务标签
 */
public interface TaskTagService extends IService<TaskTagEntity> {


    /**
     * 获取所有任务标签列表
     */
    List<TaskTagEntity> getTaskTags();

    /**
     * 创建任务标签
     */
    void createTaskTag(String tagName);

    /**
     * 更新任务标签
     */
    void updateTaskTag(Long tagId, String tagName);

    /**
     * 删除任务标签
     */
    void deleteTaskTag(Long tagId);



    PageUtils<TaskTagEntity> getTasks( Map<String,Object> pageMap);
}

