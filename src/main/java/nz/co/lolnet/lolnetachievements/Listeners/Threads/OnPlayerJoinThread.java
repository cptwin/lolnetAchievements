/*
 * Copyright 2015 lolnet.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nz.co.lolnet.lolnetachievements.Listeners.Threads;

import static nz.co.lolnet.lolnetachievements.Achievements.Achievements.addCloudAchievementsToPlayer;
import static nz.co.lolnet.lolnetachievements.Achievements.Achievements.addPlayerAchievementsToCloud;
import nz.co.lolnet.lolnetachievements.LolnetAchievements;
import org.bukkit.Achievement;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author CptWin
 */
public class OnPlayerJoinThread implements Runnable {
    
    private final PlayerJoinEvent event;
    
    public OnPlayerJoinThread(PlayerJoinEvent event)
    {
        this.event = event;
        start();
    }
    
    private void start()
    {
        LolnetAchievements.getThreadPool().execute(this);
    }

    @Override
    public void run() {
        addCloudAchievementsToPlayer(event.getPlayer());
        addPlayerAchievementsToCloud(event.getPlayer());
    }
    
}
