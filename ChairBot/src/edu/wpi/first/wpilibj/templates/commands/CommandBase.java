package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.Turret;
import edu.wpi.first.wpilibj.templates.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.templates.subsystems.LEDs;
import edu.wpi.first.wpilibj.templates.subsystems.Valves;

public abstract class CommandBase extends Command {

    public static OI oi;
    public static Drivetrain drivetrain = new Drivetrain();
    public static LEDs LEDs = new LEDs();
    public static Valves valves = new Valves();
    public static Turret turret = new Turret();

    public static void init() {
        oi = new OI();
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
