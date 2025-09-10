# Update soul HUD display for player
# Uses actionbar as per plugin config default

# Only show HUD if enabled
execute if score #hud_enabled tgsoul.config matches 0 run return 0

# Create heart symbols based on soul count
scoreboard players operation @s tgsoul.temp = @s tgsoul.souls

# Display souls in action bar with hearts (active color: red, inactive: gray)
execute if score @s tgsoul.souls matches 1 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥","color":"red"},{"text":"♡♡♡♡♡♡♡♡♡","color":"gray"},{"text":" (1/10)","color":"white"}]
execute if score @s tgsoul.souls matches 2 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥","color":"red"},{"text":"♡♡♡♡♡♡♡♡","color":"gray"},{"text":" (2/10)","color":"white"}]
execute if score @s tgsoul.souls matches 3 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥","color":"red"},{"text":"♡♡♡♡♡♡♡","color":"gray"},{"text":" (3/10)","color":"white"}]
execute if score @s tgsoul.souls matches 4 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥♥","color":"red"},{"text":"♡♡♡♡♡♡","color":"gray"},{"text":" (4/10)","color":"white"}]
execute if score @s tgsoul.souls matches 5 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥♥♥","color":"red"},{"text":"♡♡♡♡♡","color":"gray"},{"text":" (5/10)","color":"white"}]
execute if score @s tgsoul.souls matches 6 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥♥♥♥","color":"red"},{"text":"♡♡♡♡","color":"gray"},{"text":" (6/10)","color":"white"}]
execute if score @s tgsoul.souls matches 7 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥♥♥♥♥","color":"red"},{"text":"♡♡♡","color":"gray"},{"text":" (7/10)","color":"white"}]
execute if score @s tgsoul.souls matches 8 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥♥♥♥♥♥","color":"red"},{"text":"♡♡","color":"gray"},{"text":" (8/10)","color":"white"}]
execute if score @s tgsoul.souls matches 9 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥♥♥♥♥♥♥","color":"red"},{"text":"♡","color":"gray"},{"text":" (9/10)","color":"white"}]
execute if score @s tgsoul.souls matches 10 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♥♥♥♥♥♥♥♥♥♥","color":"red"},{"text":" (10/10)","color":"white"}]
execute if score @s tgsoul.souls matches 0 run title @s actionbar [{"text":"Souls: ","color":"gold"},{"text":"♡♡♡♡♡♡♡♡♡♡","color":"gray"},{"text":" (0/10) - NEED REVIVAL","color":"red"}]

# Set absorption hearts based on soul count (visual representation)
scoreboard players operation @s tgsoul.temp = @s tgsoul.souls
scoreboard players operation @s tgsoul.temp *= #2 tgsoul.config
execute store result entity @s AbsorptionAmount float 1 run scoreboard players get @s tgsoul.temp