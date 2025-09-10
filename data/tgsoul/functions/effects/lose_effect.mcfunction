# Play particle and sound effects when losing a soul
# Uses sounds from plugin config defaults

# Play lose sound (entity.wither.spawn from config)
playsound minecraft:entity.wither.spawn player @s ~ ~ ~ 0.5 1

# Create lose particles (compatible particles for MC 1.21)
particle minecraft:large_smoke ~ ~1 ~ 0.5 0.5 0.5 0.1 15
particle minecraft:dust 1 0 0 1 ~ ~1 ~ 0.5 0.5 0.5 0 20
particle minecraft:falling_dust{block_state:"minecraft:stone"} ~ ~1 ~ 0.5 0.5 0.5 0.5 10