# TGSoul Plugin

A comprehensive soul system plugin for Minecraft servers that adds a unique survival mechanic where players have limited souls and must manage them carefully.

## 🎮 Features

### Core Soul System
- **Limited Souls**: Players start with a configurable number of souls (default: 3)
- **Soul Loss**: Players lose souls when they die (configurable for PvP only or all deaths)
- **Soul Items**: Souls can be withdrawn as physical items and traded between players
- **Revival System**: Players can be revived using Revival Tokens crafted from soul items

### Advanced Features
- **Custom Model Data Support**: Soul items support custom textures via resource packs (MC 1.14-1.21.8)
- **Particle Effects**: Customizable particle effects for soul gain/loss events
- **Sound Effects**: Configurable sounds for various soul-related actions
- **GUI System**: Interactive recipe viewer for Revival Tokens
- **Punishment System**: Configurable punishment when players lose all souls (ban/spectator mode)

### Cross-Platform Compatibility
- **GeyserMC Support**: Full compatibility with Bedrock Edition players
- **Version Support**: Minecraft 1.20.x to 1.21.8
- **Performance Optimized**: Efficient data handling and particle management

## 📋 Requirements

- **Minecraft Version**: 1.20.x to 1.21.8
- **Server Software**: Paper, Purpur, or other Paper-based servers
- **Java Version**: 17 or higher
- **Optional**: GeyserMC for Bedrock Edition support

## 🚀 Installation

1. Download the latest TGSoul.jar from releases
2. Place the jar file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin using `config.yml` and `messages.yml`

## ⚙️ Configuration

### Main Configuration (`config.yml`)

```yaml
soul:
  starting-souls: 3          # Souls new players start with
  max: 3                     # Maximum souls a player can have
  material: "GHAST_TEAR"     # Item used for soul items
  ban-mode: "permanent"      # What happens at 0 souls: permanent, temp, spectator
  drop-on-mob-death: false   # Whether souls drop on mob deaths (false = PvP only)
  
  revival-token:
    enabled: true            # Enable Revival Token system
    material: "BEACON"       # Material for Revival Tokens
    
  custom-model-data:
    enabled: true            # Enable custom textures (requires resource pack)
    min: 1                   # Minimum variant number
    max: 10                  # Maximum variant number
```

### Messages (`messages.yml`)
All plugin messages are fully customizable with color code support.

## 🎯 Commands

### Player Commands
- `/soul` - Check your current soul count
- `/soul help` - Display help menu
- `/soul recipe` - View Revival Token recipe
- `/soul top [limit]` - View top players by soul count
- `/soul stats [player]` - View player statistics
- `/soulwithdraw` - Withdraw a soul as a physical item

### Admin Commands
- `/soul give <player> <amount>` - Give souls to a player
- `/soul take <player> <amount>` - Remove souls from a player
- `/soul set <player> <amount>` - Set a player's soul count
- `/soul get <player>` - Check a player's soul count
- `/soul unban <player>` - Unban and revive a player
- `/soul pack <player> <0-10>` - Set player's soul variant (CustomModelData)
- `/soul reload` - Reload plugin configuration
- `/soul testparticle <lose|gain|both>` - Test particle effects

## 🔧 Permissions

- `tgsoul.use` - Basic soul commands (default: true)
- `tgsoul.withdraw` - Withdraw souls as items (default: true)
- `tgsoul.admin` - Admin commands (default: op)
- `tgsoul.*` - All permissions

## 🎨 Resource Pack Support

TGSoul supports custom soul item textures through CustomModelData:
- **Default Soul**: CustomModelData 0 (no custom texture)
- **Soul Variants**: CustomModelData 1-10 (requires resource pack)

### Creating Resource Pack
1. Create custom textures for `ghast_tear` (or your configured material)
2. Use CustomModelData values 1-10 for different soul variants
3. Players are automatically assigned random variants on first join

**Note**: CustomModelData support is available for Minecraft 1.14-1.21.8. The plugin automatically detects version compatibility.

## 🌐 GeyserMC Integration

Full support for Bedrock Edition players:
- Automatic detection of Bedrock players
- Optimized particle effects for mobile performance
- Cross-platform soul trading and revival system

## 📊 Data Storage

- **Format**: YAML-based storage in `playerdata.yml`
- **Auto-Save**: Configurable automatic saving (default: 5 minutes)
- **Thread-Safe**: Concurrent data handling for performance
- **Backup-Friendly**: Human-readable YAML format

## 🔄 Soul Mechanics

### Death System
1. Player dies → Loses 1 soul (if enabled for death type)
2. Soul item drops at death location
3. Other players can collect and use the soul item
4. At 0 souls → Player is banned/put in spectator mode

### Revival System
1. Collect 3 soul items from the target player
2. Craft a Revival Token using the configured recipe
3. Place the Revival Token to revive the player
4. Revived player gets maximum souls and is unbanned

### Soul Trading
- Use `/soulwithdraw` to convert souls to items
- Right-click soul items to consume them (if they're yours)
- Trade soul items with other players
- Each soul item shows the original owner

## 🎪 Particle Effects

Customizable particle effects for:
- **Soul Loss**: Red dust, smoke, and falling particles
- **Soul Gain**: Happy villager, sonic boom, and cyan dust particles
- **Bedrock Optimization**: Reduced particle count for mobile players

## 🐛 Troubleshooting

### Common Issues
1. **Resource Pack Not Working**: Ensure you're using MC 1.21.8 or earlier
2. **Particles Not Showing**: Check if effects are enabled in config.yml
3. **Souls Not Saving**: Verify file permissions for playerdata.yml
4. **Commands Not Working**: Check permissions and plugin load order

### Debug Commands
- `/soul testparticle both` - Test all particle effects
- `/soul stats <player>` - Check player data integrity
- `/soul reload` - Reload configuration without restart

## 📈 Performance

- **Async Data Saving**: Non-blocking data persistence
- **Efficient Caching**: In-memory player data with periodic saves
- **Optimized Particles**: Reduced effects for Bedrock players
- **Thread-Safe Operations**: Concurrent access handling

## 🤝 Support

- **YouTube**: [TechinpointGamerz](https://www.youtube.com/@TechinpointGamerz)
- **Website**: [techinpointgamerz.netlify.app](https://techinpointgamerz.netlify.app)

## 📝 Version History

### v1.0.3
- Extended resource pack compatibility to MC 1.21.8
- Improved version detection and compatibility checking
- Enhanced CustomModelData handling for newer versions
- Performance optimizations and better error handling

---

**Developed by Prabin (Techinpoint Gamerz)**  
© 2025 - All Rights Reserved