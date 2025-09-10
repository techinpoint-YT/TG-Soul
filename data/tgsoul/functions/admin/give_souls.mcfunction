# Admin function to give souls to a player
# Usage: execute as <target> run function tgsoul:admin/give_souls

# Add souls (this is simplified - would need parameter system)
scoreboard players add @s tgsoul.souls 1

# Display message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You received a soul from an admin!","color":"green"}]

# Play gain effect
function tgsoul:effects/gain_effect