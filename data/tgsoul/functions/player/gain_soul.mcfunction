# Give a soul to the player

# Get max souls from config
scoreboard players operation @s tgsoul.temp = #max_souls tgsoul.config

# Only add soul if under maximum
execute if score @s tgsoul.souls < @s tgsoul.temp run scoreboard players add @s tgsoul.souls 1
execute if score @s tgsoul.souls < @s tgsoul.temp run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You gained a soul! (","color":"green"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":" total)","color":"green"}]
execute if score @s tgsoul.souls < @s tgsoul.temp run function tgsoul:effects/gain_effect

# Display max souls message if at maximum
execute if score @s tgsoul.souls >= @s tgsoul.temp run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You already have the maximum number of souls.","color":"gray"}]