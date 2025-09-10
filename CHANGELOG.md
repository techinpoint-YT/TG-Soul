# TGSoul Datapack Changelog

## Version 1.0.3 (Datapack Conversion)

### Added
- Complete datapack conversion from original plugin
- Scoreboard-based soul tracking system
- Function-based command system
- Revival token crafting recipe
- Advancement system for soul milestones
- Particle effects for soul gain/loss
- Action bar HUD display
- Spectator mode punishment system

### Features Converted
- ✅ Soul system (gain/lose souls)
- ✅ Death handling with soul loss
- ✅ Soul items (simplified)
- ✅ Revival tokens and revival system
- ✅ HUD display (action bar)
- ✅ Particle effects
- ✅ Sound effects
- ✅ Basic commands via functions
- ✅ Admin functions
- ✅ Configuration via scoreboards

### Limitations vs Plugin
- ❌ Complex NBT item handling
- ❌ Right-click item detection
- ❌ Custom GUIs
- ❌ Permission system
- ❌ File-based data storage
- ❌ Resource pack integration
- ❌ GeyserMC compatibility
- ❌ Complex ban system (uses spectator mode instead)

### Technical Changes
- Replaced Java classes with mcfunction files
- Converted plugin.yml to pack.mcmeta
- Replaced MySQL/YAML storage with scoreboards
- Converted Bukkit events to tick-based detection
- Simplified item system due to datapack limitations
- Replaced permission checks with function-based access

### Configuration
- Maximum souls: Configurable via scoreboard
- Starting souls: Configurable via scoreboard  
- Drop on mob death: Configurable via scoreboard
- All other settings hardcoded in functions

### Known Issues
- Soul item ownership detection is simplified
- Right-click detection limited to item selection
- No persistent data across world reloads (scoreboards reset)
- Revival token placement detection is basic

### Future Improvements
- Enhanced NBT handling for better item ownership
- More sophisticated item interaction detection
- Better data persistence solutions
- Enhanced admin tools