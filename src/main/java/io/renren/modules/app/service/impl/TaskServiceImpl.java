package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.GeoUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dao.task.TaskReceiveDao;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.TaskDifficultyEnum;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {
    private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


    @Resource
    private TaskReceiveDao taskReceiveDao;

  /*  @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TaskEntity> page = this.selectPage(
                new Query<TaskEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }*/

    @Override
    public PageUtils<TaskDto> searchTasks(TaskQueryForm form, PageWrapper page) {
        Map<String, Object> queryMap = getTaskQueryMap(form);
        List<TaskDto> tasks = this.baseMapper.searchTasks(queryMap, page);
        int total = this.baseMapper.count(queryMap);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }

    private Map<String, Object> getTaskQueryMap(TaskQueryForm form) {
        Map<String, Object> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(form.getKeyword())) {
            queryMap.put("title", form.getKeyword());
        }
        if (form.getLatitude() != null && form.getLongitude() != null && form.getRaidus() != null) {
            Map<String, Double> aroundMap = GeoUtils.getAround(form.getLatitude(), form.getLongitude(), form.getRaidus());
            queryMap.putAll(aroundMap);
        }
        if (!CollectionUtils.isEmpty(form.getTagIds())) {
            queryMap.put("tagIds", form.getTagIds());
        }
        if (form.getTaskDifficulty() != null) {
            TaskDifficultyEnum difficulty = form.getTaskDifficulty();
            queryMap.put("difficulty", difficulty.name());
            switch (difficulty) {
                case FREE:
                    queryMap.put("maxVirtualCurrency", difficulty.getMaxVirtualCurrency());
                    break;
                case SIMPLE:
                    queryMap.put("minVirtualCurrency", difficulty.getMinVirtualCurrency());
                    queryMap.put("maxVirtualCurrency", difficulty.getMaxVirtualCurrency());
                    break;
                case NORMAL:
                    queryMap.put("minVirtualCurrency", difficulty.getMinVirtualCurrency());
                    queryMap.put("maxVirtualCurrency", difficulty.getMaxVirtualCurrency());
                    break;
                case DIFFICULT:
                    queryMap.put("minVirtualCurrency", difficulty.getMinVirtualCurrency());
                default:
                    break;
            }
        }
        return queryMap;
    }

    @Override
    public PageUtils<TaskDto> getPublishedTasks(Long publisherId, PageWrapper page) {
        List<TaskDto> tasks = this.baseMapper.getPublishedTasks(publisherId, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        int total = this.baseMapper.publishCount(publisherId);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public PageUtils<TaskDto> getReceivedTasks(Long receiverId, PageWrapper page) {
        List<TaskDto> tasks = this.baseMapper.getReceivedTasks(receiverId, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        int total = this.baseMapper.receiveCount(receiverId);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public TaskDto getTask(Long id) {
        TaskDto task = this.baseMapper.getTask(id);
        return task;
    }

    @Override
    @Transactional
    public void createTask(Long creatorId, TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
        task.setCreatorId(creatorId);
        task.setCreateTime(DateUtils.now());
        this.insert(task);
        this.addTaskImageRelation(task.getId(), form.getImageUrls());
        this.addTaskTagRelation(task.getId(), form.getTagIds());
        this.addTaskNotifiedUserRelation(task.getId(), form.getNotifiedUserIds());

    }


    @Override
    public void updateTask(TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
        this.updateById(task);
    }

    @Override
    public void deleteTask(Long id) {
       /* Wrapper<TaskEntity> wrapper = new EntityWrapper<>();
        wrapper.in("id", Arrays.asList(ids));
        List<TaskEntity> tasks = this.selectList(wrapper);
        if (!CollectionUtils.isEmpty(tasks)) {
            for (TaskEntity task : tasks) {
                task.setDeleted(true);
            }
            this.updateBatchById(tasks);
        }*/
        TaskEntity task = this.selectById(id);
        if (task != null) {
            task.setDeleted(true);
            this.updateById(task);
        }
    }

    @Override
    @Transactional
    public void receiveTask(Long receiverId, Long taskId) {
        TaskReceiveEntity receive = new TaskReceiveEntity(DateUtils.now(), receiverId, taskId);
        TaskEntity task = this.selectById(taskId);
        if (task == null
                || task.getStatus() != 0//非发布状态
                || task.getDeleted()) {
            throw new RRException("任务已被领取");
        }
        task.setStatus(1);//设置为领取状态
        this.updateById(task);
        taskReceiveDao.insert(receive);
    }
/*

    @Override
    @Transactional
    public void completeTask(Long receiverId, Long taskId) {
        TaskReceiveEntity receive = new TaskReceiveEntity(DateUtils.now(), receiverId, taskId);
        TaskEntity task = this.selectById(taskId);
        if (task == null
                || task.getStatus() != 1//非领取状态
                || task.getDeleted()) {
            throw new RRException("任务已被领取");
        }
        task.setStatus(1);//设置为领取状态
        this.updateById(task);
        taskReceiveDao.insert(receive);
    }
*/


    //任务-图片关系
    private void addTaskImageRelation(Long taskId, List<String> imageUrls) {
        if (!CollectionUtils.isEmpty(imageUrls)) {
            this.baseMapper.insertTaskImageRelation(taskId, imageUrls);
        }
    }

    //任务-标签关系

    private void addTaskTagRelation(Long taskId, List<Long> tagIds) {
        if (!CollectionUtils.isEmpty(tagIds)) {
            this.baseMapper.insertTaskTagRelation(taskId, tagIds);
        }
    }


    //任务-提示用户关系
    private void addTaskNotifiedUserRelation(Long taskId, List<Long> userIds) {
        if (!CollectionUtils.isEmpty(userIds)) {
            this.baseMapper.insertTaskNotifiedUserRelation(taskId, userIds);
        }
    }

}