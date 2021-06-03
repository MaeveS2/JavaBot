package com.javadiscord.javabot;

import com.javadiscord.javabot.commands.other.Version;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StatusUpdate extends ListenerAdapter{

    private int currentIndex = 0;
    private ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onReady(ReadyEvent event) {

        threadPool.scheduleWithFixedDelay(() -> {

            Activity[] activities = {Activity.watching("javadiscord.net" + " | " + new Version().getVersion()),
                    Activity.listening(event.getJDA().getGuilds().get(0).getMemberCount() + " members" + " | " + new Version().getVersion()),
                    Activity.watching("!help" + " | " + new Version().getVersion())};

            event.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            event.getJDA().getPresence().setActivity(activities[currentIndex]);
            currentIndex = (currentIndex + 1) % activities.length;
        }, 0, 35, TimeUnit.SECONDS);
    }
}
