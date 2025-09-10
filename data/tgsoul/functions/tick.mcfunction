# TGSoul Datapack Tick Function
# Runs every tick to handle soul system

# Handle player deaths
execute as @a[scores={tgsoul.deaths=1..}] run function tgsoul:player/handle_death

# Update soul display for all players
execute as @a run function tgsoul:display/update_hud

# Handle soul item interactions
execute as @a[nbt={SelectedItem:{tag:{tgsoul_item:"soul"}}}] run function tgsoul:items/soul_item_selected

# Check for revival token placement
execute as @e[type=item,nbt={Item:{tag:{tgsoul_item:"revival_token"}}}] at @s if block ~ ~-1 ~ beacon run function tgsoul:revival/token_placed