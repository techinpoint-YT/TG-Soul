# Withdraw a soul as an item

# Check if player has souls to withdraw
execute if score @s tgsoul.souls matches 1.. run function tgsoul:commands/do_withdraw
execute if score @s tgsoul.souls matches 0 run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You don't have any souls to withdraw!","color":"red"}]