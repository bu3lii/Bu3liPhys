package physicsengine2d;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class PersistenceManager {

    private final Gson gson;

    public PersistenceManager(){
        this.gson=new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveSimulation(World world, File file) throws IOException{
        WorldState currentState = new WorldState(world.getGravity(),world.getObjects());

        try(FileWriter writer = new FileWriter(file)){
            gson.toJson(currentState,writer);
        }
    }

    public WorldState loadSimulation(File file) throws IOException {
        if (!file.exists() || !file.canRead()){
            throw new IOException("File does not exist or cannot be read");
        }
        try (FileReader reader = new FileReader(file)){
            WorldState loadedState = gson.fromJson(reader, WorldState.class);
            if(loadedState==null){
                throw new IOException("Could not read file contents, file may be corrupted");
            }
            return loadedState;
        }
    }
}
