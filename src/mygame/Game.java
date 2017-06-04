package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */

public class Game extends SimpleApplication {
    
    private BulletAppState physical_states;
    private RigidBodyControl physics_track, physics_kart;

    public static void main(String[] args) {
        Game app = new Game();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        physical_states = new BulletAppState();
        stateManager.attach(physical_states);
        createTrack();
        createKart();
        rootNode.attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
    }
    
    private void createTrack() {
        Spatial track = assetManager.loadModel("Wii U - Mario Kart 8 - GCN Dry Dry Desert/GCN Dry Desert.obj");
        Light light = new DirectionalLight(Vector3f.UNIT_Y.mult(-1));
        track.addLight(light);
        
        physics_track = new RigidBodyControl(0f);
        track.addControl(physics_track);
        physical_states.getPhysicsSpace().add(physics_track);
        physics_track.setFriction(.5f);
        rootNode.attachChild(track);
    }

    private void createKart() {
        Spatial geom_standard_kart =  assetManager.loadModel("Wii U - Mario Kart 8 - Standard Kart/Standard Kart.obj");
        Light light = new DirectionalLight(Vector3f.UNIT_Y.mult(-1));
        geom_standard_kart.addLight(light);
        geom_standard_kart.setLocalTranslation(new Vector3f(-65, 22, 10));
        Matrix3f init_rot = new Matrix3f();
        init_rot.fromAngleAxis((float) Math.PI, Vector3f.UNIT_Y);
        geom_standard_kart.setLocalRotation(init_rot);
        cam.setLocation(geom_standard_kart.getWorldTranslation().add(new Vector3f(0,2,0)));
        cam.lookAt(geom_standard_kart.getWorldTranslation(), Vector3f.UNIT_Y);
        
        physics_kart = new RigidBodyControl(0f);
        geom_standard_kart.addControl(physics_kart);
        physical_states.getPhysicsSpace().add(physics_kart);
        rootNode.attachChild(geom_standard_kart);
        
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
