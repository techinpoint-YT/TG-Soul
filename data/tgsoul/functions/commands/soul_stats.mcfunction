# Display player's soul statistics

tellraw @s [{"text":"=== Soul Stats ===","color":"gold","bold":true}]
tellraw @s [{"text":"Souls: ","color":"gray"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":"/","color":"gray"},{"score":{"name":"#max_souls","objective":"tgsoul.config"},"color":"gold"}]
tellraw @s [{"text":"Needs Revival: ","color":"gray"},{"text":"No","color":"green"}]
execute if score @s tgsoul.revival matches 1 run tellraw @s [{"text":"Needs Revival: ","color":"gray"},{"text":"Yes","color":"red"}]