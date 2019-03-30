package server.manager;

import com.esotericsoftware.minlog.Log;
import server.core.Server;
import server.database.ServerDescriptorReader;
import shared.model.readers.DescriptorsReader;
import shared.objects.types.Obj;
import shared.objects.types.Type;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Load and contains all the objects
 */
public class ObjectManager extends DefaultManager {

    private DescriptorsReader reader = new ServerDescriptorReader();
    private Map<Integer, Obj> objects;

    public ObjectManager(Server server) {
        super(server);
    }

    @Override
    public void init() {
        Log.info("Loading objects...");
        objects = reader.loadObjects("obj");
    }

    public Optional<Obj> getObject(int id) {
        return Optional.of(objects.get(id));
    }

    public Set<Obj> getTypeObjects(Type type) {
        return objects.values().stream().filter(obj -> obj.getType().equals(type)).collect(Collectors.toSet());
    }
}
