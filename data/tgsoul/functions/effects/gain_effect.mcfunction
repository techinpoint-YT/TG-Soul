# Play particle and sound effects when gaining a soul
# Uses sounds from plugin config defaults

# Play gain sound (entity.ender_dragon.growl from config)
playsound minecraft:entity.ender_dragon.growl player @s ~ ~ ~ 0.5 1.5

# Create gain particles (compatible particles for MC 1.21)
particle minecraft:happy_villager ~ ~1 ~ 0.5 0.5 0.5 0.1 15
particle minecraft:enchant ~ ~1 ~ 0.5 0.5 0.5 0 20
particle minecraft:dust 0 1 1 1.2 ~ ~1 ~ 0.5 0.5 0.5 0 15