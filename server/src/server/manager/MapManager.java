package server.manager;

import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.minlog.Log;
import position.WorldPos;
import server.core.Server;
import server.map.MapGenerator;
import shared.map.AutoTiler;
import shared.map.model.MapDescriptor;
import shared.network.notifications.EntityUpdate;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.artemis.E.E;
import static server.utils.WorldUtils.WorldUtils;

/**
 * Logic regarding maps, contains information about entities in each map, and how are they related.
 */
public class MapManager extends DefaultManager {

    public static final int MAP_COUNT = 1; // TODO set to 1 to load faster
    public static final int MAX_DISTANCE = 15;
    private static Map<Integer, Set<Integer>> nearEntities = new ConcurrentHashMap<>();
    private static Map<Integer, Set<Integer>> entitiesByMap = new ConcurrentHashMap<>();

    private static HashMap<Integer, shared.model.map.Map> maps = new HashMap<>();
    public int mapEntity;

    public MapManager(Server server) {
        super(server);
    }

    @Override
    public void init() {
        String path = "map/tileset.json";
        MapDescriptor map = AutoTiler.load(50, 50, Gdx.files.internal(path));
        generateMapEntity(map, path);
    }

    public void generateMapEntity(MapDescriptor descriptor, String path) {
        int[][] tiles = MapGenerator.generateMap(descriptor);
        mapEntity = getServer().getWorld().create();

        E map = E(mapEntity).map();
        map.mapTiles(tiles);
        map.mapPath(path);
        map.mapHeight(descriptor.getMapHeight());
        map.mapWidth(descriptor.getMapWidth());
    }

    /**
     * @param entityId
     * @return a set of near entities or empty
     */
    public Set<Integer> getNearEntities(int entityId) {
        return nearEntities.getOrDefault(entityId, ConcurrentHashMap.newKeySet());
    }

    /**
     * @param map number
     * @return a set of entities in current map
     */
    public Set<Integer> getEntitiesInMap(int map) {
        return entitiesByMap.get(map);
    }


    /**
     * Move entity to current position, leaving old relations if goes out of range
     * @param player player id
     * @param previusPos previus position in case its moving, empty if is a new position
     */
    public void movePlayer(int player, Optional<WorldPos> previusPos) {
        WorldPos actualPos = E(player).getWorldPos();
        previusPos.ifPresent(it -> {
            if (it.equals(actualPos)) {
                return;
            }
            if (it.map != actualPos.map) {
                getEntitiesInMap(it.map).remove(player);
            }
            if (nearEntities.containsKey(player)) {
                Set<Integer> near = new HashSet<>(nearEntities.get(player));
                near.forEach(nearEntity -> {
                    removeNearEntity(player, nearEntity);
                });
            }
        });
        updateEntity(player);
    }

    /**
     * Remove entity from map and unlink near entities
     * @param entity
     */
    public void removeEntity(int entity) {
        int map = E(entity).getWorldPos().map;
        // remove from near entities
        nearEntities.computeIfPresent(entity, (player, removeFrom) -> {
            removeFrom.forEach(nearEntity -> {
                unlinkEntities(nearEntity, entity);
            });
            return null;
        });
        entitiesByMap.get(map).remove(entity);
    }

    /**
     * Add entity to map and calculate near entities
     * @param player
     */
    public void updateEntity(int player) {
        int map = E(player).getWorldPos().map;
        Set<Integer> entities = entitiesByMap.computeIfAbsent(map, (it) -> new HashSet<>());
        entities.add(player);
        entities.stream()
                .filter(entity -> entity != player)
                .forEach(entity -> {
                    addNearEntities(player, entity);
                });
    }


    /**
     * Link entity1 and entity2 if they are in near range
     * @param entity1
     * @param entity2
     */
    private void addNearEntities(int entity1, int entity2) {
        int distance = WorldUtils(getServer().getWorld()).distance(E(entity2).getWorldPos(), E(entity1).getWorldPos());
        if (distance >= 0 && distance <= MAX_DISTANCE) {
            linkEntities(entity1, entity2);
            linkEntities(entity2, entity1);
        }
    }

    /**
     * Unlink entities if they are out of range
     * @param player1
     * @param player2
     */
    private void removeNearEntity(int player1, int player2) {
        int distance = WorldUtils(getServer().getWorld()).distance(E(player2).getWorldPos(), E(player1).getWorldPos());
        if (distance < 0 || distance > MAX_DISTANCE) {
            unlinkEntities(player1, player2);
            unlinkEntities(player2, player1);
        }
    }

    /**
     * Unlink entities
     * @param entity1
     * @param entity2
     */
    private void unlinkEntities(int entity1, int entity2) {
        if (nearEntities.containsKey(entity1)) {
            nearEntities.get(entity1).remove(entity2);
        }
        // always notify that this entity is not longer in range
        getServer().getWorldManager().sendEntityRemove(entity1, entity2);
    }


    /**
     * Link entities
     * @param entity1
     * @param entity2
     */
    private void linkEntities(int entity1, int entity2) {
        Set<Integer> near = nearEntities.computeIfAbsent(entity1, (i) -> new HashSet<>());
        if (near.add(entity2)) {
            EntityUpdate update = new EntityUpdate(entity2, WorldUtils(getServer().getWorld()).getComponents(entity2), new Class[0]);
            getServer().getWorldManager().sendEntityUpdate(entity1, update);
        }
    }


    /**
     * Initialize maps. TODO refactor
     */
    public void initialize() {
        Log.info("Loading maps...");
        for (int i = 1; i <= MAP_COUNT; i++) {
            //                FileInputStream mapStream = new FileInputStream("resources/maps/" + "Mapa" + i + ".json");
            InputStream mapStream = MapManager.class.getClassLoader().getResourceAsStream("maps/" + "Mapa" + i + ".json");
            shared.model.map.Map map = getJson().fromJson(shared.model.map.Map.class, mapStream);
            maps.put(i, map);
        }
    }

    private Json getJson() {
        Json json = new Json();
        json.addClassTag("map", shared.model.map.Map.class);
        return json;
    }

    /**
     * @param mapNumber
     * @return corresponding Map
     */
    public shared.model.map.Map get(int mapNumber) {
        return maps.get(mapNumber);
    }


}
