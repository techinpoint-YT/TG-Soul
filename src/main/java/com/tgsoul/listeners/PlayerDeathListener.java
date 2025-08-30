@@ .. @@
         // Create custom death message based on whether a soul was lost
         String deathMessageKey = shouldDropSoul ? "death-message-with-loss" : "death-message-no-loss";
         String deathMessage = plugin.getMessageUtil().getMessage(deathMessageKey,
                 Map.of("player", player.getName(), "souls", String.valueOf(remainingSouls)));
 
         // Set the death message (this prevents default death message)
         event.setDeathMessage(deathMessage);
+
+        // Update soul bar after death
+        Bukkit.getScheduler().runTaskLater(plugin, () -> {
+            if (player.isOnline()) {
+                plugin.getSoulBarManager().updateSoulBar(player);
+            }
+        }, 1L);
     }