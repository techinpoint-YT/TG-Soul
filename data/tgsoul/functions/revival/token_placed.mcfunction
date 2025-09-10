# Handle revival token placement

# Find nearest player needing revival
execute as @a[scores={tgsoul.revival=1},limit=1,sort=nearest] run function tgsoul:revival/revive_player

# Remove the token item
kill @s

# Play revival sound
playsound minecraft:block.end_portal.spawn player @a ~ ~ ~ 1 1

# Create revival effects
particle minecraft:happy_villager ~ ~1 ~ 1 1 1 0.1 50
particle minecraft:enchant ~ ~1 ~ 1 1 1 0.1 30