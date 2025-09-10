# Handle when player has no souls left

# Set revival status
scoreboard players set @s tgsoul.revival 1

# Set to spectator mode
gamemode spectator @s

# Display ban message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You have lost all your souls and are now in spectator mode! You need a Revival Token to be revived.","color":"red"}]

# Announce to all players
tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"selector":"@s","color":"red"},{"text":" has lost all their souls and needs revival!","color":"red"}]