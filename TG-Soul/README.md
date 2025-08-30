# TG-Soul Plugin

A comprehensive soul system plugin for Minecraft servers running Paper 1.20-1.21.

## Features

- **Soul System**: Players start with configurable souls and lose them on death
- **Revival Tokens**: Craft special tokens to revive banned players
- **HUD Display**: Action bar and boss bar soul indicators
- **CustomModelData**: Support for resource pack soul variants
- **GeyserMC Compatible**: Works with Bedrock Edition players
- **Particle Effects**: Visual feedback for soul gain/loss
- **Admin Commands**: Full administrative control over player souls

## Installation

1. Download the latest release from the [Releases](https://github.com/yourusername/TG-Soul/releases) page
2. Place the `TGSoul-x.x.x.jar` file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin by editing `plugins/TGSoul/config.yml`

## Commands

### Player Commands
- `/soul` - Check your soul count
- `/soul help` - Show help menu
- `/soul recipe` - View Revival Token recipe
- `/soul top [limit]` - View top players by souls
- `/soul stats [player]` - View player statistics
- `/soulwithdraw` - Withdraw a soul as an item

### Admin Commands
- `/soul give <player> <amount>` - Give souls to a player
- `/soul take <player> <amount>` - Take souls from a player
- `/soul set <player> <amount>` - Set player's souls
- `/soul get <player>` - Check player's souls
- `/soul unban <player>` - Unban and restore a player
- `/soul reload` - Reload configuration
- `/soul pack <player> <1-10>` - Set player's soul CustomModelData
- `/soul testparticle <lose/gain/both>` - Test particle effects

## Permissions

- `tgsoul.use` - Basic soul commands (default: true)
- `tgsoul.withdraw` - Withdraw souls as items (default: true)
- `tgsoul.admin` - Admin soul commands (default: op)

## Configuration

The plugin is highly configurable through `config.yml`. Key settings include:

- **Soul Settings**: Starting souls, maximum souls, materials
- **Ban Modes**: Permanent, temporary, or spectator mode
- **HUD Display**: Action bar, boss bar, or both
- **Particle Effects**: Customizable visual effects
- **Revival System**: Revival token recipes and materials

## Building

1. Clone this repository
2. Run `mvn clean package`
3. The compiled JAR will be in the `target` folder

## Requirements

- Java 21+
- Paper 1.20+ or Paper 1.21+
- Optional: GeyserMC for Bedrock support

## Support

- **YouTube**: [Techinpoint Gamerz](https://www.youtube.com/@TechinpointGamerz)
- **Website**: [techinpointgamerz.netlify.app](https://techinpointgamerz.netlify.app)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## Changelog

### Version 1.0.1
- Fixed boss bar and action bar HUD display issues
- Improved soul bar management and lifecycle
- Enhanced error handling for HUD components
- Better integration with player join/death events