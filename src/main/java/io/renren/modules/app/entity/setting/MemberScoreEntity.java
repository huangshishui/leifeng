package io.renren.modules.app.entity.setting;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 用户评分表
 */
@TableName("t_member_score")
public class MemberScoreEntity extends BaseEntity {

    /**
     * 评分人id
     */
    private Long judgeId;
    /**
     * 被评分用户id
     */
    private Long memberId;

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 分数
     */
    private Integer score;

    /**
     * 奖状id
     */
    private Long certificateId;

    /**
     * 鲜花数
     */
    private Integer flowerCount;

    /**
     * 评论内容
     */
    private String content;

    public MemberScoreEntity() {
    }

    public Long getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(Long judgeId) {
        this.judgeId = judgeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Long certificateId) {
        this.certificateId = certificateId;
    }

    public Integer getFlowerCount() {
        return flowerCount;
    }

    public void setFlowerCount(Integer flowerCount) {
        this.flowerCount = flowerCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
