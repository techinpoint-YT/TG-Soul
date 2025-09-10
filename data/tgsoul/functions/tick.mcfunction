# TGSoul Datapack Tick Function
# Runs every tick to handle soul system

# Handle player deaths
execute as @a[scores={tgsoul.deaths=1..}] run function tgsoul:player/handle_death

# Update soul display for all players (every 40 ticks = 2 seconds)
execute if score #tick tgsoul.config matches 0 as @a run function tgsoul:display/update_hud
scoreboard players add #tick tgsoul.config 1
execute if score #tick tgsoul.config matches 40.. run scoreboard players set #tick tgsoul.config 0

# Handle soul item interactions (simplified detection)
execute as @a[nbt={SelectedItem:{id:"minecraft:ghast_tear"}}] run function tgsoul:items/check_soul_item

# Check for revival token placement
execute as @e[type=item,nbt={Item:{id:"minecraft:beacon"}}] at @s if block ~ ~-1 ~ beacon run function tgsoul:revival/check_token_placement