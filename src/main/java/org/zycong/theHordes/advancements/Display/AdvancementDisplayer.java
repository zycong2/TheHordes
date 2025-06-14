package org.zycong.theHordes.advancements.Display;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.zycong.theHordes.TheHordes;

import java.util.UUID;

// DO NOT TOUCH TEST FILE
public class AdvancementDisplayer {
    private final NamespacedKey key;
    private final String icon;
    private final String message;
    private final Style style;

    private AdvancementDisplayer(String icon, String message, Style style) {
        this.key = new NamespacedKey(TheHordes.getPlugin(), UUID.randomUUID().toString());
        this.icon = icon;
        this.message = message;
        this.style = style;
    }

    private void start(Player player) {
        createAdvancement();
        grantAdvancement(player);

        Bukkit.getScheduler().runTaskLater(TheHordes.getPlugin(), () -> {
            revokeAdvancement(player);
            }, 10);
    }

    private void createAdvancement() {
        Bukkit.getUnsafe().loadAdvancement(key, "{\n" +
                "    \"criteria\": {\n" +
                "        \"trigger\": {\n" +
                "            \"trigger\": \"minecraft:impossible\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"display\": {\n" +
                "        \"icon\": {\n" +
                "            \"item\": \"minecraft:" + icon + "\"\n" +
                "        },\n" +
                "        \"title\": {\n" +
                "            \"text\": \"" + message.replace("|", "\n") + "\"\n" +
                "        },\n" +
                "        \"description\": {\n" +
                "            \"text\": \"\"\n" +
                "        },\n" +
                "        \"background\": \"minecraft:textures/gui/advancements/backgrounds/adventure.png\",\n" +
                "        \"frame\": \"" + style.toString().toLowerCase() + "\",\n" +
                "        \"announce_to_chat\": false,\n" +
                "        \"show_toast\": true,\n" +
                "        \"hidden\": true\n" +
                "    },\n" +
                "    \"requirements\": [\n" +
                "        [\n" +
                "            \"trigger\"\n" +
                "        ]\n" +
                "    ]\n" +
                "}");
    }

        private void grantAdvancement(Player player) {
            player.getAdvancementProgress(Bukkit.getAdvancement(key)).awardCriteria("trigger");
        }

        private void revokeAdvancement(Player player) {
            player.getAdvancementProgress(Bukkit.getAdvancement(key)).revokeCriteria("trigger");
        }

        public static void displayAdvancementTo(Player player, String icon, String message, Style style) {
            new AdvancementDisplayer(icon, message, style).start(player);
        }
}
