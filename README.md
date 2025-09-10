# TGSoul Datapack

A comprehensive soul system datapack for Minecraft 1.21 to 1.21.8, converted from the original TGSoul plugin.

## Features

- **Soul System**: Players start with 3 souls and can have up to 10 souls maximum
- **Death Mechanics**: Lose souls on death (configurable for PvP only or all deaths)
- **Soul Items**: Souls drop as items that can be picked up and consumed
- **Revival System**: Craft Revival Tokens to revive players who lost all souls
- **HUD Display**: Action bar shows current soul count with visual hearts
- **Spectator Mode**: Players with 0 souls are put in spectator mode until revived
- **Particle Effects**: Visual and audio feedback for gaining/losing souls
- **Advancements**: Achievement system for soul milestones

## Installation

1. Download the datapack files
2. Place the datapack folder in your world's `datapacks` directory
3. Run `/reload` in-game to load the datapack
4. The datapack will automatically initialize all players

## Commands

Since this is a datapack, commands are function calls:

- `/function tgsoul:commands/soul_stats` - Check your soul count
- `/function tgsoul:commands/soul_withdraw` - Withdraw a soul as an item
- `/function tgsoul:commands/soul_top` - View top players
- `/function tgsoul:commands/soul_help` - Show help information

### Admin Commands

- `/function tgsoul:admin/give_souls` - Give souls to a player (run as target)
- `/function tgsoul:admin/revive_player` - Revive a player (run as target)
- `/function tgsoul:admin/set_max_souls` - Set maximum souls

## Configuration

Edit the values in `data/tgsoul/functions/load.mcfunction` to configure:

- `#max_souls` - Maximum souls per player (default: 10)
- `#starting_souls` - Starting souls for new players (default: 3)
- `#drop_on_mob_death` - Whether souls drop on mob deaths (0=PvP only, 1=all deaths)

## Crafting

### Revival Token Recipe
```
[Paper] [Soul] [Paper]
[Soul]  [Star] [Soul]
[Block] [Star] [Block]
```

Where:
- Soul = Ghast Tear with soul NBT data
- Star = Nether Star
- Block = Netherite Block

## Limitations

Due to datapack limitations compared to plugins:

1. **NBT Handling**: Soul ownership is simplified - full NBT manipulation would require more complex systems
2. **Item Detection**: Right-click detection is limited - uses selection-based detection instead
3. **Player Data**: Uses scoreboards instead of persistent file storage
4. **Permissions**: No permission system - all players can use basic functions
5. **GUI**: No custom GUIs - uses chat-based interfaces

## Compatibility

- Minecraft 1.21 to 1.21.8
- Single-player and multiplayer
- Compatible with other datapacks
- No external dependencies required

## Credits

Original TGSoul Plugin by Prabin (Techinpoint Gamerz)
Datapack conversion maintains core functionality while adapting to datapack limitations.

YouTube: www.youtube.com/@TechinpointGamerz
Website: https://techinpointgamerz.netlify.app