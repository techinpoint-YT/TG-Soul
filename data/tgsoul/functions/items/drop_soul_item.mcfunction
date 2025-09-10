# Drop a soul item at the current location
# Uses ghast_tear as per plugin config default

# Create soul item with custom NBT
summon item ~ ~ ~ {Item:{id:"minecraft:ghast_tear",Count:1b,tag:{display:{Name:'{"text":"Soul","color":"gold","italic":false}',Lore:['{"text":"A soul that can be consumed","color":"gray","italic":false}','{"text":"Right-click to use","color":"dark_gray","italic":false}']},tgsoul_item:"soul",tgsoul_type:"soul_item",Enchantments:[{id:"minecraft:unbreaking",lvl:1s}],HideFlags:1}}}

# Display message
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"A soul was dropped at your death location.","color":"gray"}]