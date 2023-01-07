package me.sjnez.renosense.manager;

import me.sjnez.renosense.features.Feature;
import me.sjnez.renosense.util.MathUtil;
import me.sjnez.renosense.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RotationManager
extends Feature {
    private float yaw;
    private float pitch;

    public void updateRotations() {
        this.yaw = RotationManager.mc.player.field_70177_z;
        this.pitch = RotationManager.mc.player.field_70125_A;
    }

    public void restoreRotations() {
        RotationManager.mc.player.field_70177_z = this.yaw;
        RotationManager.mc.player.field_70759_as = this.yaw;
        RotationManager.mc.player.field_70125_A = this.pitch;
    }

    public void setPlayerRotations(float yaw, float pitch) {
        RotationManager.mc.player.field_70177_z = yaw;
        RotationManager.mc.player.field_70759_as = yaw;
        RotationManager.mc.player.field_70125_A = pitch;
    }

    public void setPlayerYaw(float yaw) {
        RotationManager.mc.player.field_70177_z = yaw;
        RotationManager.mc.player.field_70759_as = yaw;
    }

    public void lookAtPos(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(RotationManager.mc.player.func_174824_e(mc.getRenderPartialTicks()), new Vec3d((float)pos.func_177958_n() + 0.5f, (float)pos.func_177956_o() + 0.5f, (float)pos.func_177952_p() + 0.5f));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(Vec3d vec3d) {
        float[] angle = MathUtil.calcAngle(RotationManager.mc.player.func_174824_e(mc.getRenderPartialTicks()), new Vec3d(vec3d.x, vec3d.y, vec3d.z));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(double x, double y, double z) {
        Vec3d vec3d = new Vec3d(x, y, z);
        this.lookAtVec3d(vec3d);
    }

    public void lookAtEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(RotationManager.mc.player.func_174824_e(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void setPlayerPitch(float pitch) {
        RotationManager.mc.player.field_70125_A = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getDirection4D() {
        return RotationUtil.getDirection4D();
    }

    public String getDirection4D(boolean northRed) {
        return RotationUtil.getDirection4D(northRed);
    }
}
