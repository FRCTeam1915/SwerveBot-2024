package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class TeleopSwerve extends Command {
    private Swerve s_Swerve;
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private BooleanSupplier robotCentricSup;

    private SlewRateLimiter translationLimiter = new SlewRateLimiter(3.0);
    private SlewRateLimiter strafeLimiter = new SlewRateLimiter(3.0);
    private SlewRateLimiter rotationLimiter = new SlewRateLimiter(3.0);

    public TeleopSwerve(
            Swerve s_Swerve,
            DoubleSupplier translationSup,
            DoubleSupplier strafeSup,
            DoubleSupplier rotationSup,
            BooleanSupplier robotCentricSup) {
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;
    }

    @Override
    public void execute() {
        /* Get Values, Deadband*/
        double translationVal =
                translationLimiter.calculate(
                        MathUtil.applyDeadband(translationSup.getAsDouble(), Constants.Swerve.STICK_DEADBAND));
        double strafeVal =
                strafeLimiter.calculate(
                        MathUtil.applyDeadband(strafeSup.getAsDouble(), Constants.Swerve.STICK_DEADBAND));
        double rotationVal =
                rotationLimiter.calculate(
                        MathUtil.applyDeadband(rotationSup.getAsDouble(), Constants.Swerve.STICK_DEADBAND));

        /* Drive */
        s_Swerve.drive(
                new Translation2d(translationVal, strafeVal).times(Constants.Swerve.MAX_SPEED),
                rotationVal * Constants.Swerve.MAX_ANGULAR_VELOCITY,
                !robotCentricSup.getAsBoolean(),
                true
        );
    }
}
