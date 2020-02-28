/*
Command Blocks For Everyone. Minecraft Mod.
Copyright (C) 2020 mooviies
https://github.com/mooviies/CommandBlocksForEveryone

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/.
 */
package com.mooviies.cbforeveryone;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mooviies.cbforeveryone.interfaces.ICBFEPermissionService;
import net.minecraft.entity.player.PlayerEntity;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public final class CBFEPermissionService implements ICBFEPermissionService {
    private static final String PROPERTY_GLOBAL = "global";
    private static final String PROPERTY_DEFAULT = "default";
    private static final String PROPERTY_WHITELIST = "allow";
    private static final String PROPERTY_BLACKLIST = "deny";

    private static final String SAVE_FILE = "config/commandblocksforeveryone.json";

    private static CBFEPermissionService _globalPermService = new CBFEPermissionService(PROPERTY_GLOBAL);
    private static HashMap<Integer, CBFEPermissionService> _permServiceByDim = new HashMap<>();

    public final String dimension;
    private boolean _useGlobalPermission = true;
    private boolean _worldPermission = true;
    private HashSet<String> _allowList = new HashSet<>();
    private HashSet<String> _denyList = new HashSet<>();

    private CBFEPermissionService(String dimension) {
        this.dimension = dimension;
    }

    public static void copyPermission(CBFEPermissionService dest, CBFEPermissionService source)
    {
        dest._useGlobalPermission = source._useGlobalPermission;
        dest._worldPermission = source._worldPermission;
        dest._allowList.clear();
        dest._denyList.clear();

        dest._allowList.addAll(source._allowList);
        dest._denyList.addAll(source._denyList);
    }

    public static CBFEPermissionService get(int dimensionID)
    {
        if(!_permServiceByDim.containsKey(dimensionID))
            _permServiceByDim.put(dimensionID, new CBFEPermissionService(Integer.toString(dimensionID)));

        return _permServiceByDim.get(dimensionID);
    }

    public static CBFEPermissionService getGlobal()
    {
        return _globalPermService;
    }

    public boolean getWorldPermission()
    {
        if(this != _globalPermService && _useGlobalPermission)
            return _globalPermService.getWorldPermission();

        return _worldPermission;
    }

    public boolean getWorldPermission(PlayerEntity playerEntity)
    {
        return getWorldPermission(playerEntity.getName().getString());
    }

    public boolean getWorldPermission(String username)
    {
        if(_denyList.contains(username))
            return false;

        if(_allowList.contains(username))
            return true;

        if(this != _globalPermService && _useGlobalPermission)
            return _globalPermService.getWorldPermission(username);

        return getWorldPermission();
    }

    public void setWorldPermission(boolean allow)
    {
        _useGlobalPermission = false;
        _worldPermission = allow;
        save();
    }

    public void setWorldPermission(PlayerEntity playerEntity, boolean allow)
    {
        setWorldPermission(playerEntity.getName().getString(), allow);
    }

    public void setWorldPermission(String username, boolean allow)
    {
        if(allow)
        {
            _denyList.remove(username);
            _allowList.add(username);
        }
        else
        {
            _denyList.add(username);
            _allowList.remove(username);
        }

        save();
    }

    public void resetWorldPermission()
    {
        if(this == _globalPermService)
            _worldPermission = true;

        _useGlobalPermission = true;
        save();
    }

    public void resetWorldPermission(PlayerEntity playerEntity)
    {
        resetWorldPermission(playerEntity.getName().getString());
    }

    public void resetWorldPermission(String username)
    {
        _denyList.remove(username);
        _allowList.remove(username);
        save();
    }

    public static void load()
    {
        File saveFile = new File(SAVE_FILE);
        if(!saveFile.exists())
        {
            save();
            return;
        }

        String jsonContent = "";
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(saveFile));
            jsonContent = in.lines().collect(Collectors.joining("\n"));
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jo = jsonParser.parse(jsonContent).getAsJsonObject();

        fromJson(_globalPermService, jo.getAsJsonObject(PROPERTY_GLOBAL));
        _permServiceByDim.clear();
        for(Map.Entry<String,JsonElement> entry : jo.entrySet())
        {
            if(entry.getKey().equals(PROPERTY_GLOBAL))
                continue;

            Integer dimension = Integer.parseInt(entry.getKey());
            _permServiceByDim.put(dimension, new CBFEPermissionService(dimension.toString()));
            fromJson(_permServiceByDim.get(dimension), entry.getValue().getAsJsonObject());
        }
    }

    public static void save()
    {
        JsonObject jo = new JsonObject();
        jo.add(PROPERTY_GLOBAL, toJson(_globalPermService));
        for(Integer dimension : _permServiceByDim.keySet())
        {
            jo.add(dimension.toString(), toJson(_permServiceByDim.get(dimension)));
        }

        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(SAVE_FILE));
            out.write(jo.toString());
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static JsonObject toJson(CBFEPermissionService permService)
    {
        JsonObject jo = new JsonObject();

        if((permService != _globalPermService) && permService._useGlobalPermission)
            jo.addProperty(PROPERTY_DEFAULT, PROPERTY_GLOBAL);
        else
            jo.addProperty(PROPERTY_DEFAULT, permService._worldPermission ? "allow" : "deny");

        JsonArray jaWhitelist = new JsonArray(), jaBlacklist = new JsonArray();
        for(String username : permService._allowList)
            jaWhitelist.add(username);

        for(String username : permService._denyList)
            jaBlacklist.add(username);

        jo.add(PROPERTY_WHITELIST, jaWhitelist);
        jo.add(PROPERTY_BLACKLIST, jaBlacklist);

        return jo;
    }

    private static void fromJson(CBFEPermissionService permService, JsonObject jo)
    {
        if(jo.isJsonNull())
            return;

        JsonElement defaultElement = jo.get(PROPERTY_DEFAULT);
        JsonElement allowListElement = jo.get(PROPERTY_WHITELIST);
        JsonElement denyListElement = jo.get(PROPERTY_BLACKLIST);

        permService._useGlobalPermission = true;
        permService._worldPermission = true;
        if(!defaultElement.isJsonNull())
        {
            if(permService != _globalPermService)
            {
                String worldPermission = defaultElement.getAsString();
                if(!worldPermission.equals(PROPERTY_GLOBAL))
                {
                    permService._useGlobalPermission = false;
                    permService._worldPermission = defaultElement.getAsString().equals("allow");
                }
            }
            else
                permService._worldPermission = defaultElement.getAsString().equals("allow");
        }

        permService._allowList.clear();
        if(allowListElement.isJsonArray())
        {
            JsonArray array = allowListElement.getAsJsonArray();
            for(JsonElement je : array)
            {
                if(!je.isJsonNull())
                    permService._allowList.add(je.getAsString());
            }
        }

        permService._denyList.clear();
        if(denyListElement.isJsonArray())
        {
            JsonArray array = denyListElement.getAsJsonArray();
            for(JsonElement je : array)
            {
                if(!je.isJsonNull())
                    permService._denyList.add(je.getAsString());
            }
        }
    }
}
