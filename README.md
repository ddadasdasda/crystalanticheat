# CrystalAntiCheat - Paper 1.21.11 Plugin

A comprehensive anticheat plugin for Paper Minecraft servers designed to detect and prevent crystal PvP, sword PvP, hitbox manipulation, auto-totem, mace exploits, and movement cheats.

## Features

✅ **Crystal PvP Detection**
- Crystal spam placement detection
- Crystal exploit and damage avoidance detection
- Artificial resistance prevention

✅ **Sword PvP Detection**
- Inhuman click speed detection
- Critical hit abuse prevention
- Reach exploit detection

✅ **Hitbox Manipulation**
- Impossible hit detection
- Behind-player hit prevention
- Height difference exploit detection

✅ **Auto-Totem Detection**
- Inhuman totem pop speed detection
- Automated totem behavior prevention

✅ **Mace Exploit Prevention**
- Mace spam detection
- Damage modification prevention
- Fall damage exploitation detection

✅ **Movement Exploit Prevention**
- Speed hacking detection
- Flight hacking detection
- Glitch walking prevention

✅ **Additional Features**
- Knockback exploit detection
- Customizable violation thresholds
- Player violation tracking and history
- Admin commands and notifications

## Installation

### Requirements
- Paper 1.21.11 or later
- Java 11 or later
- Maven (for building)

### Build Instructions

1. Clone or download this plugin
2. Navigate to the plugin directory
3. Build using Maven:
```bash
mvn clean package
```

4. The compiled JAR will be in `target/CrystalAntiCheat-1.0.0.jar`
5. Place the JAR in your server's `plugins/` folder
6. Restart your server

## Configuration

Edit `plugins/CrystalAntiCheat/config.yml` to customize:
- Enable/disable specific cheat detections
- Set violation thresholds for warnings and bans
- Configure punishment types (BAN, KICK, WARN, LOG)

## Commands

### Player Information
```
/acinfo <player>     - View player's violation history and status
```

### Warning System
```
/acwarn <player> <reason>   - Manually warn a player
/acban <player> <reason>    - Manually ban a player
```

### Admin Commands
```
/anticheat status   - Show anticheat status
/anticheat reload   - Reload configuration
/anticheat help     - Show help menu
/anticheat version  - Show plugin version
```

## Permissions

- `anticheat.admin` - Access to all admin commands
- `anticheat.warn` - Use `/acwarn` command
- `anticheat.ban` - Use `/acban` command
- `anticheat.notify` - Receive anticheat notifications
- `anticheat.bypass` - Bypass speed checks (recommended for admins only)

## Violation Tracking

The plugin automatically tracks player violations:
- **3+ Violations**: Player receives warning
- **5+ Violations**: Player can be banned
- All violations are logged with timestamps
- Admin can view violation history with `/acinfo`

## Configuration Example

```yaml
anticheat:
  enabled: true
  
  crystal-check:
    enabled: true
  
  sword-pvp-check:
    enabled: true
  
  hitbox-check:
    enabled: true
  
  auto-totem-check:
    enabled: true
  
  mace-check:
    enabled: true
  
  movement-check:
    enabled: true
  
  violation-threshold:
    warn: 3
    ban: 5
  
  punishment-type: BAN
```

## Detection Methods

### Crystal Detection
- Monitors crystal placement frequency
- Detects abnormal damage absorption
- Flags artificial resistance patterns

### Sword PvP Detection
- Tracks click frequency (CPS detection)
- Monitors critical hit damage values
- Detects reach exploits beyond normal range

### Hitbox Detection
- Monitors hit angles and positions
- Prevents behind-player hits
- Detects unnatural height differences

### Auto-Totem Detection
- Tracks totem consumption speed
- Detects automated totem behavior
- Monitors fire resistance potion abuse

### Mace Detection
- Monitors swing frequency
- Tracks damage modifications
- Detects fall damage exploitation

### Movement Detection
- Monitors velocity changes
- Detects speed hacking
- Identifies flight hacking attempts

## Troubleshooting

### Plugin won't load
- Ensure you have Paper 1.21.11 or later
- Check that Java 11+ is installed
- Look for errors in `logs/latest.log`

### False positives
- Adjust violation thresholds in config.yml
- Give legitimate players `anticheat.bypass` permission if needed
- Review violation history with `/acinfo`

### Performance issues
- Disable unused detection modules in config.yml
- Check your server's tick rate
- Monitor with `/anticheat status`

## Support

For issues or suggestions:
1. Check the configuration file
2. Review violation history with `/acinfo`
3. Check server logs for error messages

## License

This plugin is provided as-is for use on Paper servers.

## Credits

CrystalAntiCheat - Advanced Protection for PvP Servers
