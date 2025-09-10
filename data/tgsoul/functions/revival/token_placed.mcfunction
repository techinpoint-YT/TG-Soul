# Handle revival token placement

# Check if revival is allowed
execute if score #allow_revive tgsoul.config matches 0 run tellraw @a[distance=..10] [{"text":"[TGSoul] ","color":"gold"},{"text":"Revival is not allowed.","color":"red"}]
execute if score #allow_revive tgsoul.config matches 0 run return 0

# Find nearest player needing revival
execute as @a[scores={tgsoul.revival=1},limit=1,sort=nearest] run function tgsoul:revival/revive_player

# If no player needs revival
execute unless entity @a[scores={tgsoul.revival=1}] run tellraw @a[distance=..10] [{"text":"[TGSoul] ","color":"gold"},{"text":"No players need revival.","color":"yellow"}]

# Remove the token item
kill @s

# Play revival sound (from config: block.end_portal.spawn)
playsound minecraft:block.end_portal.spawn player @a ~ ~ ~ 1 1

# Create revival effects
particle minecraft:happy_villager ~ ~1 ~ 1 1 1 0.1 50
particle minecraft:enchant ~ ~1 ~ 1 1 1 0.1 30