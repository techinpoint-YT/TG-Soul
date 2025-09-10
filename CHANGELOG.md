# TGSoul Datapack Changelog

## Version 1.0.3 (Datapack Conversion)

### Added
- Complete datapack conversion from original TGSoul plugin
- Scoreboard-based soul tracking system matching plugin defaults
- Function-based command system
- Revival token crafting recipe (Paper + 3 Souls + 2 Nether Stars + 2 Netherite Blocks)
- Advancement system for soul milestones
- Particle effects for soul gain/loss matching plugin config
- Action bar HUD display with heart symbols
- Spectator mode punishment system (replaces banning)

### Default Configuration (from Plugin)
- **Starting Souls**: 3 (soul.starting-souls)
- **Maximum Souls**: 10 (soul.max)
- **Soul Material**: Ghast Tear (soul.material: "GHAST_TEAR")
- **Revival Token Material**: Beacon (soul.revival-token.material: "BEACON")
- **Soul Loss Mode**: PvP only (soul.drop-on-mob-death: false)
- **HUD Type**: Action bar (soul.hud.type: "actionbar")
- **Effects**: Enabled (soul.effects.lose/gain.enabled: true)
- **Revival**: Allowed (soul.allow-revive: true)

### Sounds (from Plugin Config)
- **Revival**: `block.end_portal.spawn`
- **Withdraw**: `block.glass.break`
- **Gain**: `entity.ender_dragon.growl`
- **Lose**: `entity.wither.spawn`

### Features Converted
- ✅ Soul system (gain/lose souls)
- ✅ Death handling with configurable soul loss
- ✅ Soul items (simplified ghast tears)
- ✅ Revival tokens and revival system
- ✅ HUD display (action bar with hearts)
- ✅ Particle effects (compatible particles for MC 1.21)
- ✅ Sound effects (matching plugin config)
- ✅ Basic commands via functions
- ✅ Admin functions
- ✅ Configuration via scoreboards

### Limitations vs Plugin
- ❌ Complex item ownership (any ghast tear works)
- ❌ Right-click detection (selection-based only)
- ❌ Custom GUIs (chat-based interfaces)
- ❌ Permission system (function-based access)
- ❌ File-based data storage (scoreboards only)
- ❌ Resource pack integration
- ❌ GeyserMC compatibility
- ❌ Actual ban system (uses spectator mode)
- ❌ CustomModelData support
- ❌ Boss bar HUD option

### Technical Implementation
- Replaced Java classes with 25+ mcfunction files
- Converted plugin.yml to pack.mcmeta (pack format 48 for MC 1.21)
- Replaced database storage with scoreboard objectives
- Converted Bukkit events to tick-based detection
- Simplified item system due to datapack NBT limitations
- Replaced permission checks with function-based access
- Used spectator mode instead of ban system

### File Structure
```
pack.mcmeta
data/
├── minecraft/tags/functions/ (load/tick integration)
├── tgsoul/functions/
│   ├── load.mcfunction (initialization)
│   ├── tick.mcfunction (main game loop)
│   ├── player/ (player management)
│   ├── items/ (soul item handling)
│   ├── revival/ (revival token system)
│   ├── effects/ (particles and sounds)
│   ├── display/ (HUD system)
│   ├── commands/ (player commands)
│   └── admin/ (admin functions)
├── tgsoul/recipes/ (revival token recipe)
└── tgsoul/advancements/ (achievement system)
```

### Configuration Options
All configurable via scoreboard values in load.mcfunction:
- `#max_souls` - Maximum souls per player (10)
- `#starting_souls` - Starting souls for new players (3)
- `#drop_on_mob_death` - Soul loss mode (0=PvP only, 1=all deaths)
- `#allow_revive` - Revival system enabled (1=yes, 0=no)
- `#hud_enabled` - HUD display enabled (1=yes, 0=no)
- `#effects_enabled` - Particle effects enabled (1=yes, 0=no)
- `#revival_token_enabled` - Revival tokens enabled (1=yes, 0=no)

### Known Issues
- Soul item ownership is simplified (any ghast tear can be consumed)
- Right-click detection limited to item selection
- Players must manually remove consumed soul items
- Scoreboard data resets on world reload
- No persistent player data across sessions

### Future Improvements
- Enhanced NBT handling for better item ownership
- More sophisticated item interaction detection
- Better data persistence solutions
- Enhanced admin configuration tools
- Compatibility improvements for multiplayer servers