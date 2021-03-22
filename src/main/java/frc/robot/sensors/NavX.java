package frc.robot.sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.geometry.Rotation2d;

public class NavX {

    private static final NavX INSTANCE = new NavX();
    private AHRS ahrs;
    private double offsetFusedHeading;
    private double resetPitchValue;
    private double resetYawValue;

    private NavX() {
        this.ahrs = new AHRS(SPI.Port.kMXP);
        this.offsetFusedHeading = 0;
        this.resetFusedHeading();
        this.resetPitch();
    }

    public static NavX getInstance() {
        return INSTANCE;
    }

    public double getFusedHeading() {
        double fusedHeading = (ahrs.getFusedHeading() - this.offsetFusedHeading);

        //set angle between -180 and 180
        fusedHeading = fusedHeading % 360 - 180;
        //fusedHeading = -fusedHeading;

        /**
        if (fusedHeading < 0) {
            return 360 + fusedHeading;
        }*/
        return fusedHeading;
    }

    public void resetYaw() {
        this.resetYawValue = this.ahrs.getYaw();
    }

    public void resetPitch() {
        this.resetPitchValue = -this.ahrs.getPitch();
    }

    public void resetFusedHeading() {
        this.offsetFusedHeading = ahrs.getFusedHeading();
    }

    public void setFusedHeading(double degreesOffset) {
        this.offsetFusedHeading = ahrs.getFusedHeading() + degreesOffset;
    }

    public void reset() {
        this.resetPitch();
        this.resetFusedHeading();
        this.resetYaw();
    }

    public double getAngleZ() {
        return this.getFusedHeading();
    }

    public Rotation2d getRotation2d() {
        double angle = this.getAngleZ() / 180 * Math.PI;
    
        return new Rotation2d(angle);
    }

    public double getPitchDegrees() {
        double pitchDegrees = -this.ahrs.getPitch() - resetPitchValue;
        return pitchDegrees;
    }

    public double getRollDegrees() {
        return this.ahrs.getRoll();
    }
}

