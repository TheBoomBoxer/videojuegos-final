package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */

public class Game extends SimpleApplication {
    
    private BulletAppState physical_states;
    private RigidBodyControl physics_track;

    public static void main(String[] args) {
        Game app = new Game();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(8f);
        physical_states = new BulletAppState();
        stateManager.attach(physical_states);
        createTrack();
    }
    
    private void createTrack() {
        Geometry track = (Geometry) assetManager.loadModel("Models/track.j3o");
        Light light = new DirectionalLight(Vector3f.UNIT_Y.mult(-1));
        track.addLight(light);
        
        physics_track = new RigidBodyControl(0f);
        track.addControl(physics_track);
        physical_states.getPhysicsSpace().add(physics_track);
        physics_track.setFriction(.5f);
        rootNode.attachChild(track);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
