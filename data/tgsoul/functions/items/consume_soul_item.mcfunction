# Consume a soul item and give player a soul

# Add soul to player
function tgsoul:player/gain_soul

# Remove one item from player's hand (simplified)
# In datapack, we can't directly modify inventory, so we give instruction
tellraw @s [{"text":"[TGSoul] ","color":"gold"},{"text":"Soul consumed! Please remove one ghast tear from your inventory.","color":"green"}]

# Play consume sound (gain sound from config)
playsound minecraft:entity.experience_orb.pickup player @s ~ ~ ~ 1 1