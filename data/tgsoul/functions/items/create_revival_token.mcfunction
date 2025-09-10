# Create a revival token item
# This would be called from crafting recipes

give @s minecraft:beacon{display:{Name:'{"text":"Revival Token","color":"gold","italic":false}',Lore:['{"text":"Place to revive a player","color":"gray","italic":false}','{"text":"Requires 3 souls to craft","color":"dark_gray","italic":false}']},tgsoul_item:"revival_token",Enchantments:[{id:"minecraft:unbreaking",lvl:1s}],HideFlags:1} 1