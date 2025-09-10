# Try to consume a soul item

# Check if player can consume the soul (not at max)
scoreboard players operation @s tgsoul.temp = #max_souls tgsoul.config

# If under max souls, consume the item
execute if score @s tgsoul.souls < @s tgsoul.temp run function tgsoul:items/consume_soul_item
execute if score @s tgsoul.souls >= @s tgsoul.temp run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You already have the maximum number of souls.","color":"gray"}]