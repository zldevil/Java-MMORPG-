package com.example.demoserver.game.task.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskReward {

    private List<RewardItem> itemList = new ArrayList<>();
    private List<Integer> nextTask = new ArrayList<>();
    private Integer exp;

    @Data
    public static class RewardItem {
        private Integer itemInfoId;
        private Integer num;
    }

}
