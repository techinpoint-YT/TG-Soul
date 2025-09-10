# Remove a soul from player and handle consequences

# Decrease soul count
scoreboard players remove @s tgsoul.souls 1

# Drop soul item at death location
execute at @s run function tgsoul:items/drop_soul_item

# Check if player has no souls left
execute if score @s tgsoul.souls matches 0 run function tgsoul:player/no_souls_left

# Display message if player still has souls
execute if score @s tgsoul.souls matches 1.. run tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"selector":"@s","color":"red"},{"text":" died and lost a soul! (","color":"red"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":" souls remaining)","color":"red"}]

# Play lose effect if effects are enabled
execute if score #effects_enabled tgsoul.config matches 1 at @s run function tgsoul:effects/lose_effect