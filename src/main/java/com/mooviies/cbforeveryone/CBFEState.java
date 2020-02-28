/*
Command Blocks For Everyone. Minecraft Mod.
Copyright (C) 2020 mooviies

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
