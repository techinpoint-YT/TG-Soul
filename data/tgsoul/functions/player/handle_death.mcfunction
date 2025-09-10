# Handle player death and soul loss
# Called when a player dies

# Store current souls for comparison
scoreboard players operation @s tgsoul.temp = @s tgsoul.souls

# Check if should drop soul based on config
# drop_on_mob_death = 0 means PvP only (default from plugin config)
# drop_on_mob_death = 1 means all deaths
execute if score #drop_on_mob_death tgsoul.config matches 1 run function tgsoul:player/lose_soul
execute if score #drop_on_mob_death tgsoul.config matches 0 if entity @s[tag=killed_by_player] run function tgsoul:player/lose_soul

# If no soul was lost, still show death message
execute if score @s tgsoul.souls = @s tgsoul.temp run tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"selector":"@s","color":"red"},{"text":" died. (","color":"red"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":" souls remaining)","color":"red"}]

# Reset death counter
scoreboard players set @s tgsoul.deaths 0

# Remove PvP tag if present
tag @s remove killed_by_player