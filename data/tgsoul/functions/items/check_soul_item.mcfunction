# Check if selected item is a soul item and handle consumption
# This is simplified due to datapack limitations

# Check if holding a ghast tear (soul material from config)
execute if entity @s[nbt={SelectedItem:{id:"minecraft:ghast_tear",tag:{tgsoul_item:"soul"}}}] run function tgsoul:items/try_consume_soul

# Alternative check for any ghast tear if NBT detection fails
execute if entity @s[nbt={SelectedItem:{id:"minecraft:ghast_tear"}}] unless entity @s[nbt={SelectedItem:{tag:{tgsoul_item:"soul"}}}] run function tgsoul:items/check_generic_soul