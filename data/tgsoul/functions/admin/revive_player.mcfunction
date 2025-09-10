# Admin function to revive a player
# Usage: execute as <target> run function tgsoul:admin/revive_player

# Check if revival is allowed
execute if score #allow_revive tgsoul.config matches 0 run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"Revival is disabled.","color":"red"}]
execute if score #allow_revive tgsoul.config matches 0 run return 0

# Set souls to maximum
scoreboard players operation @s tgsoul.souls = #max_souls tgsoul.config

# Set revival status to 0
scoreboard players set @s tgsoul.revival 0

# Set to survival mode
gamemode survival @s

# Display message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You have been revived by an admin!","color":"green"}]

# Announce to all players
tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"selector":"@s","color":"green"},{"text":" has been revived by an admin!","color":"green"}]

# Play revival effects if enabled
execute if score #effects_enabled tgsoul.config matches 1 run function tgsoul:effects/gain_effect