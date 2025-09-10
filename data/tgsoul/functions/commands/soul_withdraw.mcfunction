# Withdraw a soul as an item

# Check if player has souls to withdraw and not at minimum
execute if score @s tgsoul.souls matches 2.. run function tgsoul:commands/do_withdraw
execute if score @s tgsoul.souls matches 0..1 run tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"You cannot withdraw your last soul!","color":"red"}]