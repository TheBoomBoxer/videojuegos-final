package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Game extends SimpleApplication {

    private BulletAppState physical_states;
    private RigidBodyControl physics_track, physics_kart;
    private Light spot;

    public static void main(String[] args) {
        Game app = new Game();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        physical_states = new BulletAppState();
        stateManager.attach(physical_states);
        spot = new PointLight(new Vector3f(-50, 100, 100));
        createTrack();
        // createKart();
        Kart std_kart = new Kart(this, "Wii U - Mario Kart 8 - Standard Kart/Standard Kart.obj", "Four Leaf Tires/Leaf Tires.obj");

        cam.setLocation(std_kart.getNodeKart().getWorldTranslation().add(new Vector3f(0, 2, 0)));
        cam.lookAt(std_kart.getNodeKart().getWorldTranslation(), Vector3f.UNIT_Y);

        rootNode.attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
    }

    private void createTrack() {
        Spatial track = assetManager.loadModel("Wii U - Mario Kart 8 - GCN Dry Dry Desert/Dry Desert Waterless.obj");
        track.addLight(spot);
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

    public BulletAppState getPhysicalStates() {
        return physical_states;
    }
    
    public Light getSpotLight() {
        return spot;
    }

}
