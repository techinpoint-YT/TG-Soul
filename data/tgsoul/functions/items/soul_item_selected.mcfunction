# Handle when player selects a soul item
# This is a simplified version - full implementation would need more complex NBT checking

# Check if player can consume the soul (not at max)
scoreboard players operation @s tgsoul.temp = #max_souls tgsoul.config

# If under max souls, consume the item
execute if score @s tgsoul.souls < @s tgsoul.temp run function tgsoul:items/consume_soul_item