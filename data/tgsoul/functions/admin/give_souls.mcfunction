# Admin function to give souls to a player
# Usage: execute as <target> run function tgsoul:admin/give_souls

# Check if player is at max souls
scoreboard players operation @s tgsoul.temp = #max_souls tgsoul.config
execute if score @s tgsoul.souls >= @s tgsoul.temp run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You already have maximum souls!","color":"red"}]
execute if score @s tgsoul.souls >= @s tgsoul.temp run return 0

# Add souls
scoreboard players add @s tgsoul.souls 1

# Display message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You received a soul from an admin!","color":"green"}]

# Play gain effect if enabled
execute if score #effects_enabled tgsoul.config matches 1 run function tgsoul:effects/gain_effect