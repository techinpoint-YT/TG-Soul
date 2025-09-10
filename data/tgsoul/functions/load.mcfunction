# TGSoul Datapack Load Function
# Initialize scoreboards and settings

# Create scoreboards
scoreboard objectives add tgsoul.souls dummy "Souls"
scoreboard objectives add tgsoul.deaths deathCount "Deaths"
scoreboard objectives add tgsoul.revival dummy "Revival Status"
scoreboard objectives add tgsoul.temp dummy "Temporary Values"
scoreboard objectives add tgsoul.config dummy "Configuration"

# Set default configuration values from plugin config.yml
scoreboard players set #max_souls tgsoul.config 10
scoreboard players set #starting_souls tgsoul.config 3
scoreboard players set #drop_on_mob_death tgsoul.config 0
scoreboard players set #allow_revive tgsoul.config 1
scoreboard players set #hud_enabled tgsoul.config 1
scoreboard players set #effects_enabled tgsoul.config 1
scoreboard players set #revival_token_enabled tgsoul.config 1

# Initialize all online players
execute as @a run function tgsoul:player/initialize

# Display load message
tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"text":"Datapack loaded successfully! Compatible with MC 1.21-1.21.8","color":"green"}]
tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"text":"Starting souls: 3, Max souls: 10, PvP-only soul loss","color":"gray"}]