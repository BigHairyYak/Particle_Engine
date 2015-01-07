package mainPackage;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.Timer;
import javax.swing.JFrame;

public class Driver 
{
	static ViewFrame viewFrame;
	public static void main(String[] Args)
	{
		//Grid grid = new Grid(20, 400, 400);
		viewFrame = new ViewFrame();
		viewFrame.setVisible(true);
		viewFrame.gameTimer.start();
	}
}
class ViewFrame extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener
{
	ViewPanel VP;
	Scanner INPUT = new Scanner(System.in);
	int mouseX, mouseY, pmouseX, pmouseY;
	float mouseMass;
	public float ADJUSTMENT = (float) -1;
	public boolean autoPilot;
	Timer gameTimer = new Timer(50, this);
	Random PhysModeSwitcher = new Random();
	int ticksSinceLastSwitch = 0;
	int CoMx = 0; int CoMy = 0;
	static boolean mousePressed = false;
	public ViewFrame()
	{
		VP = new ViewPanel();
		add(VP);
		setSize(950, 950);
		//setUndecorated(true);
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		gameTimer.start();		
	}
	public void actionPerformed(ActionEvent e)
	{
		if (ticksSinceLastSwitch % 50 == 0)
		{
			if (UniversalFunctions.PHYSICS_MODE == 3)
			{
				UniversalFunctions.adjustForceFactor(ADJUSTMENT);
				ticksSinceLastSwitch = 0;
			}
			if (autoPilot)
			{
				int newMode = 1;
				switch ((PhysModeSwitcher.nextInt(16) + 1)/3)
				{
					case (1):
					{
						newMode = 1;
						break;
					}
					case (2):
					{
						newMode = 2;
						break;
					}
					case (3):
					{
						newMode = 4;
						break;
					}
					case (4):
					{
						newMode = 4;
						break;
					}
					case (5):
					{
						resetParticles();
						break;
					}
					default:
					{
						break;
					}
				}
				UniversalFunctions.setPhysMode(newMode);
				ticksSinceLastSwitch = 0;
			}
		}
		Particle A, B;
		Particle centerOfMass = UniversalFunctions.centerOfMass(VP.particleVector);
		CoMx = (int) centerOfMass.X; CoMy = (int) centerOfMass.Y;
		
		for (Enumeration<Particle> q1 = VP.particleVector.elements(); q1.hasMoreElements();)
		{
			A = (Particle) q1.nextElement();
			//for (Enumeration q2 = VP.particleVector.elements(); q2.hasMoreElements();)
			//{
				//B = (Particle) q2.nextElement();
				UniversalFunctions.determineGravitation(A, centerOfMass);
				
				A.X -= (CoMx - 450); A.Y -= (CoMy - 450);
				//UniversalFunctions.determineGravitation(A, VP.mouseParticle);
			//}				
		}
		VP.repaint();
		A = null; B = null;
		ticksSinceLastSwitch++;
	}
	public void keyPressed(KeyEvent E)
	{
		if (E.getKeyCode() == KeyEvent.VK_1)
		{
			UniversalFunctions.setPhysMode(1);
		}
		else if (E.getKeyCode() == KeyEvent.VK_2)
		{
			UniversalFunctions.setPhysMode(2);
		}
		else if (E.getKeyCode() == KeyEvent.VK_3)
		{
			UniversalFunctions.setPhysMode(3);
		}
		else if (E.getKeyCode() == KeyEvent.VK_4)
		{
			UniversalFunctions.setPhysMode(4);
		}
		if (E.getKeyCode() == KeyEvent.VK_J)
		{
			UniversalFunctions.switchJitter();
		}
		if (E.getKeyCode() == KeyEvent.VK_Q)
		{
			float forcedAdjustment = (float).4;
			try
			{
				forcedAdjustment = INPUT.nextFloat();
			}
			catch(InputMismatchException error)
			{
				INPUT = new Scanner(System.in);
			}
			UniversalFunctions.forceAdjustment(forcedAdjustment);
		}
		if (E.getKeyCode() == KeyEvent.VK_A)
		{
			autoPilot = !autoPilot;
		}
		if (E.getKeyCode() == KeyEvent.VK_R)
		{
			resetParticles();
		}
		if (E.getKeyCode() == KeyEvent.VK_SPACE) //PAUSE THE TIMER
		{
			if (gameTimer.isRunning())
				gameTimer.stop();
			else
				gameTimer.start();
		}
	}
	
	public void resetParticles()
	{
		VP.particleVector = new Vector<Particle>();
		for (int q1 = 0; q1 < 900; q1 += 900/VP.fieldDensity)
		{
			for (int q2 = 0; q2 < 900; q2 += 900/VP.fieldDensity)
			{
				VP.particleVector.addElement(new Particle(q1+20, q2+20, 50));
			}
		}
	}
	
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void mouseDragged(MouseEvent e) {}
	/*public void mouseMoved(MouseEvent e) 
	{
		mouseX = e.getX(); mouseY = e.getY();
		VP.particleVector.addElement(new Particle(mouseX, mouseY, 50));
	}*/
	public void mouseMoved(MouseEvent e) 
	{
		//mouseX = e.getX();
		//mouseY = e.getY();
		//VP.mouseParticle.X = e.getX();
		//VP.mouseParticle.Y = e.getY();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {if (!gameTimer.isRunning()) gameTimer.start();}
	@Override
	public void mouseExited(MouseEvent e) {gameTimer.stop();}
	@Override
	public void mousePressed(MouseEvent e)
	{
		mousePressed = true;
		//VP.mouseParticle.artificialParticle = true;
	}
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		mousePressed = false;
		/*VP.particleVector.add(new Particle(VP.mouseParticle));
		VP.mouseParticle.mass = 0;	VP.mouseParticle.X = e.getX(); VP.mouseParticle.Y = e.getY();
		System.out.println("MOUSE RELEASED");*/
	}
}
