package dev.undervolt.renderer;

import dev.undervolt.engine.graphics.Shader;
import dev.undervolt.engine.graphics.Texture;
import dev.undervolt.engine.objects.Camera;
import dev.undervolt.engine.objects.Model;
import dev.undervolt.world.Tile;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class TileRenderer {

    private HashMap<String, Texture> tile_textures;

    private Model model;

    public TileRenderer() {
        tile_textures = new HashMap<>();
        float[] vertices = new float[]{
                -1f, 1f, 0, //TOP LEFT     0
                1f, 1f, 0,  //TOP RIGHT    1
                1f, -1f, 0, //BOTTOM RIGHT 2
                -1f, -1f, 0,//BOTTOM LEFT  3
        };

        float[] textures = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1,
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        model = new Model(vertices, textures, indices);

        for (int i = 0; i < Tile.tiles.length; i++) {
            if (Tile.tiles[i] != null) {
                if (!tile_textures.containsKey(Tile.tiles[i].getTexture())) {
                    String texture = Tile.tiles[i].getTexture();
                    tile_textures.put(texture, new Texture("blocks/" + texture + ".png"));
                }
            }
        }
    }

    public void render(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera) {
        shader.bind();
        if (tile_textures.containsKey(Tile.tiles[tile.getId()].getTexture())) {
            tile_textures.get(Tile.tiles[tile.getId()].getTexture()).bind(0);
        }
        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f target = new Matrix4f();

        camera.getProjection().mul(world, target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);
        model.render();
    }
}
