package com.example.demoserver.event.events;

import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.task.model.Task;
import com.example.demoserver.game.task.model.TaskProgress;

public class TaskFinishEvent {

    Player player;

    TaskProgress taskProgress;

}
