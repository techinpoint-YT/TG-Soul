# Update soul HUD display for player

# Display souls in action bar
title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥","color":"red","extra":[{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"}]},{"text":"/","color":"gray"},{"score":{"name":"#max_souls","objective":"tgsoul.config"},"color":"gold"}]

# Set absorption hearts based on soul count (visual representation)
scoreboard players operation @s tgsoul.temp = @s tgsoul.souls
scoreboard players operation @s tgsoul.temp *= #2 tgsoul.config
execute store result entity @s AbsorptionAmount float 1 run scoreboard players get @s tgsoul.temp