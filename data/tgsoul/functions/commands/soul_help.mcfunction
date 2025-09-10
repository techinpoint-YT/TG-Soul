# Display help information for soul commands

tellraw @s [{"text":"=== TGSoul Help ===","color":"gold","bold":true}]
tellraw @s [{"text":"• ","color":"gray"},{"text":"/function tgsoul:commands/soul_stats","color":"aqua"},{"text":" - Check your soul count","color":"gray"}]
tellraw @s [{"text":"• ","color":"gray"},{"text":"/function tgsoul:commands/soul_withdraw","color":"aqua"},{"text":" - Withdraw a soul as an item","color":"gray"}]
tellraw @s [{"text":"• ","color":"gray"},{"text":"/function tgsoul:commands/soul_top","color":"aqua"},{"text":" - View top players by souls","color":"gray"}]
tellraw @s [{"text":"• ","color":"gray"},{"text":"Right-click soul items to consume them","color":"yellow"}]
tellraw @s [{"text":"• ","color":"gray"},{"text":"Craft Revival Tokens to revive players","color":"yellow"}]
tellraw @s [{"text":"Admin Commands:","color":"red","bold":true}]
tellraw @s [{"text":"• ","color":"gray"},{"text":"/function tgsoul:admin/give_souls","color":"red"},{"text":" - Give souls (run as target)","color":"gray"}]
tellraw @s [{"text":"• ","color":"gray"},{"text":"/function tgsoul:admin/revive_player","color":"red"},{"text":" - Revive player (run as target)","color":"gray"}]