package com.furcino.treegenerator;

public class BranchInfo {
    public float positionX = 0;
    public float positionY = 0;
    public float positionZ = 0;
    public float rotationX = 0;
    public float rotationY = 0;

    BranchInfo() {

    }

    BranchInfo (BranchInfo toCopy) {
        this.positionX = toCopy.positionX;
        this.positionY = toCopy.positionY;
        this.positionZ = toCopy.positionZ;
        this.rotationX = toCopy.rotationX;
        this.rotationY = toCopy.rotationY;
    }
}
