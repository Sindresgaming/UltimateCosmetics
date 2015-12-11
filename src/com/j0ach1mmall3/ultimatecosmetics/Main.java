package com.j0ach1mmall3.ultimatecosmetics;

import com.j0ach1mmall3.jlib.integration.MetricsLite;
import com.j0ach1mmall3.jlib.integration.Placeholders;
import com.j0ach1mmall3.jlib.integration.updatechecker.AsyncUpdateChecker;
import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.methods.Notes;
import com.j0ach1mmall3.jlib.methods.ReflectionAPI;
import com.j0ach1mmall3.ultimatecosmetics.api.CosmeticsAPI;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import com.j0ach1mmall3.ultimatecosmetics.internal.balloons.BalloonsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.banners.BannersListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.bowtrails.BowtrailsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.commands.Commands;
import com.j0ach1mmall3.ultimatecosmetics.internal.config.*;
import com.j0ach1mmall3.ultimatecosmetics.internal.data.*;
import com.j0ach1mmall3.ultimatecosmetics.internal.gadgets.GadgetsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.gui.*;
import com.j0ach1mmall3.ultimatecosmetics.internal.hats.HatsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.hearts.HeartsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.listeners.*;
import com.j0ach1mmall3.ultimatecosmetics.internal.morphs.MorphsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.mounts.MountsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.music.MusicListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.particles.ParticlesTask;
import com.j0ach1mmall3.ultimatecosmetics.internal.pets.PetsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.trails.TrailsListener;
import com.j0ach1mmall3.ultimatecosmetics.internal.wardrobe.WardrobeListener;
import com.xxmicloxx.noteblockapi.NoteBlockPlayerMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * Created by j0ach1mmall3 on 20:26 17/08/2015 using IntelliJ IDEA.
 */
public final class Main extends JavaPlugin {
    public static final String BUKKIT_VERSION = Bukkit.getBukkitVersion().split("\\-")[0];
    public static final String MINECRAFT_VERSION = ReflectionAPI.getNmsVersion();
    private boolean libsDisguises;
    private boolean protocolLib;
    private CosmeticsAPI api = null;
    private Config config = null;
    private Lang lang = null;
    private Storage storage = null;
    private DataLoader dataLoader = null;
    private Misc misc = null;
    private Pagination pagination = null;
    private Balloons balloons = null;
    private Banners banners = null;
    private Bowtrails bowtrails = null;
    private Morphs morphs = null;
    private Fireworks fireworks = null;
    private Gadgets gadgets = null;
    private Hats hats = null;
    private Hearts hearts = null;
    private Mounts mounts = null;
    private Music music = null;
    private Particles particles = null;
    private Pets pets = null;
    private Trails trails = null;
    private Wardrobe wardrobe = null;
    private GadgetsListener gadgetsListener = null;
    private MorphsListener morphsListener = null;
    private TrailsListener trailsListener = null;

    @Override
    public void onEnable() {
        this.config = new Config(this);
        this.api = new CosmeticsAPI(this);
        if (this.config.getLoggingLevel() >= 2)
            General.sendColoredMessage(this, "You are running Bukkit version " + BUKKIT_VERSION + " (MC " + MINECRAFT_VERSION + ')', ChatColor.GOLD);
        if (this.config.isUpdateChecker()) {
            AsyncUpdateChecker checker = new AsyncUpdateChecker(this, 5885, this.getDescription().getVersion());
            checker.checkUpdate(updateCheckerResult -> {
                switch (updateCheckerResult.getType()) {
                    case NEW_UPDATE:
                        General.sendColoredMessage(this, "A new update is available!", ChatColor.GOLD);
                        General.sendColoredMessage(this, "Version " + updateCheckerResult.getNewVersion() + " (Current: " + this.getDescription().getVersion() + ')', ChatColor.GOLD);
                        break;
                    case UP_TO_DATE:
                        General.sendColoredMessage(this, "You are up to date!", ChatColor.GREEN);
                        break;
                    case ERROR:
                        General.sendColoredMessage(this, "An error occured while trying to check for updates on spigotmc.org!", ChatColor.RED);
                        break;
                }
            });
        }
        try {
            MetricsLite metricsLite = new MetricsLite(this);
            metricsLite.start();
        } catch (IOException e) {
            General.sendColoredMessage(this, "An error occured while starting MetricsLite!", ChatColor.RED);
            e.printStackTrace();
        }
        PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("LibsDisguises") != null) {
            this.libsDisguises = true;
            if (this.config.getLoggingLevel() >= 1)
                General.sendColoredMessage(this, "Successfully hooked into LibsDisguises for Morphs", ChatColor.GREEN);
        } else {
            if (this.config.getLoggingLevel() >= 1)
                General.sendColoredMessage(this, "LibsDisguises not found, Morphs will not work!", ChatColor.GOLD);
        }
        if (pm.getPlugin("ProtocolLib") != null) {
            this.protocolLib = true;
            if (this.config.getLoggingLevel() >= 1)
                General.sendColoredMessage(this, "Switching over to ProtocolLib", ChatColor.GREEN);
        } else {
            if (this.config.getLoggingLevel() >= 1)
                General.sendColoredMessage(this, "ProtocolLib not found, falling back to TinyProtocol", ChatColor.GOLD);
        }
        if (this.config.getLoggingLevel() >= 1) General.sendColoredMessage(this, "Registering Commands...", ChatColor.GREEN);
        new Commands(this);
        new NoteBlockPlayerMain(this);
        if (this.config.getLoggingLevel() >= 1) General.sendColoredMessage(this, "Loading Configs...", ChatColor.GREEN);
        this.lang = new Lang(this);
        this.storage = new Storage(this);
        switch (this.storage.getType()) {
            case FILE:
                this.dataLoader = new FileDataLoader(this);
                break;
            case MYSQL:
                this.dataLoader = new MySQLDataLoader(this);
                break;
            case SQLITE:
                this.dataLoader = new SQLiteDataLoader(this);
                break;
            case MONGODB:
                this.dataLoader = new MongoDBDataLoader(this);
                break;
            case REDIS:
                General.sendColoredMessage(this, "Redis Storage is deprecated as of version 3.6.0! Switching back to File Storage!", ChatColor.RED);
                this.dataLoader = new FileDataLoader(this);
                break;
        }
        this.misc = new Misc(this);
        this.pagination = new Pagination(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Balloons...", ChatColor.GREEN);
        this.balloons = new Balloons(this);
        new BalloonsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Banners...", ChatColor.GREEN);
        this.banners = new Banners(this);
        new BannersListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Bowtrails...", ChatColor.GREEN);
        this.bowtrails = new Bowtrails(this);
        new BowtrailsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Morphs...", ChatColor.GREEN);
        this.morphs = new Morphs(this);
        this.morphsListener = new MorphsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Fireworks...", ChatColor.GREEN);
        this.fireworks = new Fireworks(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Gadgets...", ChatColor.GREEN);
        this.gadgets = new Gadgets(this);
        this.gadgetsListener = new GadgetsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Hats...", ChatColor.GREEN);
        this.hats = new Hats(this);
        new HatsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Hearts...", ChatColor.GREEN);
        this.hearts = new Hearts(this);
        new HeartsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Mounts...", ChatColor.GREEN);
        this.mounts = new Mounts(this);
        new MountsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Music...", ChatColor.GREEN);
        this.music = new Music(this);
        new MusicListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Particles...", ChatColor.GREEN);
        this.particles = new Particles(this);
        new ParticlesTask(this).runTaskTimerAsynchronously(this, (long) (this.particles.getUpdateInterval() * 20.0), (long) (this.particles.getUpdateInterval() * 20.0));
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Pets...", ChatColor.GREEN);
        this.pets = new Pets(this);
        new PetsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Trails...", ChatColor.GREEN);
        this.trails = new Trails(this);
        this.trailsListener = new TrailsListener(this);
        if (this.config.getLoggingLevel() >= 2) General.sendColoredMessage(this, "Enabling Wardrobe...", ChatColor.GREEN);
        this.wardrobe = new Wardrobe(this);
        new WardrobeListener(this);
        if (this.config.getLoggingLevel() >= 1)
            General.sendColoredMessage(this, "Registering Listeners...", ChatColor.GREEN);
        new CommandsListener(this);
        new PlayerListener(this);
        new EntityListener(this);
        if (this.protocolLib) {
            new ProtocolLibListener(this);
        } else if(!BUKKIT_VERSION.startsWith("1.7")) {
            try {
                new ProtocolListener(this);
            } catch (RuntimeException e) {
                if (this.config.getLoggingLevel() >= 1)
                    General.sendColoredMessage(this, "Failed to set up TinyProtocol, disabling Bat sounds will NOT work!", ChatColor.RED);
                if (this.config.getLoggingLevel() >= 2) e.printStackTrace();
            }
        }
        if (this.config.getLoggingLevel() >= 1) General.sendColoredMessage(this, "Registering Guis...", ChatColor.GREEN);
        new CosmeticsGuiHandler(this);
        new BalloonsGuiHandler(this);
        new BannersGuiHandler(this);
        new BowtrailsGuiHandler(this);
        new FireworksGuiHandler(this);
        new GadgetsGuiHandler(this);
        new HatsGuiHandler(this);
        new HeartsGuiHandler(this);
        new MorphsGuiHandler(this);
        new MountsGuiHandler(this);
        new MusicGuiHandler(this);
        new ParticlesGuiHandler(this);
        new PetsGuiHandler(this);
        new TrailsGuiHandler(this);
        new WardrobeGuiHandler(this);
        if (this.config.getLoggingLevel() >= 1) General.sendColoredMessage(this, "Done!", ChatColor.GREEN);
    }

    @Override
    public void onDisable() {
        Methods.cleanup(this);
    }

    public void reload() {
        Methods.cleanup(this);
        this.lang = new Lang(this);
        this.misc = new Misc(this);
        this.balloons = new Balloons(this);
        this.banners = new Banners(this);
        this.morphs = new Morphs(this);
        this.fireworks = new Fireworks(this);
        this.gadgets = new Gadgets(this);
        this.hats = new Hats(this);
        this.hearts = new Hearts(this);
        this.mounts = new Mounts(this);
        this.music = new Music(this);
        this.particles = new Particles(this);
        this.pets = new Pets(this);
        this.trails = new Trails(this);
        this.wardrobe = new Wardrobe(this);
    }

    public void informPlayerNoPermission(CommandSender s, String message) {
        if (s instanceof Player) {
            Player p = (Player) s;
            p.sendMessage(Placeholders.parse(message, p));
            Notes.playNote(p, Instrument.BASS_DRUM, Note.Tone.A);
        } else s.sendMessage(Placeholders.parse(this.lang.getNotEnabled(), null));
    }

    public void informPlayerNotEnabled(CommandSender s) {
        if (s instanceof Player) {
            Player p = (Player) s;
            p.sendMessage(Placeholders.parse(this.lang.getNotEnabled(), p));
            Notes.playNote(p, Instrument.BASS_DRUM, Note.Tone.A);
        } else s.sendMessage(Placeholders.parse(this.lang.getNotEnabled(), null));
    }

    public boolean isLibsDisguises() {
        return this.libsDisguises;
    }

    public CosmeticsAPI getApi() {
        return this.api;
    }

    public Lang getLang() {
        return this.lang;
    }

    public Misc getMisc() {
        return this.misc;
    }

    public Balloons getBalloons() {
        return this.balloons;
    }

    public Banners getBanners() {
        return this.banners;
    }

    public Config getBabies() {
        return this.config;
    }

    public Morphs getMorphs() {
        return this.morphs;
    }

    public Fireworks getFireworks() {
        return this.fireworks;
    }

    public Gadgets getGadgets() {
        return this.gadgets;
    }

    public Hats getHats() {
        return this.hats;
    }

    public GadgetsListener getGadgetsListener() {
        return this.gadgetsListener;
    }

    public Hearts getHearts() {
        return this.hearts;
    }

    public Mounts getMounts() {
        return this.mounts;
    }

    public Music getMusic() {
        return this.music;
    }

    public Particles getParticles() {
        return this.particles;
    }

    public Pets getPets() {
        return this.pets;
    }

    public Trails getTrails() {
        return this.trails;
    }

    public TrailsListener getTrailsListener() {
        return this.trailsListener;
    }

    public Wardrobe getWardrobe() {
        return this.wardrobe;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public MorphsListener getMorphsListener() {
        return this.morphsListener;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public Bowtrails getBowtrails() {
        return this.bowtrails;
    }

    public DataLoader getDataLoader() {
        return this.dataLoader;
    }
}
