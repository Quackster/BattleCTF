package org.alexdev.battlectf.managers.arena;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.alexdev.battlectf.BattleCTF;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Paths;

public class SchematicManager {
    private static String SCHEMATIC_FOLDER = "schematics";

    public static boolean save(Player player, String arenaName, Location min, Location max) {
        File schematicFile = getSchematicFile(arenaName);
        checkArenaFolder();

        if (schematicFile.exists()) {
            if (!schematicFile.delete()) {
                player.sendMessage(ChatColor.RED + "Failed to delete previous schematic file");
                return false;
            }

            try {
                schematicFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()),
                BlockVector3.at(min.getBlockX(), min.getBlockY(), min.getBlockZ()),
                BlockVector3.at(max.getBlockX(), max.getBlockY(), max.getBlockZ())
        );

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1);
        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
        forwardExtentCopy.setCopyingEntities(true);

        try {
            Operations.complete(forwardExtentCopy);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schematicFile))) {
            writer.write(clipboard);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static Clipboard load(Player player, String arenaName) {
        File schematicFile = getSchematicFile(arenaName);
        checkArenaFolder();

        if (schematicFile == null) {
            return null;
        }

        Clipboard clipboard = null;

        try (ClipboardReader reader = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getReader(new FileInputStream(schematicFile))) {
            clipboard = reader.read();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clipboard;
    }

    public static void paste(Player player, Clipboard clipboard) {
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(clipboard.getOrigin())
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Check schematic folder directory and create if not exists.
     *
     * @return true, if successful
     */
    private static boolean checkArenaFolder() {
        File arenaFolder = new File(BattleCTF.getInstance().getDataFolder().getAbsolutePath(), SCHEMATIC_FOLDER);

        if (arenaFolder.exists()) {
            if (arenaFolder.isFile()) {
                return arenaFolder.delete();
            }

            return arenaFolder.mkdir();
        }  else {
            return arenaFolder.mkdir();
        }

    }


    private static File getSchematicFile(String arenaName) {
        return Paths.get(BattleCTF.getInstance().getDataFolder().getAbsolutePath(), SCHEMATIC_FOLDER, arenaName + ".schematic").toFile();
    }
}
