package io.renren.modules.app.dto;

import io.renren.modules.app.entity.setting.Member;

import java.io.Serializable;

/**
 * 任务圈返回信息
 */
public class TaskCircleDto implements Serializable {

    /**
     * 创建人
     */
    private Member creator;

    /**
     * 圈名称
     */
    private String name;

    /**
     * 圈头像
     */
    private String avatar;
    /**
     * 圈描述
     */
    private String description;

    /**
     * 是否需要审核
     */
    private Boolean needReview;

    /**
     * 任务数
     */
    private Integer memberCount;

    /**
     * 任务数
     */
    private Integer taskCount;

    /**
     * 创建时间
     */
    private Long createTime;


    public Member getCreator() {
        return creator;
    }

    public void setCreator(Member creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getNeedReview() {
        return needReview;
    }

    public void setNeedReview(Boolean needReview) {
        this.needReview = needReview;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
