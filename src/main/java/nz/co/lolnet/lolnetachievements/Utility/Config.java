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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import nz.co.lolnet.lolnetachievements.LolnetAchievements;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    private static final int BCRYPT_NUMBER_OF_ROUNDS = 10;
    private static JSONArray ACHIEVEMENT_CONVERSION_LIST;

    public static final int API_TIMEOUT = 10000;
    public static final boolean DEBUG_MODE = false;

    public static void initConfig() {
        LolnetAchievements.plugin.saveDefaultConfig();
        LolnetAchievements.plugin.getConfig().options().copyDefaults(true);
        LolnetAchievements.plugin.saveConfig();
        FileConfiguration configFile = LolnetAchievements.plugin.getConfig();
        SERVER_NAME = configFile.getString(SERVER_NAME_CONFIG, SERVER_NAME_DEFAULT);
        SERVER_HASH = configFile.getString(SERVER_HASH_CONFIG, SERVER_HASH_DEFAULT);
        if (SERVER_NAME.equals(SERVER_NAME_DEFAULT)) {
            LolnetAchievements.logger.log(Level.SEVERE, "Please change the server name in the lolnet Achievements config.yml!");
            LolnetAchievements.plugin.onDisable();
        } else if (!SERVER_HASH.startsWith("$2a")) {
            SERVER_HASH = BCrypt.hashpw(SERVER_NAME, BCrypt.gensalt(BCRYPT_NUMBER_OF_ROUNDS));
            configFile.set(SERVER_HASH_CONFIG, SERVER_HASH);
            LolnetAchievements.plugin.saveConfig();
            configFile = LolnetAchievements.plugin.getConfig();
        }
        if (!retrieveAchievementConversionList()) {
            LolnetAchievements.logger.log(Level.SEVERE, "Couldn't get the achievement conversion list from the API Server");
            LolnetAchievements.plugin.onDisable();
        }
    }

    private static boolean retrieveAchievementConversionList() {
        boolean output = false;
        try {
            URL url = new URL("https://api-lnetachievements.rhcloud.com/api/achievementconversionlist");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(API_TIMEOUT);

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            ACHIEVEMENT_CONVERSION_LIST = (JSONArray) new JSONParser().parse(rd.readLine());
            rd.close();

            output = true;

        } catch (IOException | ParseException ex) {
            output = false;
        }
        return output;
    }
    
    public static JSONArray getAchievementConversionList()
    {
        return ACHIEVEMENT_CONVERSION_LIST;
    }

}
