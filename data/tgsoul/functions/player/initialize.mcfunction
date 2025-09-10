# Initialize a player's soul data
# Called when a player joins or datapack loads

# Set starting souls if player doesn't have souls set
execute unless score @s tgsoul.souls matches 0.. run scoreboard players operation @s tgsoul.souls = #starting_souls tgsoul.config

# Reset death counter
scoreboard players set @s tgsoul.deaths 0

# Set revival status to 0 (not needing revival)
scoreboard players set @s tgsoul.revival 0

# Display welcome message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You have ","color":"gray"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":" souls remaining.","color":"gray"}]