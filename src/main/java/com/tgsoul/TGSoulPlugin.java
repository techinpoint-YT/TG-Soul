@@ .. @@
         // Clean up soul bars
         if (soulBarManager != null) {
             for (Player player : Bukkit.getOnlinePlayers()) {
-                soulBarManager.removeSoulBar(player); // Updated from removeBars
+                soulBarManager.removeSoulBar(player);
             }
+            soulBarManager.shutdown();
         }
 
         getLogger().info("TGSoul has been disabled!");