package com.mooviies.cbforeveryone;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.UUID;

public class CBFEState {

    private CBFEState() {}

    private static boolean _globalPrivilege = true;
    private static HashSet<UUID> _whitelist = new HashSet<>();
    private static HashSet<UUID> _blacklist = new HashSet<>();

    public static void setGlobalPrivilege(boolean globalPrivilege)
    {
        _globalPrivilege = globalPrivilege;
    }

    public static void setPrivilege(UUID uuid, boolean allow)
    {
        if(allow)
        {
            _blacklist.remove(uuid);
            _whitelist.add(uuid);
        }
        else
        {
            _blacklist.add(uuid);
            _whitelist.remove(uuid);
        }
    }

    public static void clearPrivilege(UUID uuid)
    {
        _blacklist.remove(uuid);
        _whitelist.remove(uuid);
    }

    public static boolean canUseCommandblock(PlayerEntity playerEntity)
    {
        if(playerEntity.canUseCommandBlock())
            return true;

        UUID uuid = playerEntity.getUniqueID();
        if(_blacklist.contains(uuid))
            return false;

        return _whitelist.contains(uuid) || _globalPrivilege;
    }
}
