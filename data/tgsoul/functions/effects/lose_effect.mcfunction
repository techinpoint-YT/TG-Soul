# Play particle and sound effects when losing a soul

# Play lose sound
playsound minecraft:entity.wither.spawn player @s ~ ~ ~ 0.5 1

# Create lose particles
particle minecraft:large_smoke ~ ~1 ~ 0.5 0.5 0.5 0.1 15
particle minecraft:dust 1 0 0 1 ~ ~1 ~ 0.5 0.5 0.5 0 20
particle minecraft:falling_dust stone ~ ~1 ~ 0.5 0.5 0.5 0.5 10