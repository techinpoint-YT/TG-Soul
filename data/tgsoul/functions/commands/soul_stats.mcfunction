# Display player's soul statistics

tellraw @s [{"text":"=== Soul Stats for ","color":"gold","bold":true},{"selector":"@s","color":"gold"},{"text":" ===","color":"gold","bold":true}]
tellraw @s [{"text":"Souls: ","color":"gray"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":"/","color":"gray"},{"score":{"name":"#max_souls","objective":"tgsoul.config"},"color":"gold"}]
tellraw @s [{"text":"Needs Revival: ","color":"gray"},{"text":"No","color":"green"}]
execute if score @s tgsoul.revival matches 1 run tellraw @s [{"text":"Needs Revival: ","color":"gray"},{"text":"Yes","color":"red"}]
tellraw @s [{"text":"Configuration:","color":"yellow"}]
tellraw @s [{"text":"• Max Souls: ","color":"gray"},{"score":{"name":"#max_souls","objective":"tgsoul.config"},"color":"gold"}]
tellraw @s [{"text":"• Starting Souls: ","color":"gray"},{"score":{"name":"#starting_souls","objective":"tgsoul.config"},"color":"gold"}]
execute if score #drop_on_mob_death tgsoul.config matches 0 run tellraw @s [{"text":"• Soul Loss: ","color":"gray"},{"text":"PvP Only","color":"yellow"}]
execute if score #drop_on_mob_death tgsoul.config matches 1 run tellraw @s [{"text":"• Soul Loss: ","color":"gray"},{"text":"All Deaths","color":"red"}]