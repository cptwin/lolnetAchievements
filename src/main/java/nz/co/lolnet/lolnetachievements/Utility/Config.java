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
package nz.co.lolnet.lolnetachievements.Utility;

import java.util.logging.Level;
import nz.co.lolnet.lolnetachievements.LolnetAchievements;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author CptWin
 */
public class Config {
    
    public static String SERVER_NAME;
    public static String SERVER_HASH;
    
    public static final String SERVER_NAME_CONFIG = "ServerName";
    public static final String SERVER_HASH_CONFIG = "ServerHash";
    
    public static final String SERVER_NAME_DEFAULT = "default";
    public static final String SERVER_HASH_DEFAULT = "123456";
    public static final int BCRYPT_NUMBER_OF_ROUNDS = 10;
    
    public static void initConfig()
    {
        FileConfiguration configFile = LolnetAchievements.plugin.getConfig();
        SERVER_NAME = configFile.getString(SERVER_NAME_CONFIG, SERVER_NAME_DEFAULT);
        SERVER_HASH = configFile.getString(SERVER_HASH_CONFIG, SERVER_HASH_DEFAULT);
        if(SERVER_NAME.equals(SERVER_NAME_DEFAULT))
        {
            LolnetAchievements.logger.log(Level.SEVERE, "Please change the server name in the lolnet Achievements config.yml!");
            LolnetAchievements.plugin.onDisable();
        }
        if(!SERVER_HASH.startsWith("$2a"))
        {
            SERVER_HASH = BCrypt.hashpw(SERVER_NAME, BCrypt.gensalt(BCRYPT_NUMBER_OF_ROUNDS));
            configFile.set(SERVER_HASH_CONFIG, SERVER_HASH);
            LolnetAchievements.plugin.saveConfig();
            configFile = LolnetAchievements.plugin.getConfig();
        }
    }
    
}
