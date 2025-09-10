# Drop a soul item at the current location

# Create soul item with player's name
summon item ~ ~ ~ {Item:{id:"minecraft:ghast_tear",Count:1b,tag:{display:{Name:'{"text":"Soul","color":"gold","italic":false}',Lore:['{"text":"A soul that can be consumed","color":"gray","italic":false}','{"text":"Right-click to use","color":"dark_gray","italic":false}']},tgsoul_item:"soul",tgsoul_owner:"temp",Enchantments:[{id:"minecraft:unbreaking",lvl:1s}],HideFlags:1}}}

# Store player name in the item (this would need NBT manipulation in actual implementation)
# For datapack limitations, we'll use a simplified approach

tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"A soul was dropped at your death location.","color":"gray"}]