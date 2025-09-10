# Handle player death and soul loss
# Called when a player dies

# Store current souls
scoreboard players operation @s tgsoul.temp = @s tgsoul.souls

# Check if should drop soul (PvP or mob death based on config)
execute if score #drop_on_mob_death tgsoul.config matches 1 run function tgsoul:player/lose_soul
execute if score #drop_on_mob_death tgsoul.config matches 0 if entity @s[tag=killed_by_player] run function tgsoul:player/lose_soul

# Reset death counter
scoreboard players set @s tgsoul.deaths 0

# Remove PvP tag if present
tag @s remove killed_by_player