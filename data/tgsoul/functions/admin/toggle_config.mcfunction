# Admin function to toggle configuration options

# Toggle drop on mob death
execute if score #drop_on_mob_death tgsoul.config matches 0 run scoreboard players set #drop_on_mob_death tgsoul.config 1
execute if score #drop_on_mob_death tgsoul.config matches 1 run scoreboard players set #drop_on_mob_death tgsoul.config 0

# Display current setting
execute if score #drop_on_mob_death tgsoul.config matches 0 run tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"text":"Soul loss mode: ","color":"green"},{"text":"PvP Only","color":"yellow"}]
execute if score #drop_on_mob_death tgsoul.config matches 1 run tellraw @a [{"text":"[TGSoul] ","color":"gold"},{"text":"Soul loss mode: ","color":"green"},{"text":"All Deaths","color":"red"}]