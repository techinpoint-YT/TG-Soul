# TGSoul Datapack

A comprehensive soul system datapack for Minecraft 1.21 to 1.21.8, converted from the original TGSoul plugin by Prabin (Techinpoint Gamerz).

## Features

- **Soul System**: Players start with 3 souls and can have up to 10 souls maximum
- **Death Mechanics**: Lose souls on death (PvP only by default, configurable)
- **Soul Items**: Souls drop as ghast tears that can be picked up and consumed
- **Revival System**: Craft Revival Tokens to revive players who lost all souls
- **HUD Display**: Action bar shows current soul count with visual hearts
- **Spectator Mode**: Players with 0 souls are put in spectator mode until revived
- **Particle Effects**: Visual and audio feedback for gaining/losing souls
- **Advancements**: Achievement system for soul milestones

## Default Configuration (from Plugin)

- **Starting Souls**: 3
- **Maximum Souls**: 10
- **Soul Material**: Ghast Tear
- **Revival Token Material**: Beacon
- **Ban Mode**: Spectator mode (permanent equivalent)
- **Soul Loss**: PvP only (drop-on-mob-death: false)
- **HUD**: Action bar enabled with heart symbols
- **Effects**: Enabled with particle and sound effects
- **Revival**: Allowed with Revival Tokens

## Installation

1. Download the datapack files
2. Place the datapack folder in your world's `datapacks` directory
3. Run `/reload` in-game to load the datapack
4. The datapack will automatically initialize all players

## Commands

Since this is a datapack, commands are function calls:

### Player Commands
- `/function tgsoul:commands/soul_stats` - Check your soul count and stats
- `/function tgsoul:commands/soul_withdraw` - Withdraw a soul as an item
- `/function tgsoul:commands/soul_top` - View top players by souls
- `/function tgsoul:commands/soul_help` - Show help information

### Admin Commands
- `/execute as <player> run function tgsoul:admin/give_souls` - Give souls to a player
- `/execute as <player> run function tgsoul:admin/revive_player` - Revive a player
- `/function tgsoul:admin/set_max_souls` - Reset max souls to default (10)
- `/function tgsoul:admin/toggle_config` - Toggle PvP-only vs all-death soul loss

## Configuration

The datapack uses scoreboard values for configuration. You can modify these in `data/tgsoul/functions/load.mcfunction`:

```mcfunction
# Configuration values (matching plugin defaults)
scoreboard players set #max_souls tgsoul.config 10
scoreboard players set #starting_souls tgsoul.config 3
scoreboard players set #drop_on_mob_death tgsoul.config 0  # 0=PvP only, 1=all deaths
scoreboard players set #allow_revive tgsoul.config 1       # 1=enabled, 0=disabled
scoreboard players set #hud_enabled tgsoul.config 1        # 1=enabled, 0=disabled
scoreboard players set #effects_enabled tgsoul.config 1    # 1=enabled, 0=disabled
```

## Crafting Recipes

### Revival Token Recipe (from plugin config)
```
[Paper] [Soul] [Paper]
[Soul]  [Star] [Soul]
[Block] [Star] [Block]
```

Where:
- Soul = Ghast Tear (any ghast tear works in datapack)
- Star = Nether Star  
- Block = Netherite Block

## Sounds & Effects (from Plugin Config)

- **Revival Sound**: `block.end_portal.spawn`
- **Withdraw Sound**: `block.glass.break` 
- **Gain Sound**: `entity.ender_dragon.growl`
- **Lose Sound**: `entity.wither.spawn`

## Advancements

- **First Soul**: Start your soul journey
- **Soul Survivor**: Survive with only 1 soul
- **Soul Master**: Reach maximum souls (10)
- **Soul Savior**: Revive another player

## Limitations vs Plugin

Due to datapack constraints:

1. **Item Ownership**: Simplified soul item system (any ghast tear can be consumed)
2. **Right-Click Detection**: Limited to selection-based detection
3. **Inventory Management**: Players must manually remove consumed items
4. **Ban System**: Uses spectator mode instead of actual banning
5. **Permissions**: No permission system - all players can use functions
6. **Data Persistence**: Uses scoreboards (resets on world reload)
7. **GUI**: Chat-based interfaces instead of custom GUIs

## Compatibility

- Minecraft 1.21 to 1.21.8
- Single-player and multiplayer
- Compatible with other datapacks
- No external dependencies required

## Credits

**Original Plugin**: TGSoul by Prabin (Techinpoint Gamerz)
- YouTube: www.youtube.com/@TechinpointGamerz
- Website: https://techinpointgamerz.netlify.app

**Datapack Conversion**: Maintains core functionality while adapting to datapack limitations.