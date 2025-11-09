# WLag - Server Lag Reduction Plugin

[![Java](https://img.shields.io/badge/Java-11%2B-orange?style=for-the-badge&logo=java)](https://www.java.com/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.16%2B-green?style=for-the-badge&logo=minecraft)](https://www.minecraft.net/)
[![Spigot API](https://img.shields.io/badge/Spigot-API-orange?style=for-the-badge)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)](https://github.com/vados2343/WLag)

---

## 📋 Overview

**WLag** is a comprehensive Minecraft server optimization and lag-reduction plugin designed for Bukkit/Spigot servers. It provides real-time TPS (Ticks Per Second) monitoring, automatic entity clearing, dynamic mob AI management, and administrator controls to maintain optimal server performance during high-load situations.

The plugin intelligently detects server lag patterns and responds with automated performance optimization while giving administrators granular control over clearing behaviors through both GUI and command-line interfaces.

---

## ✨ Key Features

### 🎯 Real-Time TPS Monitoring
- Continuous server performance tracking with multiple monitoring methods
- Ring buffer-based TPS calculation (built-in)
- Optional PlaceholderAPI integration for enhanced accuracy
- Live TPS display for administrators

### 🧹 Automatic Entity Clearing
- Scheduled removal of lag-causing dropped items
- Selective mob clearing when TPS is critically low
- Configurable clear intervals and exclusion lists
- Smart clearing that respects excluded items/mobs
- Manual clear command for immediate execution

### ⚠️ Progressive Warning System
- **Last-minute warnings** before automatic clearing
- Boss bar countdown display
- Action bar messages
- Optional visual effects:
  - Lightning strikes (Thunder)
  - Particle effects (Flame, Smoke, Spark)
  - Sound effects (Levelup)
  - Entity glowing
  - Fireworks
  - Rainbow-colored chat messages
  - Fire effects

### 🤖 Dynamic Mob AI Management
- Automatically disables mob AI when server TPS is critically low
- Mobs become stationary but remain visible
- AI re-enables when server recovers
- Reduces computational load without removing entities

### 🚫 Spawn Prevention
- Prevents new creature spawning during critical lag situations
- Configurable TPS threshold for spawn blocking
- Prevents lag accumulation during server crises

### 📊 Admin Commands
- `/wlagg check` - Display current TPS
- `/wlagg clear` - Manually clear entities immediately
- `/wlagg reload` - Reload configuration
- `/wlagg status` - Show loaded chunks and entity count
- `/wlagg optimize` - Unload empty chunks
- `/wlagg gui` - Open interactive admin menu
- `/wlagg help` - Display help information

### 🖱️ Interactive Admin GUI
- Visual menu interface for configuration
- Easy access to common functions
- Effects and warning customization submenu
- Chat-based value input for intervals and messages

### 🌐 Multi-Language Support
- English and Russian language files
- Automatic fallback system
- Configurable language selection
- Easy to extend with additional languages

### 🔧 Advanced Configuration
- Highly customizable via YAML config file
- Exclude specific items from clearing
- Exclude specific mob types from removal
- Configure separate TPS thresholds for different actions
- Customize all warning effects independently
- Chat message prefix and broadcast customization

### 🔗 Optional Integrations
- **PlaceholderAPI** - For enhanced TPS reading
- **Spartan Anti-Cheat** - Automatic configuration reloading

---

## 📦 Requirements

- **Java 11 or higher**
- **Minecraft Server**: Spigot 1.16.5 or newer
- **Bukkit/Spigot API**: Compatible with modern server implementations

### Optional Dependencies
- **PlaceholderAPI** - For advanced TPS integration (recommended)
- **Spartan** - For anti-cheat integration (optional)

---

## 🚀 Installation

1. **Download** the WLag plugin JAR file
2. **Place** it in your server's `plugins/` directory
3. **Restart** your Minecraft server
4. **Configure** the plugin by editing `plugins/WLag/config.yml`
5. **Grant permissions** to administrators: `wlagg.admin`

### Automatic Setup
On first startup, the plugin will:
- Create the configuration directory (`plugins/WLag/`)
- Generate default `config.yml`
- Extract language files (English and Russian)
- Display banner confirming successful load

---

## ⚙️ Configuration

The plugin is configured through `plugins/WLag/config.yml`. Here are the main configuration sections:

### Core Settings
```yaml
settings:
  # Interval for automatic clearing (examples: "30m", "5m", "1h", "300" for seconds)
  clear-interval: "30m"

  # TPS threshold for disabling mob AI (prevents lag)
  ai-threshold: 10.0

  # TPS threshold for preventing new creature spawning
  disable-spawn-threshold: 10.0

  # TPS threshold for removing mobs during clearing
  tps-threshold: 15.0

  # Enable logging of clearing actions
  log-clear-actions: true
```

### Warning System
```yaml
warnings:
  # Show boss bar countdown before clearing
  use-bossbar: true

  # Show action bar countdown messages
  use-actionbar: true

  # Enable lightning strikes as visual warning
  thunder-on-warning: true

  # Number of seconds before clear to show warnings (max 60)
  bossbar-last-seconds: 60
```

### Visual Effects
```yaml
effects:
  # Enable each effect individually
  particles: true      # Flame particle effects
  sound: true          # Levelup sound effects
  glowing: true        # Entity glowing during warning
  firework: true       # Firework displays
  smoke: true          # Smoke particles
  spark: true          # Spark particles
  rainbow: true        # Rainbow-colored messages
  fire: true           # Fire effect on players
  shine: true          # Shine effect messages
```

### Entity Exclusions
```yaml
# Items that should NOT be cleared
excluded-items:
  - "DIAMOND"
  - "EMERALD"
  - "GOLDEN_APPLE"

# Mobs that should NOT be removed
excluded-mobs:
  - "ARMOR_STAND"
  - "WITHER"
```

### PlaceholderAPI Integration (Optional)
```yaml
use-placeholderapi-for-tps: false
placeholderapi-tps-placeholder: "%tps%"
```

### Language Settings
```yaml
global-messages:
  language: "English"  # Options: "English", "Russian"
  enabled: true
  broadcast-prefix: "&8[&6WLag&8]&r"
```

---

## 🎮 Usage Guide

### For Server Administrators

#### Checking Server Health
```
/wlagg check          # View current TPS and server performance
/wlagg status         # View loaded chunks and entity statistics
```

#### Manual Operations
```
/wlagg clear          # Immediately clear all lag-causing entities
/wlagg optimize       # Unload chunks with no nearby players
/wlagg reload         # Reload configuration from file
```

#### Configuration
```
/wlagg gui            # Open interactive configuration menu
```

#### Help
```
/wlagg help           # Display available commands
```

### Automatic Clearing Process

1. **Countdown Phase**: Plugin counts down to scheduled clear time
2. **Warning Phase**: During last 60 seconds (configurable):
   - Boss bar appears showing countdown
   - Action bar displays warning messages
   - Thunder effects strike periodically
   - Particles and effects display around entities
3. **Clearing Phase**: At scheduled time:
   - All dropped items are removed
   - Mobs are removed if current TPS < tps-threshold
   - Excluded items and mobs are preserved
   - Log messages are recorded (optional)
4. **Reset**: Countdown timer resets for next scheduled clear

### Manual Clear Behavior
When using `/wlagg clear`:
- All dropped items are immediately removed
- Mobs are removed only if current TPS < tps-threshold
- Exclusion lists are respected
- No warning phase occurs

---

## 🎨 Effects Customization

The plugin supports 9 different warning effect types that can be enabled/disabled individually:

| Effect | Description |
|--------|-------------|
| **Particles** | Flame-colored particles around dropping items |
| **Sound** | Levelup sound effect for audio warning |
| **Glowing** | Mobs glow with bright visibility |
| **Firework** | Fireworks display around warning areas |
| **Smoke** | Smoke particle effects |
| **Spark** | Spark particle effects |
| **Rainbow** | Rainbow-colored warning messages |
| **Fire** | Temporary fire effect (brief, safe) |
| **Shine** | Shine/luster effect in chat messages |

Access effect settings via `/wlagg gui` → Effects Menu

---

## 📊 Performance Impact

WLag is designed for minimal performance overhead:
- **TPS Monitoring**: Negligible impact (uses ring buffer)
- **Auto-clearing**: Runs on scheduled intervals, not continuous
- **Mob AI Toggle**: Reduces load, doesn't add it
- **Warning Effects**: Configurable, can be disabled for minimal impact

### Recommended Settings for Different Server Sizes

**Small Servers (< 20 players)**
```yaml
clear-interval: "1h"
ai-threshold: 8.0
tps-threshold: 18.0
```

**Medium Servers (20-100 players)**
```yaml
clear-interval: "30m"
ai-threshold: 10.0
tps-threshold: 15.0
```

**Large Servers (> 100 players)**
```yaml
clear-interval: "10m"
ai-threshold: 15.0
tps-threshold: 15.0
disable-spawn-threshold: 12.0
```

---

## 🔧 Troubleshooting

### Plugin Not Loading
- Check Java version (requires 11+)
- Verify plugin JAR is in `plugins/` directory
- Check server logs for error messages
- Ensure Spigot/Bukkit version compatibility

### TPS Not Being Tracked Correctly
- Verify PlaceholderAPI is installed if using PAPI integration
- Check if PlaceholderAPI placeholder is correct
- Switch to ring buffer method (default)
- Review server logs for errors

### Effects Not Showing
- Verify effects are enabled in `config.yml`
- Check if players have proper rendering settings
- Ensure client-side particle rendering is enabled
- Review effect threshold settings

### Clearing Not Happening
- Verify scheduled time is in correct format ("30m", "5m", etc.)
- Check if server TPS is consistently above tps-threshold
- Review exclusion lists (items/mobs may be excluded)
- Check plugin logs for warnings or errors

### Mob AI Issues
- Verify ai-threshold is set appropriately
- Check if PlaceholderAPI is interfering
- Ensure no conflicting plugins are managing mob AI
- Review TPS thresholds for proper values

---

## 📝 Command Permissions

| Command | Permission | Description |
|---------|-----------|-------------|
| All commands | `wlagg.admin` | Administrative access to WLag commands |
| `/wlagg gui` | `wlagg.admin` | Access GUI interface |
| `/wlagg clear` | `wlagg.admin` | Trigger manual clearing |
| `/wlagg reload` | `wlagg.admin` | Reload configuration |

### Permission Configuration (PermissionsEx Example)
```yaml
groups:
  admin:
    permissions:
      - wlagg.admin
```

---

## 🌐 Supported Languages

- **English** - Full support
- **Russian** - Full support
- **Extensible** - Easy to add more languages

Language files are located in `plugins/WLag/lang/`

---

## 🔄 Integration with Other Plugins

### PlaceholderAPI
If PlaceholderAPI is installed, WLag can use its TPS placeholder for more accurate readings:
```yaml
use-placeholderapi-for-tps: true
placeholderapi-tps-placeholder: "%tps%"
```

### Spartan Anti-Cheat
The plugin can trigger Spartan configuration reload:
```bash
/wlagg reload
```

---

## 📈 Monitoring & Metrics

Track plugin performance with:
- `/wlagg status` - Current entity and chunk statistics
- `/wlagg check` - Current TPS reading
- Server logs - Detailed clearing operations (if enabled)

### Log Output Example
```
[WLag] Clearing started: Removing 150 items and 45 mobs
[WLag] Clear completed in 125ms
[WLag] Server TPS recovered to 18.5
```

---

## 🐛 Known Limitations

- Clearing disabled items requires them to be in exclusion list
- Mob AI toggle affects all non-player entities
- Warning effects are client-side dependent
- PlaceholderAPI integration relies on PAPI accuracy

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 🤝 Support & Contributions

For issues, feature requests, or contributions:
- Report bugs via GitHub Issues
- Submit feature requests
- Create pull requests with improvements

---

**Developed & engineered by Vados2343**
