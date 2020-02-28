package com.mooviies.cbforeveryone;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CUpdateCommandBlockPacket;
import net.minecraft.network.play.client.CUpdateMinecartCommandBlockPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Objects;

public class CBFEServerPlayNetHandler extends ServerPlayNetHandler {
    private MinecraftServer _server;

    public CBFEServerPlayNetHandler(ServerPlayerEntity playerIn) {
        super(playerIn.server, playerIn.connection.getNetworkManager(), playerIn);
        _server = playerIn.server;
    }

    @Override
    public void processUpdateCommandBlock(CUpdateCommandBlockPacket packetIn)
    {
        if(this.player.isCreative()) {
            super.processUpdateCommandBlock(packetIn);
            return;
        }

        if (!this._server.isCommandBlockEnabled()) {
            this.player.sendMessage(new TranslationTextComponent("advMode.notEnabled"));
            return;
        }

        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        CommandBlockLogic commandblocklogic = null;
        CommandBlockTileEntity commandblocktileentity = null;
        BlockPos blockpos = packetIn.getPos();
        TileEntity tileentity = this.player.world.getTileEntity(blockpos);
        if (tileentity instanceof CommandBlockTileEntity) {
            commandblocktileentity = (CommandBlockTileEntity)tileentity;
            commandblocklogic = commandblocktileentity.getCommandBlockLogic();
        }

        String s = packetIn.getCommand();
        boolean flag = packetIn.shouldTrackOutput();
        if (commandblocklogic != null) {
            CommandBlockTileEntity.Mode commandblocktileentity$mode = commandblocktileentity.getMode();
            Direction direction = this.player.world.getBlockState(blockpos).get(CommandBlockBlock.FACING);
            switch (packetIn.getMode()) {
                case SEQUENCE:
                    BlockState blockstate1 = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
                    this.player.world.setBlockState(blockpos, blockstate1.with(CommandBlockBlock.FACING, direction).with(CommandBlockBlock.CONDITIONAL, packetIn.isConditional()), 2);
                    break;
                case AUTO:
                    BlockState blockstate = Blocks.REPEATING_COMMAND_BLOCK.getDefaultState();
                    this.player.world.setBlockState(blockpos, blockstate.with(CommandBlockBlock.FACING, direction).with(CommandBlockBlock.CONDITIONAL, packetIn.isConditional()), 2);
                    break;
                case REDSTONE:
                default:
                    BlockState blockstate2 = Blocks.COMMAND_BLOCK.getDefaultState();
                    this.player.world.setBlockState(blockpos, blockstate2.with(CommandBlockBlock.FACING, direction).with(CommandBlockBlock.CONDITIONAL, packetIn.isConditional()), 2);
            }

            tileentity.validate();
            this.player.world.setTileEntity(blockpos, tileentity);
            commandblocklogic.setCommand(s);
            commandblocklogic.setTrackOutput(flag);
            if (!flag) {
                commandblocklogic.setLastOutput((ITextComponent) null);
            }

            commandblocktileentity.setAuto(packetIn.isAuto());
            if (commandblocktileentity$mode != packetIn.getMode()) {
                commandblocktileentity.func_226987_h_();
            }

            commandblocklogic.updateCommand();
            if (!StringUtils.isNullOrEmpty(s)) {
                this.player.sendMessage(new TranslationTextComponent("advMode.setCommand.success", s));
            }
        }
    }

    @Override
    public void processUpdateCommandMinecart(CUpdateMinecartCommandBlockPacket packetIn) {
        if(this.player.isCreative()) {
            super.processUpdateCommandMinecart(packetIn);
            return;
        }

        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());

        if (!this._server.isCommandBlockEnabled()) {
            this.player.sendMessage(new TranslationTextComponent("advMode.notEnabled"));
            return;
        }

        CommandBlockLogic commandblocklogic = packetIn.getCommandBlock(this.player.world);
        if (commandblocklogic != null) {
            commandblocklogic.setCommand(packetIn.getCommand());
            commandblocklogic.setTrackOutput(packetIn.shouldTrackOutput());
            if (!packetIn.shouldTrackOutput()) {
                commandblocklogic.setLastOutput((ITextComponent)null);
            }

            commandblocklogic.updateCommand();
            this.player.sendMessage(new TranslationTextComponent("advMode.setCommand.success", packetIn.getCommand()));
        }
    }
}
