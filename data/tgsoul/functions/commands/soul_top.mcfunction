# Display top players by soul count

tellraw @s [{"text":"=== Top Soul Leaders ===","color":"gold","bold":true}]

# This is simplified - datapack can't easily sort players by score
# Show instruction for manual checking
tellraw @s [{"text":"Use ","color":"gray"},{"text":"/scoreboard players list tgsoul.souls","color":"aqua"},{"text":" to see all player scores","color":"gray"}]
tellraw @s [{"text":"Or check individual players with ","color":"gray"},{"text":"/function tgsoul:commands/soul_stats","color":"aqua"}]

# Show current player's rank context
tellraw @s [{"text":"Your current souls: ","color":"yellow"},{"score":{"name":"@s","objective":"tgsoul.souls"},"color":"gold"},{"text":"/","color":"gray"},{"score":{"name":"#max_souls","objective":"tgsoul.config"},"color":"gold"}]