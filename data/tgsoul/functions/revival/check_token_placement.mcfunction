# Check if placed beacon is a revival token

# Check if the beacon has revival token NBT
execute if entity @s[nbt={Item:{tag:{tgsoul_item:"revival_token"}}}] run function tgsoul:revival/token_placed

# If revival tokens are disabled, prevent usage
execute if score #revival_token_enabled tgsoul.config matches 0 run tellraw @a[distance=..10] [{"text":"[TGSoul] ","color":"gold"},{"text":"Revival tokens are disabled.","color":"red"}]