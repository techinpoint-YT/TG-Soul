# Actually withdraw the soul

# Remove one soul
scoreboard players remove @s tgsoul.souls 1

# Give soul item
function tgsoul:items/drop_soul_item

# Play withdraw sound
playsound minecraft:block.glass.break player @s ~ ~ ~ 1 1

# Display message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You withdrew a soul as an item. (","color":"gray"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":" remaining)","color":"gray"}]

# Play lose effect
function tgsoul:effects/lose_effect