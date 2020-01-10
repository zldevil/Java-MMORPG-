package com.example.demoserver.game.task.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Progress {


    private FinishCondition condition;

    //进度使用数量来代表。
    private AtomicInteger progressNum = new AtomicInteger(0);

    public volatile boolean finished = false;


    public Progress(FinishCondition condition) {
        this.condition = condition;
    }

    public Progress() {
    }

    //例如杀怪，每杀一次增加一个，判断
    public void addProgressNum(Integer count) {
        if(progressNum.addAndGet(count) >= condition.getGoal()) {
            progressNum.set(condition.getGoal());
            finished = true;
        }
    }

    public void setProgressNumber(Integer count) {
        if(count >= condition.getGoal()) {
            progressNum.set(condition.getGoal());
            finished = true;
        } else {
            progressNum.set(count);
        }

    }
}
