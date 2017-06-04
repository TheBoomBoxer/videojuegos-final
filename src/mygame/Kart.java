/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author James
 */
public class Kart {

    private String character, kart, tires;
    private Spatial geom_kart, geom_tires;
    private Game game;
    private Node node_kart;
    private RigidBodyControl physics_kart;
    private int current_sphere;
    public static final int SPHERE_RADIUS = 5;
    public static final float SPEED = 7;

    public Kart(Game game, String kart, String tire) {
        this.game = game;

        current_sphere = 0;

        geom_kart = game.getAssetManager().loadModel(kart);
        geom_tires = game.getAssetManager().loadModel(tire);
        Spatial geom_mario = game.getAssetManager().loadModel("Wii U - Mario Kart 8 - Mario/mario.obj");
        

        geom_kart.addLight(game.getSpotLight());
        geom_tires.addLight(game.getSpotLight());
        geom_mario.addLight(game.getSpotLight());

        node_kart = new Node();

        node_kart.attachChild(geom_kart);
        node_kart.attachChild(geom_tires);
        node_kart.attachChild(geom_mario);

        node_kart.setLocalTranslation(new Vector3f(-65, 5, 10));

        Matrix3f init_rot = new Matrix3f();
        init_rot.fromAngleAxis((float) Math.PI, Vector3f.UNIT_Y);
        node_kart.setLocalRotation(init_rot);
        node_kart.setLocalScale(.5f);
        
        physics_kart = new RigidBodyControl(2f);
        node_kart.addControl(physics_kart);
        game.getPhysicalStates().getPhysicsSpace().add(physics_kart);

        game.getRootNode().attachChild(node_kart);
    }

    public void update(float tf) {
        Vector3f next_sphere = game.getSphereList().get(current_sphere);
        Vector3f my_pos = physics_kart.getPhysicsLocation();
        Vector3f dir = next_sphere.subtract(my_pos);
       // System.out.println(next_sphere + ", " + current_sphere);
        if (dir.length() < 10) {
            current_sphere = (current_sphere + 1) % 40;
        }

        physics_kart.applyCentralForce(dir.normalize().mult(15));

    }

    public Node getNodeKart() {
        return node_kart;
    }

    public RigidBodyControl getKartPhysics() {
        return physics_kart;
    }

}
