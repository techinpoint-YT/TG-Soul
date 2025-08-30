@@ .. @@
         // Add soul to player
         plugin.getSoulManager().addSouls(event.getPlayer(), 1);
 
+        // Update soul bar
+        plugin.getSoulBarManager().updateSoulBar(event.getPlayer());
+
         event.setCancelled(true);
     }