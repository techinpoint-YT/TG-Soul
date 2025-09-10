# Revive a player who needs revival

# Reset souls to maximum
scoreboard players operation @s tgsoul.souls = #max_souls tgsoul.config

# Set revival status to 0
scoreboard players set @s tgsoul.revival 0

# Set to survival mode
gamemode survival @s

# Teleport to revival location (where token was placed)
tp @s ~ ~1 ~

# Display revival message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You have been revived! You now have ","color":"green"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":" souls.","color":"green"}]

# Announce to all players
tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"selector":"@s","color":"green"},{"text":" has been revived!","color":"green"}]

# Play revival effects
function tgsoul:effects/gain_effect