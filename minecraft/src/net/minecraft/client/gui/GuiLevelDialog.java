package net.minecraft.client.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import net.minecraft.client.PlayerLoader;

public final class GuiLevelDialog extends Thread {
    private GuiLoadLevel screen;

    GuiLevelDialog(GuiLoadLevel var1) {
        this.screen = var1;
    }

    protected final JFileChooser saveFileChooser() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser(new File(this.screen.mc.mcDataDir, "saves")); // Automatically open the saves folder
        fileChooser.setDialogTitle("Save level");
        fileChooser.setSelectedFile(new File("default.mclevel"));
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".mclevel");
            }

            @Override
            public String getDescription() {
                return "Minecraft Level Files (*.mclevel)";
            }
        });

        return fileChooser;
    }

    public final void run() {
        try {
            JFileChooser fileChooser = saveFileChooser();
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String var5 = fileToSave.getAbsolutePath();
                if (!var5.toLowerCase().endsWith(".mclevel")) {
                    var5 = var5 + ".mclevel";
                }
                GuiLoadLevel.a(this.screen, new File(var5));
            }
        } finally {
            GuiLoadLevel.unknown(this.screen, false);
        }
    }
}
