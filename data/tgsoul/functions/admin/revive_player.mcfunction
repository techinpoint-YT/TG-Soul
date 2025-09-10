# Admin function to revive a player
# Usage: execute as <target> run function tgsoul:admin/revive_player

# Set souls to maximum
scoreboard players operation @s tgsoul.souls = #max_souls tgsoul.config

# Set revival status to 0
scoreboard players set @s tgsoul.revival 0

# Set to survival mode
gamemode survival @s

# Display message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You have been revived by an admin!","color":"green"}]

# Play revival effects
function tgsoul:effects/gain_effect