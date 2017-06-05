package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import java.util.ArrayList;
import java.util.List;

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
    private List<Vector3f> sphere_list;
    private Kart std_kart;

    public static void main(String[] args) {
        Game app = new Game();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        sphere_list = new ArrayList<>();
        physical_states = new BulletAppState();
        stateManager.attach(physical_states);
        spot = new PointLight(new Vector3f(-50, 100, 100));
        rootNode.addLight(spot);
        createTrack();
        // createKart();
        createCircuit();
        createWater();
        std_kart = new Kart(this, "Wii U - Mario Kart 8 - Standard Kart/Standard Kart.obj");

        cam.setLocation(std_kart.getNodeKart().getWorldTranslation().add(new Vector3f(0, 2, 0)));
        cam.lookAt(std_kart.getNodeKart().getWorldTranslation(), Vector3f.UNIT_Y);

        rootNode.attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
    }

    private void createTrack() {
        Spatial track = assetManager.loadModel("Wii U - Mario Kart 8 - GCN Dry Dry Desert/Dry Desert Waterless.obj");
        track.addLight(spot);
        track.setShadowMode(ShadowMode.Receive);

        physics_track = new RigidBodyControl(0f);
        track.addControl(physics_track);
        physical_states.getPhysicsSpace().add(physics_track);
        physics_track.setFriction(.5f);
        rootNode.attachChild(track);
    }

    private void createWater() {
        WaterFilter water = new WaterFilter(rootNode, Vector3f.UNIT_Y.mult(-1));
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(water);

        BloomFilter bloom = new BloomFilter();

        bloom.setExposurePower(55);
        bloom.setBloomIntensity(1.0f);

        fpp.addFilter(bloom);

        LightScatteringFilter lsf = new LightScatteringFilter(Vector3f.UNIT_Y.mult(-300));
        lsf.setLightDensity(1.0f);
        fpp.addFilter(lsf);

        DepthOfFieldFilter dof = new DepthOfFieldFilter();
        dof.setFocusDistance(0);
        dof.setFocusRange(100);
        fpp.addFilter(dof);

        water.setWaveScale(0.003f);
        water.setMaxAmplitude(2f);
        water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
        water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));

        water.setRefractionStrength(0.2f);
 
        water.setRadius(40f);
        water.setCenter(new Vector3f(-5.40856f, 0f, 73.2708f));
        // water.setCenter(new Vector3f(58.43384f, -9.44549f, 65.74243f));
        water.setWaterHeight(-6.5f);
        System.out.println(water.getShapeType() + ", " + water.getRadius());
         
        viewPort.addProcessor(fpp);

    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        std_kart.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void createCircuit() {
        sphere_list.add(new Vector3f(-66.42629f, -0.61749f, -36.41609f));
        sphere_list.add(new Vector3f(-56.63694f, 0.13945f, -62.27739f));
        sphere_list.add(new Vector3f(-36.80048f, 0.13945f, -68.92015f));
        sphere_list.add(new Vector3f(-25.77202f, -3.17818f, -61.65129f));
        sphere_list.add(new Vector3f(-14.13021f, -3.17818f, -51.90519f));
        sphere_list.add(new Vector3f(-2.68156f, -4.22268f, -52.90021f));
        sphere_list.add(new Vector3f(5.34758f, -5.04010f, -60.56477f));
        sphere_list.add(new Vector3f(16.70005f, -5.07571f, -58.31284f));
        sphere_list.add(new Vector3f(22.83770f, -3.77291f, -47.32069f));
        sphere_list.add(new Vector3f(31.42191f, -3.77291f, -51.12412f));
        sphere_list.add(new Vector3f(40.96337f, -2.49726f, -61.34949f));
        sphere_list.add(new Vector3f(53.34271f, -0.66898f, -65.76698f));
        sphere_list.add(new Vector3f(65.82645f, 1.09590f, -66.97594f));
        sphere_list.add(new Vector3f(73.87437f, 2.52069f, -62.90333f));
        sphere_list.add(new Vector3f(75.94656f, 0.42250f, -50.74335f));
        sphere_list.add(new Vector3f(71.36706f, -1.50554f, -38.28936f));
        sphere_list.add(new Vector3f(66.57477f, -2.22360f, -28.66216f));
        sphere_list.add(new Vector3f(60.49354f, -2.22360f, -22.53596f));
        sphere_list.add(new Vector3f(53.47881f, -2.22360f, -14.74751f));
        sphere_list.add(new Vector3f(50.31588f, -2.22360f, -6.83443f));
        sphere_list.add(new Vector3f(50.64336f, -2.22360f, 1.66141f));
        sphere_list.add(new Vector3f(53.70052f, -2.22360f, 8.94757f));
        sphere_list.add(new Vector3f(60.64709f, -2.22360f, 15.85904f));
        sphere_list.add(new Vector3f(66.17561f, -2.22360f, 26.65774f));
        sphere_list.add(new Vector3f(67.85895f, -2.22360f, 42.75705f));
        sphere_list.add(new Vector3f(64.55494f, -0.16024f, 55.87842f));
        sphere_list.add(new Vector3f(59.31455f, -0.16024f, 64.59521f));
        sphere_list.add(new Vector3f(49.18835f, -0.16024f, 71.75800f));
        sphere_list.add(new Vector3f(34.13102f, -1.67521f, 77.90910f)); // 28
        sphere_list.add(new Vector3f(22.55143f, -3.40478f, 73.24865f));
        sphere_list.add(new Vector3f(10.99477f, -3.40478f, 68.04240f));
        sphere_list.add(new Vector3f(-1.76227f, -3.40478f, 53.90268f));
        sphere_list.add(new Vector3f(-17.40836f, -5.05639f, 56.45221f));
        sphere_list.add(new Vector3f(-30.38690f, -5.05639f, 61.58204f));
        sphere_list.add(new Vector3f(-43.28731f, -3.36221f, 67.38071f));
        sphere_list.add(new Vector3f(-68.16870f, -0.86987f, 67.38071f));
        sphere_list.add(new Vector3f(-74.71447f, -0.86987f, 58.97087f));
        sphere_list.add(new Vector3f(-70.00146f, -0.86987f, 46.97723f));
        sphere_list.add(new Vector3f(-62.23225f, -1.59350f, 35.09459f));
        sphere_list.add(new Vector3f(-65.04268f, -0.16430f, 2.03006f));
    }

    public BulletAppState getPhysicalStates() {
        return physical_states;
    }

    public Light getSpotLight() {
        return spot;
    }

    public List<Vector3f> getSphereList() {
        return sphere_list;
    }

}
