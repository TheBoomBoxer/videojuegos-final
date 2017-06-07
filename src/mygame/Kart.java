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
    private float max_steer_angle, acceleration;
    private Node node_kart;
    private Game game;
    private Vector3f pos = new Vector3f();
    private VehicleControl vehicle_control;
    private float wheelRadius;
    private float steeringValue;
    private float accelerationValue;
    private int current_point;
    private Vector3f car_eyes;
    private Long time;
    private boolean setFollowCam = false;
    private Vector3f posAnterior;
    
    public boolean isSetFollowCam() {
        return setFollowCam;
    }

    public void setSetFollowCam(boolean setFollowCam) {
        this.setFollowCam = setFollowCam;
    }

    public Kart(Game game, String kart, Vector3f pos, float accl, float maxstr) {
        acceleration = -accl;
        max_steer_angle = (float) Math.PI / (maxstr-4);
        current_point = 0;
        accelerationValue = 0;
        car_eyes = Vector3f.UNIT_Z.mult(-1);
        this.game = game;
        this.pos = pos;
        posAnterior = pos;
        setupKeys();
        buildPlayer2();
        time = System.currentTimeMillis();
    }

    public void update(float tf) {
        // game.getCamera().lookAt(node_kart.getWorldTranslation(), Vector3f.UNIT_Y);
        // System.out.println(node_kart.getWorldTranslation());
        Vector3f my_pos = vehicle_control.getPhysicsLocation();
        Vector3f eyes = vehicle_control.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f eyesNegado = eyes.mult(-1);
        Vector3f back = new Vector3f(my_pos.x + 3 * eyes.x, my_pos.y + 3 * eyes.y + 3, my_pos.z + 6 * eyes.z);
        Vector3f front = new Vector3f(my_pos.x - eyes.x, my_pos.y - eyes.y + 3, my_pos.z - eyes.z);
        if (setFollowCam) {
            game.getCamera().setLocation(back);
            game.getCamera().lookAt(front, Vector3f.UNIT_Y);
        }
        System.out.println(current_point + "point ");

        // If the car enters a water pulse
        if (game.getBox1().contains(vehicle_control.getPhysicsLocation()) || 
                game.getBox2().contains(vehicle_control.getPhysicsLocation()) || 
                game.getBox3().contains(vehicle_control.getPhysicsLocation())) {
            
            vehicle_control.applyCentralForce(new Vector3f(0, 4000, 0));
        }

        Vector3f next_point = game.getSphereList().get(current_point);
        Vector3f dir = next_point.subtract(my_pos);

        System.out.println("eyesNegado: " + Math.acos(dir.normalize().dot(eyesNegado)));

        if (Math.acos(dir.normalize().dot(eyesNegado)) > 0.1 || Math.acos(dir.dot(eyesNegado)) < -0.1) {
            // float steerAngle = (float) Math.acos(dir.normalize().dot(eyesNegado)) * MAX_STEER_ANGLE;
            float steerAngle = dir.normalize().cross(eyesNegado).y * -max_steer_angle;
            vehicle_control.steer(steerAngle);
            System.out.println("Algun mensajito: " + steerAngle / max_steer_angle);
        } else {
            System.out.println("Entra en el else");
            vehicle_control.steer(0);
        }

        vehicle_control.accelerate(acceleration);
        System.out.println("Distance to point " + current_point + ": " + dir.length());
        // vehicle_control.accelerate(-400f);

        if (dir.length() < 8) {
            time = System.currentTimeMillis();
            current_point = (current_point + 1) % game.getSphereList().size();
            System.out.println("current_point: " + current_point);
        }
        
        if(isStuck())
        {
            System.out.println("ATASCADO");
            game.setFitness(current_point);
//            vehicle_control.setPhysicsLocation(pos);
//            current_point = 0;
//            vehicle_control.setPhysicsRotation(Matrix3f.IDENTITY);
//            vehicle_control.clearForces();
//            time = null;
            
        }
//        
//        if(vehicle_control.getPhysicsLocation().subtract(posAnterior) )

        posAnterior = vehicle_control.getPhysicsLocation();
    }
    
    private boolean isStuck() {
        return time!=null && (System.currentTimeMillis() - time) > 10000;
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
        //node_kart.scale(0.1f);
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

        vehicle_control.setPhysicsLocation(pos);

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
            if (value) {
                accelerationValue += 400;
            } else {
                accelerationValue -= 800;
            }
            vehicle_control.accelerate(accelerationValue);
            vehicle_control.setCollisionShape(CollisionShapeFactory.createDynamicMeshShape(findGeom(node_kart, "Car")));
        } else if (binding.equals("Downs")) {
            if (value) {
                vehicle_control.brake(40f);
            } else {
                vehicle_control.brake(0f);
            }
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                vehicle_control.setPhysicsLocation(new Vector3f(-65, 5, 10));
                vehicle_control.setPhysicsRotation(new Matrix3f());
                vehicle_control.setLinearVelocity(Vector3f.ZERO);
                vehicle_control.setAngularVelocity(Vector3f.ZERO);
                vehicle_control.resetSuspension();
            } else {
            }
        }

    }

    public VehicleControl getVehicleControl() {
        return vehicle_control;
    }
    
    

}
