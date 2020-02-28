package com.mooviies.cbforeveryone.interfaces;

import net.minecraft.entity.player.PlayerEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public interface ICBFEPermissionService {
    boolean getWorldPermission();
    boolean getWorldPermission(PlayerEntity playerEntity);
    boolean getWorldPermission(String username);
    void setWorldPermission(boolean globalPrivilege);
    void setWorldPermission(PlayerEntity playerEntity, boolean allow);
    void setWorldPermission(String username, boolean allow);
    void resetWorldPermission();
    void resetWorldPermission(PlayerEntity playerEntity);
    void resetWorldPermission(String username);
}
