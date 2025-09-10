# Handle when player has no souls left
# Uses spectator mode instead of banning (datapack limitation)

# Set revival status
scoreboard players set @s tgsoul.revival 1

# Set to spectator mode (permanent ban equivalent)
gamemode spectator @s

# Display message to player
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You have been permanently banned for losing all souls! Only admins can revive you now.","color":"red"}]

# Announce to all players
tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"selector":"@s","color":"red"},{"text":" has lost all their souls and needs revival!","color":"red"}]