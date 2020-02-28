package com.mooviies.cbforeveryone;

import com.mooviies.cbforeveryone.interfaces.ICBFEPermissionService;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class CBFEAPI {

    /**
     * Return the command block permission value for the server (global).
     * @return The global permission for the server (true : allow, false : deny)
     */
    public static boolean getPermission()
    {
        return CBFEPermissionService.getGlobal().getWorldPermission();
    }

    /**
     * Return the command block permission value for the world. If the world permission wasn't set, will return
     * the permission for the server (global).
     * @param dimensionID The world's dimension
     * @return The world permission (true : allow, false : deny)
     */
    public static boolean getPermission(int dimensionID)
    {
        return CBFEPermissionService.get(dimensionID).getWorldPermission();
    }

    /**
     * Return the command block permission value for the world. If the world permission wasn't set, will return
     * the permission for the server (global).
     * @param world The world
     * @return The world permission (true : allow, false : deny)
     */
    public static boolean getPermission(World world)
    {
        return CBFEPermissionService.get(world.getDimension().getType().getId()).getWorldPermission();
    }

    /**
     * Return the permission for the given player in the world where they currently are.
     * @param playerEntity The player
     * @return The player permission in the current world (true : allow, false : deny)
     */
    public static boolean getPermission(PlayerEntity playerEntity)
    {
        return CBFEPermissionService.get(playerEntity.world.dimension.getType().getId()).getWorldPermission(playerEntity);
    }

    /**
     * Return the permission for the given player in the given world.
     * @param playerEntity The player
     * @param dimensionID The world's dimension
     * @return The player permission in the given world (true : allow, false : deny)
     */
    public static boolean getPermission(PlayerEntity playerEntity, int dimensionID)
    {
        return CBFEPermissionService.get(dimensionID).getWorldPermission(playerEntity);
    }

    /**
     * Return the permission for the given player in the given world.
     * @param username The username of the player
     * @param dimensionID The world's dimension
     * @return The player permission in the given world (true : allow, false : deny)
     */
    public static boolean getPermission(String username, int dimensionID)
    {
        return CBFEPermissionService.get(dimensionID).getWorldPermission(username);
    }

    /**
     * Return the permission for the given player in the given world.
     * @param playerEntity The player
     * @param world The world
     * @return The player permission in the given world (true : allow, false : deny)
     */
    public static boolean getPermission(PlayerEntity playerEntity, World world)
    {
        return CBFEPermissionService.get(world.getDimension().getType().getId()).getWorldPermission(playerEntity);
    }

    /**
     * Return the permission for the given player in the given world.
     * @param username The username of the player
     * @param world The world
     * @return The player permission in the given world (true : allow, false : deny)
     */
    public static boolean getPermission(String username, World world)
    {
        return CBFEPermissionService.get(world.getDimension().getType().getId()).getWorldPermission(username);
    }

    /**
     * Set the server (global) permission.
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(boolean allow)
    {
        CBFEPermissionService.getGlobal().setWorldPermission(allow);
    }

    /**
     * Set the given world permission.
     * @param dimensionID The world's dimension
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(int dimensionID, boolean allow)
    {
        CBFEPermissionService.get(dimensionID).setWorldPermission(allow);
    }

    /**
     * Set the given world permission.
     * @param world The world
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(World world, boolean allow)
    {
        CBFEPermissionService.get(world.getDimension().getType().getId()).setWorldPermission(allow);
    }

    /**
     * Set the given player permission in the world they are in currently.
     * @param playerEntity The player
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(PlayerEntity playerEntity, boolean allow)
    {
        CBFEPermissionService.get(playerEntity.world.getDimension().getType().getId()).setWorldPermission(playerEntity, allow);
    }

    /**
     * Set the given player permission in the given world.
     * @param playerEntity The player
     * @param dimensionID The world's dimension
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(PlayerEntity playerEntity, int dimensionID, boolean allow)
    {
        CBFEPermissionService.get(dimensionID).setWorldPermission(playerEntity, allow);
    }

    /**
     * Set the given player permission in the given world.
     * @param username The username of the player
     * @param dimensionID The world's dimension
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(String username, int dimensionID, boolean allow)
    {
        CBFEPermissionService.get(dimensionID).setWorldPermission(username, allow);
    }

    /**
     * Set the given player permission in the given world.
     * @param playerEntity The player
     * @param world The world
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(PlayerEntity playerEntity, World world, boolean allow)
    {
        CBFEPermissionService.get(world.getDimension().getType().getId()).setWorldPermission(playerEntity, allow);
    }

    /**
     * Set the given player permission in the given world.
     * @param username The username of the player
     * @param world The world
     * @param allow (true : allow, false : deny)
     */
    public static void setPermission(String username, World world, boolean allow)
    {
        CBFEPermissionService.get(world.getDimension().getType().getId()).setWorldPermission(username, allow);
    }

    /**
     * Reset the permission for the server (global) to the default value (allow)
     */
    public static void resetPermission()
    {
        CBFEPermissionService.getGlobal().resetWorldPermission();
    }

    /**
     * Reset the given world permission to the default value (Use the server (global) permission)
     * @param dimensionID The world's dimension
     */
    public static void resetPermission(int dimensionID)
    {
        CBFEPermissionService.get(dimensionID).resetWorldPermission();
    }

    /**
     * Reset the given world permission to the default value (Use the server (global) permission)
     * @param world The world
     */
    public static void resetPermission(World world)
    {
        CBFEPermissionService.get(world.getDimension().getType().getId()).resetWorldPermission();
    }

    /**
     * Reset the given player permission in the world they are in currently to the default value (Use the world permission)
     * @param playerEntity The player
     */
    public static void resetPermission(PlayerEntity playerEntity)
    {
        CBFEPermissionService.get(playerEntity.world.getDimension().getType().getId()).resetWorldPermission(playerEntity);
    }

    /**
     * Reset the given player permission in the given world to the default value (Use the world permission)
     * @param playerEntity The player
     * @param dimensionID The world's dimension
     */
    public static void resetPermission(PlayerEntity playerEntity, int dimensionID)
    {
        CBFEPermissionService.get(dimensionID).resetWorldPermission(playerEntity);
    }

    /**
     * Reset the given player permission in the given world to the default value (Use the world permission)
     * @param username The username of the player
     * @param dimensionID The world's dimension
     */
    public static void resetPermission(String username, int dimensionID)
    {
        CBFEPermissionService.get(dimensionID).resetWorldPermission(username);
    }

    /**
     * Reset the given player permission in the given world to the default value (Use the world permission)
     * @param playerEntity The player
     * @param world The world
     */
    public static void resetPermission(PlayerEntity playerEntity, World world)
    {
        CBFEPermissionService.get(world.getDimension().getType().getId()).resetWorldPermission(playerEntity);
    }

    /**
     * Reset the given player permission in the given world to the default value (Use the world permission)
     * @param username The username of the player
     * @param world The world
     */
    public static void resetPermission(String username, World world)
    {
        CBFEPermissionService.get(world.getDimension().getType().getId()).resetWorldPermission(username);
    }
}
