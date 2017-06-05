/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author James
 */
public class Kart implements ActionListener {

    private Node node_kart;
    private Game game;
    private VehicleControl vehicle_control;
    private float wheelRadius;
    private float steeringValue;
    private float accelerationValue;

    public Kart(Game game, String kart) {
        accelerationValue = 800;
        this.game = game;
        buildPlayer2();
    }

    public void update(float tf) {
        // game.getCamera().lookAt(node_kart.getWorldTranslation(), Vector3f.UNIT_Y);
        // System.out.println(node_kart.getWorldTranslation());
    }

    public Node getNodeKart() {
        return node_kart;
    }

    private Geometry findGeom(Spatial spatial, String name) {
        if (spatial instanceof Node) {
            Node node = (Node) spatial;
            for (int i = 0; i < node.getQuantity(); i++) {
                Spatial child = node.getChild(i);
                Geometry result = findGeom(child, name);
                if (result != null) {
                    return result;
                }
            }
        } else if (spatial instanceof Geometry) {
            if (spatial.getName().startsWith(name)) {
                return (Geometry) spatial;
            }
        }
        return null;
    }

    private void buildPlayer() {
        float stiffness = 120.0f;
        float compValue = 0.2f;
        float dampValue = 0.3f;
        final float mass = 400;

        // Load model and get chasis geometry
        node_kart = (Node) game.getAssetManager().loadModel("Models/Standard Kart/standard kart leaf.scene");
        node_kart.setShadowMode(ShadowMode.Cast);

        Geometry chasis = findGeom(node_kart, "Kart");
        System.out.println(chasis);
        BoundingBox box;

        // Create a hull collision shape fot the chasis
        CollisionShape carHull = CollisionShapeFactory.createDynamicMeshShape(chasis);

        // Create a vehicle control
        vehicle_control = new VehicleControl(carHull, mass);
        node_kart.addControl(vehicle_control);

        // Setting default values for wheels
        vehicle_control.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle_control.setSuspensionDamping(dampValue * 2.0f * FastMath.sqr(stiffness));
        vehicle_control.setSuspensionStiffness(stiffness);
        vehicle_control.setMaxSuspensionForce(10000);

        // Create Four Wheels and add them at their locations
        Vector3f wheelDirection = new Vector3f(0, -1, 0);
        Vector3f wheelAxle = new Vector3f(-1, 0, 0);

        Geometry wheel_fr = findGeom(node_kart, "FrontRight");
        System.out.println(wheel_fr);
        wheel_fr.center();

        box = (BoundingBox) wheel_fr.getModelBound();
        wheelRadius = box.getYExtent();

        float back_wheel_h = (wheelRadius * 1.7f) - 1f;
        float front_wheel_h = (wheelRadius * 1.7f) - 1f;

        vehicle_control.addWheel(wheel_fr.getParent(), box.getCenter().add(0, -front_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, true);
       

        Geometry wheel_fl = findGeom(node_kart, "FrontLeft");
        System.out.println(wheel_fl);
        wheel_fl.center();

        box = (BoundingBox) wheel_fl.getModelBound();

        vehicle_control.addWheel(wheel_fl.getParent(), box.getCenter().add(0, -front_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, true);

        Geometry wheel_br = findGeom(node_kart, "BackRight");
        System.out.println(wheel_br);
        
        wheel_br.center();

        box = (BoundingBox) wheel_br.getModelBound();

        vehicle_control.addWheel(wheel_br.getParent(), box.getCenter().add(0, -back_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, false);

        Geometry wheel_bl = findGeom(node_kart, "BackLeft");
        System.out.println(wheel_bl);
        wheel_bl.center();
        box = (BoundingBox) wheel_bl.getModelBound();
        vehicle_control.addWheel(wheel_bl.getParent(), box.getCenter().add(0, -back_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, false);

        vehicle_control.getWheel(2).setFrictionSlip(4);
        vehicle_control.getWheel(3).setFrictionSlip(4);
        
        System.out.println(node_kart.getWorldTranslation());
        game.getRootNode().attachChild(node_kart);
        game.getPhysicalStates().getPhysicsSpace().add(vehicle_control);
    }
    
    private void buildPlayer2() {
        float stiffness = 120.0f; // 200 = f1 car
        float compValue = 0.2f; // (lower than damp!)
        float dampValue = 0.3f;
        final float mass = 400;

        //Load model and get chassis Geometry
        node_kart = (Node) game.getAssetManager().loadModel("Models/Ferrari/Car.scene");
        node_kart.setShadowMode(ShadowMode.Cast);
        Geometry chasis = findGeom(node_kart, "Car");
        BoundingBox box;

        //Create a hull collision shape for the chassis
        CollisionShape carHull = CollisionShapeFactory.createDynamicMeshShape(chasis);

        //Create a vehicle control
        vehicle_control = new VehicleControl(carHull, mass);
        node_kart.addControl(vehicle_control);

        //Setting default values for wheels
        vehicle_control.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle_control.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle_control.setSuspensionStiffness(stiffness);
        vehicle_control.setMaxSuspensionForce(10000);

        // Create four wheels and add them at their locations
        // note that our fancy car actually goes backwards..
        Vector3f wheelDirection = new Vector3f(0, -1, 0);
        Vector3f wheelAxle = new Vector3f(-1, 0, 0);

        Geometry wheel_fr = findGeom(node_kart, "WheelFrontRight");
        wheel_fr.center();
        box = (BoundingBox) wheel_fr.getModelBound();
        wheelRadius = box.getYExtent();
        float back_wheel_h = (wheelRadius * 1.7f) - 1f;
        float front_wheel_h = (wheelRadius * 1.9f) - 1f;
        vehicle_control.addWheel(wheel_fr.getParent(), box.getCenter().add(0, -front_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, true);

        
        Geometry wheel_fl = findGeom(node_kart, "WheelFrontLeft");
        wheel_fl.center();
        box = (BoundingBox) wheel_fl.getModelBound();
        vehicle_control.addWheel(wheel_fl.getParent(), box.getCenter().add(0, -front_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, true);

        Geometry wheel_br = findGeom(node_kart, "WheelBackRight");
        wheel_br.center();
        box = (BoundingBox) wheel_br.getModelBound();
        vehicle_control.addWheel(wheel_br.getParent(), box.getCenter().add(0, -back_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, false);

        Geometry wheel_bl = findGeom(node_kart, "WheelBackLeft");
        wheel_bl.center();
        box = (BoundingBox) wheel_bl.getModelBound();
        vehicle_control.addWheel(wheel_bl.getParent(), box.getCenter().add(0, -back_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, false);

        vehicle_control.getWheel(2).setFrictionSlip(4);
        vehicle_control.getWheel(3).setFrictionSlip(4);

        game.getRootNode().attachChild(node_kart);
        game.getPhysicalStates().getPhysicsSpace().add(vehicle_control);
    }
    
    private void setupKeys() {
        game.getInputManager().addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        game.getInputManager().addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        game.getInputManager().addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
        game.getInputManager().addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
        game.getInputManager().addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        game.getInputManager().addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        game.getInputManager().addListener(this, "Lefts");
        game.getInputManager().addListener(this, "Rights");
        game.getInputManager().addListener(this, "Ups");
        game.getInputManager().addListener(this, "Downs");
        game.getInputManager().addListener(this, "Space");
        game.getInputManager().addListener(this, "Reset");
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                steeringValue += .5f;
            } else {
                steeringValue += -.5f;
            }
            vehicle_control.steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (value) {
                steeringValue += -.5f;
            } else {
                steeringValue += .5f;
            }
            vehicle_control.steer(steeringValue);
        } //note that our fancy car actually goes backwards..
        else if (binding.equals("Ups")) {
            System.out.println("ACCELERATE");
            if (value) {
                accelerationValue -= 800;
            } else {
                accelerationValue += 800;
            }
            vehicle_control.accelerate(accelerationValue);
            vehicle_control.setCollisionShape(CollisionShapeFactory.createDynamicMeshShape(findGeom(node_kart, "Kart")));
        } else if (binding.equals("Downs")) {
            if (value) {
                vehicle_control.brake(40f);
            } else {
                vehicle_control.brake(0f);
            }
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                vehicle_control.setPhysicsLocation(Vector3f.ZERO);
                vehicle_control.setPhysicsRotation(new Matrix3f());
                vehicle_control.setLinearVelocity(Vector3f.ZERO);
                vehicle_control.setAngularVelocity(Vector3f.ZERO);
                vehicle_control.resetSuspension();
            } else {
            }
        }

    }

}
