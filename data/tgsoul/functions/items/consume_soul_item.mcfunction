# Consume a soul item and give player a soul

# Add soul to player
function tgsoul:player/gain_soul

# Remove item from player's hand (simplified - would need more complex implementation)
# In a real datapack, this would require more sophisticated item handling

# Play consume sound
playsound minecraft:entity.experience_orb.pickup player @s ~ ~ ~ 1 1