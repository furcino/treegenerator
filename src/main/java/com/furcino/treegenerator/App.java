package com.furcino.treegenerator;


import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Cylinder;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

import java.util.UUID;

public class App extends SimpleApplication {

    long lastCheckTime = 0;
    public static void main(String[] args) {

        App app = new App();

        AppSettings settings = new AppSettings(true);
        settings.setTitle("My Awesome Game");
        app.setSettings(settings);

        app.start();

    }

    @Override
    public void simpleInitApp() {
        createBranches(0, new BranchInfo());
        rootNode.rotate((float) Math.PI/2, 0, 0);

    }

    @Override
    public void simpleUpdate(float tpf) {
        if ((timer.getTime() - lastCheckTime) > 10_000_000_000L) {
            lastCheckTime = timer.getTime();
            rootNode.detachAllChildren();
            createBranches(0, new BranchInfo());
        }
        // rotate world
        rootNode.rotate(0, 0, 1 * tpf);
    }

    private void createBranches(int level, BranchInfo info) {
        // set length and radius for cylinder
        float length = 0.6f - level * 0.06f;
        float radius = 0.03f - level * 0.004f;

        // create cylinder
        Cylinder cylinder = new Cylinder(10, 10, radius, length, true);
        Geometry geom = new Geometry("Cylinder" + UUID.randomUUID(), cylinder);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Brown);
        geom.setMaterial(mat);

        // create pivot node
        Node node = new Node("pivot" + UUID.randomUUID());
        node.attachChild(geom);
        rootNode.attachChild(node);
        // cylinder has pivot in center, moving cylinder so that pivot will be at the bottom end
        geom.setLocalTranslation(0 , 0, -length/2);
        // move and rotate branch
        node.attachChild(geom);
        node.setLocalTranslation(info.positionX, info.positionY, info.positionZ);
        node.rotate(info.rotationX, info.rotationY, 0);

        // generate random rotation values (only two are needed)
        float rotationX = (float)  (Math.random()-0.5* FastMath.PI/2);
        float rotationY = (float) (Math.random()-0.5* FastMath.PI/2);

        // calculate new rotation
        float nextX = (float) -(Math.sin(info.rotationY) * length);
        float nextY = (float) (Math.sin(info.rotationX) * length);
        float nextZ = (float) -(Math.cos(info.rotationY) * Math.cos(info.rotationX) * length);

        // create new branch info
        BranchInfo newInfo = new BranchInfo();
        newInfo.rotationX = FastMath.abs(info.rotationX + rotationX) < FastMath.PI/2 ? info.rotationX + rotationX : info.rotationX;
        newInfo.rotationY = FastMath.abs(info.rotationY + rotationY) < FastMath.PI/2 ? info.rotationY + rotationY : info.rotationY;
        newInfo.positionX = info.positionX + nextX;
        newInfo.positionY = info.positionY + nextY;
        newInfo.positionZ = info.positionZ + nextZ;

        if (level < 6) {
            level++;
            createBranches(level, newInfo);

            BranchInfo info1 = new BranchInfo(newInfo);
            info1.rotationX = -newInfo.rotationX;
            info1.rotationY = newInfo.rotationY;
            createBranches(level, info1);

            BranchInfo info2 = new BranchInfo(newInfo);
            info2.rotationX = -newInfo.rotationX;
            info2.rotationY = -newInfo.rotationY;
            createBranches(level, info2);

            BranchInfo info3 = new BranchInfo(newInfo);
            info3.rotationX = newInfo.rotationX;
            info3.rotationY = -newInfo.rotationY;
            createBranches(level, info3);
        } else {
            Geometry flower = new Geometry("Triangle" + UUID.randomUUID(), createFlower());
            Material flowerMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            flowerMat.setColor("Color", ColorRGBA.White);
            flower.setMaterial(flowerMat);
            flower.scale(0.01f);
            float flowerRotateX = (float) Math.random() * FastMath.PI;
            float flowerRotateY = (float) Math.random() * FastMath.PI;
            float flowerRotateZ = (float) Math.random() * FastMath.PI;
            flower.rotate(flowerRotateX, flowerRotateY, flowerRotateZ);
            flower.setLocalTranslation(newInfo.positionX, newInfo.positionY, newInfo.positionZ);

            rootNode.attachChild(flower);
        }
    }

    private Mesh createFlower() {
        Vector3f [] vertices = new Vector3f[12];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(3,-2,0);
        vertices[2] = new Vector3f(3,2,0);
        vertices[3] = new Vector3f(0,0,0);
        vertices[4] = new Vector3f(2,3,0);
        vertices[5] = new Vector3f(-2,3,0);
        vertices[6] = new Vector3f(0,0,0);
        vertices[7] = new Vector3f(-3,2,0);
        vertices[8] = new Vector3f(-3,-2,0);
        vertices[9] = new Vector3f(0,0,0);
        vertices[10] = new Vector3f(-2,-3,0);
        vertices[11] = new Vector3f(2,-3,0);

        int [] indexes = { 0,1,2, 3,4,5, 6,7,8, 9,10,11 };

        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();

        return mesh;
    }
}
