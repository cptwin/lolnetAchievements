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
package nz.co.lolnet.lolnetachievements.Achievements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.co.lolnet.lolnetachievements.LolnetAchievements;
import nz.co.lolnet.lolnetachievements.Utility.Config;
import static nz.co.lolnet.lolnetachievements.Utility.Config.API_TIMEOUT;
import org.bukkit.Achievement;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author CptWin
 */
public class Achievements {
    
    public static void addPlayerAchievementsToCloud(Player player)
    {
        for(Achievement achievement : Achievement.values())
        {
            if(player.hasAchievement(achievement))
            {
                try {
                    awardPlayerAchievement(player.getName(), achievement.name());
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(Achievements.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void addCloudAchievementsToPlayer(Player player)
    {
        try {
            JSONArray achievements = getPlayerAchievements(player.getName());
            for (Object achievement : achievements) {
                JSONObject object = (JSONObject) achievement;
                try {
                    player.awardAchievement(Achievement.valueOf(LolnetAchievements.convertAchievementName((String) object.get("achievementname"))));
                } catch (IllegalArgumentException ex)
                {
                    
                }
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Achievements.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void awardPlayerAchievement(String playername, String achievementname) throws MalformedURLException, IOException, ParseException {
        
        JSONObject data = new JSONObject();
        data.put("serverName", Config.SERVER_NAME);
        data.put("serverKey", Config.SERVER_HASH);
        data.put("achievementName", LolnetAchievements.convertAchievementName(achievementname));
        data.put("playerName", playername.toLowerCase());
        
        if(Config.DEBUG_MODE)
        {
            System.out.println(data.toJSONString());
        }
        
        URL url = new URL("https://api-lnetachievements.rhcloud.com/api/achievements");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setConnectTimeout(API_TIMEOUT);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data.toJSONString());
        wr.flush();
        wr.close();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String temp = rd.readLine();
        if(Config.DEBUG_MODE)
        {
            System.out.println(temp);
        }
        //JSONObject object = (JSONObject) new JSONParser().parse(temp);
        //System.out.println("");
        rd.close();
    }

    private static JSONArray getPlayerAchievements(String playername) throws MalformedURLException, IOException, ParseException {
        URL url = new URL("https://api-lnetachievements.rhcloud.com/api/achievements?playerName=" + playername.toLowerCase());
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setConnectTimeout(API_TIMEOUT);

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String temp = rd.readLine();
        rd.close();
        return (JSONArray) new JSONParser().parse(temp);
    }

}
