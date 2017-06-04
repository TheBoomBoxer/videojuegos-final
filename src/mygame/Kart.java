/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
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
    
    public Kart(Game game, String kart, String tire) {
        this.game = game;
        
        geom_kart = game.getAssetManager().loadModel(kart);
        geom_tires = game.getAssetManager().loadModel(tire);
        
        geom_kart.addLight(game.getSpotLight());
        geom_tires.addLight(game.getSpotLight());
        
        node_kart = new Node();
        
        node_kart.attachChild(geom_kart);
        node_kart.attachChild(geom_tires);
        
        node_kart.setLocalTranslation(new Vector3f(-65, 10, 10));
        
        Matrix3f init_rot = new Matrix3f();
        init_rot.fromAngleAxis((float) Math.PI, Vector3f.UNIT_Y);
        node_kart.setLocalRotation(init_rot);
        
        physics_kart = new RigidBodyControl(2f);
        node_kart.addControl(physics_kart);
        game.getPhysicalStates().getPhysicsSpace().add(physics_kart);
        
        game.getRootNode().attachChild(node_kart);
    }
    

    public Node getNodeKart() {
        return node_kart;
    }
    
    
}
