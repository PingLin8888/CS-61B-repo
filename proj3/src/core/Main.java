package core;

import tileengine.TERenderer;

public class Main {
    public static void main(String[] args) {

        // build your own world!

        //main menu with options. navigate via keyboard
        World world = new World();
        TERenderer teRenderer = new TERenderer();
        int width = world.getMap().length;
        int height = world.getMap()[0].length;
        teRenderer.initialize(width, height);

        world.buildWorld(50);

        teRenderer.renderFrame(world.getMap());

    }
}
