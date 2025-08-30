@@ .. @@
     @EventHandler
     public void onPlayerJoin(PlayerJoinEvent event) {
         // Initialize or update player data
         PlayerSoulData data = plugin.getSoulManager().getOrCreatePlayerData(event.getPlayer());
 
         // Assign or apply CustomModelData for soul items
         GiveSoul.handleSoulOnJoin(event.getPlayer(), plugin);
 
+        // Update soul bar/HUD for the player
+        plugin.getSoulBarManager().updateSoulBar(event.getPlayer());
+
         // Welcome message with soul count
         plugin.getMessageUtil().sendMessage(event.getPlayer(), "souls-remaining",
                 Map.of("souls", String.valueOf(data.getSouls())));
     }