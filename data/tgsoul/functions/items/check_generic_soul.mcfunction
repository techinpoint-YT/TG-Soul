# Check if a generic ghast tear might be a soul item
# Fallback for when NBT detection doesn't work perfectly

# Simple check - if player is holding ghast tear and has less than max souls
scoreboard players operation @s tgsoul.temp = #max_souls tgsoul.config
execute if score @s tgsoul.souls < @s tgsoul.temp run tellraw @s [{"text":"[TGSoul] ","color":"yellow"},{"text":"Right-click with the soul item to consume it!","color":"yellow"}]