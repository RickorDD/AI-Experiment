package de.reikodd.ddweki;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    public static int getNumberChallenges(String content) {

        try {
            JSONObject jsObjChallenges = new JSONObject(content);
            JSONArray jsArrChallenges = jsObjChallenges
                    .getJSONArray("challenges");
            return jsArrChallenges.length();
        } catch (JSONException je) {
            return 0;
        }
    }

    public static String getNameChallenge(String content, int challenge) {

        try {
            JSONObject jsObjChallenges = new JSONObject(content);
            JSONArray jsArrChallenges = jsObjChallenges
                    .getJSONArray("challenges");
            return jsArrChallenges.getJSONObject(challenge).getString("name");
        } catch (JSONException je) {
            return null;
        }
    }

    public static String getDetailsChallenge(String content, int challenge) {

        try {
            JSONObject jsObjChallenges = new JSONObject(content);
            JSONArray jsArrChallenges = jsObjChallenges
                    .getJSONArray("challenges");
            return jsArrChallenges.getJSONObject(challenge).getString("details");
        } catch (JSONException je) {
            return null;
        }
    }


    public static int getNumberDescSet(String content, int challenge)
    {
        try {
            JSONObject jsObjChallenges = new JSONObject(content);
            JSONArray jsArrChallenges = jsObjChallenges.getJSONArray("challenges");

            JSONObject jsObjChallenge = jsArrChallenges.getJSONObject(challenge);
            JSONArray jsArrChallenge = jsObjChallenge.getJSONArray("description_set");

            return jsArrChallenge.length();
        } catch (JSONException je) {
            return 0;
        }

    }

    public static String getCharacterDescSet(String content, int challenge, int character)
    {
        try {
            JSONObject jsObjChallenges = new JSONObject(content);
            JSONArray jsArrChallenges = jsObjChallenges.getJSONArray("challenges");

            JSONObject jsObjChallenge = jsArrChallenges.getJSONObject(challenge);
            JSONArray jsArrChallenge = jsObjChallenge.getJSONArray("description_set");

            return jsArrChallenge.getString(character);
        } catch (JSONException je) {
            return null;
        }

    }
}