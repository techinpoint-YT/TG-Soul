# Admin function to set maximum souls
# Default: 10 souls (from plugin config)

scoreboard players set #max_souls tgsoul.config 10
tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"text":"Maximum souls set to ","color":"green"},{"score":{"name":"#max_souls","objective":"tgsoul.config"},"color":"gold"}]