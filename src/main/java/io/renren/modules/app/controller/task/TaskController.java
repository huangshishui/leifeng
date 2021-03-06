package io.renren.modules.app.controller.task;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;
import io.renren.modules.app.service.TaskService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/app/task")
@Api(tags = "任务")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Login
    @PostMapping("/create")
    @ApiOperation("创建任务")
    public R createTask(@RequestBody TaskForm form) {
        taskService.createTask(ReqUtils.currentUserId(),form);
        return R.ok();
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取任务详细信息")
    public R getTask(@PathVariable("id") Long id) {
        TaskDto task = taskService.getTask(id);
        return R.ok().put("result", task);
    }


    @Login
    @PutMapping("/update")
    @ApiOperation("更新任务信息")
    public R updateTask(@RequestBody TaskForm form) {
        taskService.updateTask(form);
        return R.ok();
    }


    @Login
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除任务")
    public R deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return R.ok();
    }

    @Login
    @PostMapping("/receive")
    @ApiOperation("领取任务")
    public R receiveTask(@RequestBody Long taskId) {
        taskService.receiveTask(ReqUtils.currentUserId(),taskId);
        return R.ok();
    }

    @PostMapping("/receive/list")
    @ApiOperation("分页获取领取任务列表")
    public R getReceivedTasks(@RequestParam Long receiverId,@RequestParam Integer curPage,@RequestParam Long pageSize) {
        Map<String,Object> pageMap = new HashMap<>();
        pageMap.put("page",curPage);
        pageMap.put("size",pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskDto> tasks= taskService.getReceivedTasks(receiverId,page);
        return R.ok().put("result",tasks);
    }

    @PostMapping("/publish/list")
    @ApiOperation("分页获取发布任务列表")
    public R getPublishedTasks(@RequestParam Long publisherId,@RequestParam Integer curPage,@RequestParam Long pageSize) {
        Map<String,Object> pageMap = new HashMap<>();
        pageMap.put("page",curPage);
        pageMap.put("size",pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskDto> tasks= taskService.getPublishedTasks(publisherId,page);
        return R.ok().put("result",tasks);
    }

    @PostMapping("/search/list")
    @ApiOperation("分页获取发布任务列表")
    public R searchTasks(@RequestBody TaskQueryForm form) {
        Map<String,Object> pageMap = new HashMap<>();
        pageMap.put("page",form.getCurPage());
        pageMap.put("size",form.getPageSize());
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskDto> tasks= taskService.searchTasks(form,page);
        return R.ok().put("result",tasks);
    }

}
