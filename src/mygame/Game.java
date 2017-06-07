package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
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
    private Kart std_kart2;
    private BoundingBox box_1, box_2, box_3;
    private double fitness;
    private float acceleration, max_steer_angle;
    private Object o;
    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public Game(int accl, int maxstr, Object o) {
        acceleration = accl;
        max_steer_angle = maxstr;
        this.o = o;
    }
     

    public static void main(String[] args) {
//        Game app = new Game();
//        app.start();
    }

    @Override
    public void simpleInitApp() {
//        flyCam.setMoveSpeed(10f);
        fitness = -1;
        sphere_list = new ArrayList<>();
        physical_states = new BulletAppState();
        stateManager.attach(physical_states);
        spot = new PointLight(new Vector3f(-50, 100, 100));
        rootNode.addLight(spot);
        createTrack();
        // createKart();
        createCircuit();
        createWater();
        createWaterPulses();
        std_kart = new Kart(this, "Wii U - Mario Kart 8 - Standard Kart/Standard Kart.obj",new Vector3f(-65f, 0f, 10f), acceleration, max_steer_angle);
        std_kart.setSetFollowCam(true);
        
        //std_kart2 = new Kart(this, "Wii U - Mario Kart 8 - Standard Kart/Standard Kart.obj",new Vector3f(-65, -2, 10));
        //cam.setLocation(std_kart.getVehicleControl().getPhysicsLocation().add(new Vector3f(0, 2, 0)));
        //cam.lookAt(std_kart.getVehicleControl().getPhysicsLocation(), Vector3f.UNIT_Y);

        rootNode.attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
    }

    private void createTrack() {
        Spatial track = assetManager.loadModel("Wii U - Mario Kart 8 - GCN Dry Dry Desert/Dry Desert Waterless.obj");
        track.addLight(spot);
        track.setShadowMode(ShadowMode.Receive);
        
        Spatial spheres = assetManager.loadModel("Control Spheres/spheres.obj");
        rootNode.attachChild(spheres);

        physics_track = new RigidBodyControl(0f);
        track.addControl(physics_track);
        physical_states.getPhysicsSpace().add(physics_track);
        physics_track.setFriction(.5f);
        rootNode.attachChild(track);
    }
    
    private void createWaterPulses() {
        box_1 = new BoundingBox(new Vector3f(27.11419f, -25.55816f, 264.05148f), 14f, 5f, 14f);
        box_2 = new BoundingBox(new Vector3f(-21.80325f, -25.55816f, 271.94205f), 14f, 5f, 14f);
        box_3 = new BoundingBox(new Vector3f(-75.81601f, -25.55816f, 243.22943f), 14f, 5f, 14f);
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
    public synchronized void simpleUpdate(float tpf) {
        // TODO: add update code
        std_kart.update(tpf);
        if (fitness >= 0) {
            o.notify();
            stop();
        }
        // std_kart2.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void createCircuit2() {
        sphere_list.add(new Vector3f(-245.62790f, 5.30044f, -123.25159f));
        sphere_list.add(new Vector3f(-213.13654f, 5.30044f, -213.08435f));
        sphere_list.add(new Vector3f(-150.55319f, 5.30044f, -248.03986f));
        sphere_list.add(new Vector3f(-93.02710f, 5.30044f, -231.53078f));
        sphere_list.add(new Vector3f(-39.56034f, 5.30044f, -184.55576f));
        sphere_list.add(new Vector3f(11.20152f, 5.30044f, -217.32643f));
        sphere_list.add(new Vector3f(43.89831f, 5.30044f, -223.20647f));
        sphere_list.add(new Vector3f(82.01228f, 5.30044f, -175.97444f));
        sphere_list.add(new Vector3f(144.71498f, 5.30044f, -225.04626f));
        sphere_list.add(new Vector3f(217.53221f, 5.30044f, -249.04906f));
        sphere_list.add(new Vector3f(274.82755f, 5.30044f, -226.33450f));
        sphere_list.add(new Vector3f(249.09311f, 5.30044f, -134.26569f));
        sphere_list.add(new Vector3f(204.95755f, 5.30044f, -60.64651f));
        sphere_list.add(new Vector3f(189.94162f, 5.30044f, -18.23472f));
        sphere_list.add(new Vector3f(203.62585f, 5.30044f, 20.75019f));
        sphere_list.add(new Vector3f(230.60085f, 5.30044f, 72.75465f));
        sphere_list.add(new Vector3f(248.51663f, 5.30044f, 139.93573f));
        sphere_list.add(new Vector3f(241.56050f, 5.30044f, 185.29614f));
        sphere_list.add(new Vector3f(219.48958f, 5.30044f, 227.80998f));
        sphere_list.add(new Vector3f(170.75247f, 5.30044f, 258.18802f));
        sphere_list.add(new Vector3f(115.49359f, 5.30044f, 263.94482f));
        sphere_list.add(new Vector3f(48.13744f, 5.30044f, 247.92998f));
        sphere_list.add(new Vector3f(-6.63051f, 5.30044f, 261.22830f));
        sphere_list.add(new Vector3f(-53.62571f, 5.30044f, 239.49409f));
        sphere_list.add(new Vector3f(-163.25490f, 5.30044f, 239.49409f));
        sphere_list.add(new Vector3f(-255.95808f, 5.30044f, 237.24933f));
        sphere_list.add(new Vector3f(-268.59679f, 5.30044f, 184.38710f));
        sphere_list.add(new Vector3f(-225.15872f, 5.30044f, 127.73737f));
        
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
        sphere_list.add(new Vector3f(58.74326f, -4.39841f, -14.74751f));
        sphere_list.add(new Vector3f(57.27221f, -5.25833f, -6.43602f));
        sphere_list.add(new Vector3f(57.68353f, -5.58911f, -1.66141f));
        sphere_list.add(new Vector3f(59.99884f, -4.77143f, 8.94757f)); //21
        sphere_list.add(new Vector3f(53.70052f, -2.22360f, 8.94757f));
        sphere_list.add(new Vector3f(60.64709f, -2.22360f, 15.85904f));
        sphere_list.add(new Vector3f(66.17561f, -2.22360f, 26.65774f));
        sphere_list.add(new Vector3f(67.85895f, -2.22360f, 42.75705f));
        sphere_list.add(new Vector3f(64.55494f, -0.16024f, 55.87842f));
        sphere_list.add(new Vector3f(59.31455f, -0.16024f, 64.59521f));
        sphere_list.add(new Vector3f(49.18835f, -0.16024f, 71.75800f));
        sphere_list.add(new Vector3f(34.13102f, -1.67521f, 77.90910f)); // 29
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
    
    public BoundingBox getBox1() {
        return box_1;
        
    }
    
    public BoundingBox getBox2() {
        return box_2;
        
    }
    
    public BoundingBox getBox3() {
        return box_3;
        
    }
    
    public void setAcceleration(float acc) {
        acceleration = acc;
    }
    
    public void setMaxSteerAngle(float maxstr) {
        max_steer_angle = maxstr;
    }

    

}
