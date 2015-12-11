package com.j0ach1mmall3.ultimatecosmetics.internal.data;

import com.j0ach1mmall3.jlib.storage.database.CallbackHandler;
import com.j0ach1mmall3.jlib.storage.database.mysql.MySQLLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.GadgetStorage;
import com.j0ach1mmall3.ultimatecosmetics.internal.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/11/2015
 */
public final class MySQLDataLoader extends MySQLLoader implements DataLoader {
    private final Map<String, Map<String, Integer>> ammo = new HashMap<>();
    private final String ammoName;
    private final String queueName;
    private final String stackerName;

    public MySQLDataLoader(Main plugin) {
        super(plugin, plugin.getStorage().getDatabaseHost(), plugin.getStorage().getDatabasePort(), plugin.getStorage().getDatabaseDatabase(), plugin.getStorage().getDatabaseUser(), plugin.getStorage().getDatabasePassword());
        this.ammoName = plugin.getStorage().getDatabasePrefix() + "ammo";
        this.queueName = plugin.getStorage().getDatabasePrefix() + "queue";
        this.stackerName = plugin.getStorage().getDatabasePrefix() + "stacker";
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.ammoName + "(Player VARCHAR(36), Enderbow INT(5), EtherealPearl INT(5), PaintballGun INT(5), FlyingPig INT(5), BatBlaster INT(5), CATapult INT(5), RailGun INT(5), CryoTube INT(5), Rocket INT(5), PoopBomb INT(5), GrapplingHook INT(5), SelfDestruct INT(5), SlimeVasion INT(5), FunGun INT(5), MelonThrower INT(5), ColorBomb INT(5), FireTrail INT(5), DiamondShower INT(5), GoldFountain INT(5), PaintTrail INT(5))");
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " + this.queueName + "(Player VARCHAR(36), Balloon VARCHAR(64), Banner VARCHAR(64), Bowtrail VARCHAR(64), Gadget VARCHAR(64), Hat VARCHAR(64), Hearts VARCHAR(64), Morph VARCHAR(64), Mount VARCHAR(64), Music VARCHAR(64), Particles VARCHAR(64), Pet VARCHAR(64), Trail VARCHAR(64), Outfit VARCHAR(64))");
        this.mySQL.execute("CREATE TABLE IF NOT EXISTS " +  this.stackerName + "(Player VARCHAR(36), Enabled TINYINT(1))");
    }

    @Override
    public void disconnectLoader() {
        disconnect();
    }

    @Override
    public void loadAmmo(String uuid) {
        Map<String, Integer> gadgetAmmo = new HashMap<>();
        this.mySQL.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", preparedStatement -> {
            this.mySQL.setString(preparedStatement, 1, uuid);
            this.mySQL.executeQuerry(preparedStatement, resultSet ->  {
                try {
                    if(!resultSet.next()) {
                        for (GadgetStorage gadget : ((Main) this.plugin).getGadgets().getGadgets()) {
                            gadgetAmmo.put(gadget.getIdentifier(), 0);
                        }
                        this.ammo.put(uuid, gadgetAmmo);
                        return;
                    }
                    for (GadgetStorage gadget : ((Main) this.plugin).getGadgets().getGadgets()) {
                        gadgetAmmo.put(gadget.getIdentifier(), resultSet.getInt(gadget.getIdentifier()));
                    }
                    this.ammo.put(uuid, gadgetAmmo);
                } catch (SQLException e) {
                    e.printStackTrace();
                    for (GadgetStorage gadget : ((Main) this.plugin).getGadgets().getGadgets()) {
                        gadgetAmmo.put(gadget.getIdentifier(), 0);
                    }
                    this.ammo.put(uuid, gadgetAmmo);
                }
            });
        });
    }

    @Override
    public void unloadAmmo(String uuid) {
        Map<String, Integer> gadgetAmmo = this.ammo.get(uuid);
        this.ammo.remove(uuid);
        for(Map.Entry<String, Integer> entry : gadgetAmmo.entrySet()) {
            this.mySQL.prepareStatement("UPDATE " + this.ammoName + " SET " + entry.getKey() + "=? WHERE Player=?", preparedStatement -> {
                this.mySQL.setString(preparedStatement, 1, String.valueOf(entry.getValue()));
                this.mySQL.setString(preparedStatement, 2, uuid);
                this.mySQL.execute(preparedStatement);
            });
        }
    }

    @Override
    public int getAmmo(String identifier, String uuid) {
        return this.ammo.get(uuid).get(identifier);
    }

    @Override
    public void giveAmmo(String identifier, String uuid, int amount) {
        if(this.ammo.get(uuid).get(identifier) + amount <= 99999) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) + amount);
    }

    @Override
    public void takeAmmo(String identifier, String uuid, int amount) {
        if(this.ammo.get(uuid).get(identifier) - amount >= 0) setAmmo(identifier, uuid, this.ammo.get(uuid).get(identifier) - amount);
    }

    @Override
    public void createAmmo(String uuid) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.ammoName + " WHERE Player = ?", preparedStatement -> {
            this.mySQL.setString(preparedStatement, 1, uuid);
            this.mySQL.executeQuerry(preparedStatement, resultSet -> {
                try {
                    if(!resultSet.next()) this.mySQL.prepareStatement("INSERT INTO " + this.ammoName + " VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)", preparedStatement1 -> {
                        this.mySQL.setString(preparedStatement1, 1, uuid);
                        this.mySQL.execute(preparedStatement1);
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void setAmmo(String identifier, String uuid, int amount) {
        this.ammo.get(uuid).put(identifier, amount);
        }

    @Override
    public void giveBackQueue(Player p) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.queueName + " WHERE Player = ?", preparedStatement -> {
            this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
            this.mySQL.executeQuerry(preparedStatement, resultSet ->  {
                try {
                    if(resultSet.next()) {
                        Bukkit.getScheduler().callSyncMethod(this.plugin, (Callable<Void>) () -> {
                            new CosmeticsQueue((Main) this.plugin, Arrays.asList(resultSet.getString("Balloon"), resultSet.getString("Banner"), resultSet.getString("Bowtrail"), resultSet.getString("Gadget"), resultSet.getString("Hat"), resultSet.getString("Hearts"), resultSet.getString("Morph"), resultSet.getString("Mount"), resultSet.getString("Music"), resultSet.getString("Particles"), resultSet.getString("Pet"), resultSet.getString("Trail"), resultSet.getString("Outfit"))).give(p);
                            return null;
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void updateQueue(Player p, CosmeticsQueue queue) {
        Methods.removeCosmetics(p, (Main) this.plugin);
        List<String> list = queue.asList();
        this.mySQL.prepareStatement("UPDATE " + this.queueName + " SET Balloon=?, Banner=?, Bowtrail=?, Gadget=?, Hat=?, Hearts=?, Morph=?, Mount=?, Music=?, Particles=?, Pet=?, Trail=?, Outfit=? WHERE Player=?", preparedStatement -> {
            for(int i=0;i<12;i++) {
                this.mySQL.setString(preparedStatement, i+1, list.get(i));
            }
            this.mySQL.setString(preparedStatement, 14, p.getUniqueId().toString());
            this.mySQL.execute(preparedStatement);
        });
    }

    @Override
    public void createQueue(Player p) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.queueName + " WHERE Player = ?", preparedStatement -> {
            this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
            this.mySQL.executeQuerry(preparedStatement, resultSet ->  {
                try {
                    if(!resultSet.next()) this.mySQL.prepareStatement("INSERT INTO " + this.queueName + " VALUES(?, '', '', '', '', '', '', '', '', '', '', '', '', '')", preparedStatement1 -> {
                        this.mySQL.setString(preparedStatement1, 1, p.getUniqueId().toString());
                        this.mySQL.execute(preparedStatement1);
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void getStacker(Player p, CallbackHandler<Boolean> callbackHandler) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.stackerName + " WHERE Player = ?", preparedStatement -> {
            this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
            this.mySQL.executeQuerry(preparedStatement, resultSet ->  {
                try {
                    resultSet.next();
                    callbackHandler.callback(resultSet.getBoolean("Enabled"));
                } catch (SQLException e) {
                    e.printStackTrace();
                    callbackHandler.callback(false);
                }
            });
        });

    }

    @Override
    public void setStacker(Player p, boolean stacker) {
        this.mySQL.prepareStatement("UPDATE " + this.stackerName + " SET Enabled=? WHERE Player=?", preparedStatement -> {
            this.mySQL.setBoolean(preparedStatement, 1, stacker);
            this.mySQL.setString(preparedStatement, 2, p.getUniqueId().toString());
            this.mySQL.execute(preparedStatement);
        });
    }

    @Override
    public void createStacker(Player p) {
        this.mySQL.prepareStatement("SELECT * FROM " + this.stackerName + " WHERE Player = ?", preparedStatement -> {
            this.mySQL.setString(preparedStatement, 1, p.getUniqueId().toString());
            this.mySQL.executeQuerry(preparedStatement, resultSet -> {
                try {
                    if(!resultSet.next()) this.mySQL.prepareStatement("INSERT INTO " + this.stackerName + " VALUES(?, 1)", preparedStatement1 -> {
                        this.mySQL.setString(preparedStatement1, 1, p.getUniqueId().toString());
                        this.mySQL.execute(preparedStatement1);
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
