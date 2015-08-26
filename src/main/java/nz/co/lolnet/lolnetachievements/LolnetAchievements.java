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
package nz.co.lolnet.lolnetachievements;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.co.lolnet.lolnetachievements.Utility.Config;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

/**
 *
 * @author CptWin
 */
public class LolnetAchievements extends JavaPlugin {
    
    public static final Logger logger = Logger.getLogger("Minecraft");
    public static LolnetAchievements plugin;
    
    private static ExecutorService threadPool;
    
    @Override
    public void onEnable() {
        plugin = this;
        threadPool = Executors.newFixedThreadPool(4);
        Config.initConfig();
        getServer().getPluginManager().registerEvents(new nz.co.lolnet.lolnetachievements.Listeners.EventListener(), this);
        PluginDescriptionFile pdfFile = this.getDescription();
        logger.log(Level.INFO, "{0} Version {1} Has Been Enabled!", new Object[]{pdfFile.getName(), pdfFile.getVersion()});
    }
    
    @Override
    public void onDisable() {
        threadPool.shutdown();
        PluginDescriptionFile pdfFile = this.getDescription();
        logger.log(Level.INFO, "{0} Version {1} Has Been Disabled!", new Object[]{pdfFile.getName(), pdfFile.getVersion()});
    }
    
    public static ExecutorService getThreadPool()
    {
        return threadPool;
    }
    
    public static String convertAchievementName(String input)
    {
        if(input.contains("_"))
        {
            for(int i = 0; i < Config.getAchievementConversionList().size(); i++)
            {
                JSONObject object = (JSONObject) Config.getAchievementConversionList().get(i);
                if(input.equals(object.get("pluginName")))
                {
                    return object.get("storedName").toString();
                }
            }
        }
        else
        {
            for(int i = 0; i < Config.getAchievementConversionList().size(); i++)
            {
                JSONObject object = (JSONObject) Config.getAchievementConversionList().get(i);
                if(input.equals(object.get("storedName")))
                {
                    return object.get("pluginName").toString();
                }
            }
        }
        return input;
    }
    
}
