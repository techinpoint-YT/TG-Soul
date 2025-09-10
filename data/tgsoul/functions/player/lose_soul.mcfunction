# Remove a soul from player and handle consequences

# Decrease soul count
scoreboard players remove @s tgsoul.souls 1

# Drop soul item at death location
execute at @s run function tgsoul:items/drop_soul_item

# Check if player has no souls left
execute if score @s tgsoul.souls matches 0 run function tgsoul:player/no_souls_left

# Display message if player still has souls
execute if score @s tgsoul.souls matches 1.. run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You lost a soul! (","color":"red"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":" remaining)","color":"red"}]

# Play lose effect
execute at @s run function tgsoul:effects/lose_effect