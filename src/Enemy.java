import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Enemy
{
	public static final int ENEMY_ALIVE_STATE = 1;
	public static final int ENEMY_DEATH_STATE = 0;
	
	static final int ENEMY_STEP_Y = 5;
	
	static final int ENEMY_TYPE_MAX = 6; 
	
	public int mPosX = 0;
	public int mPosY = 0;
	
	public int enemyState = ENEMY_ALIVE_STATE;
	
	private Image enemyExploreImg[] = new Image[ENEMY_TYPE_MAX];
	
	public int mEnemyID = 0;
	
	public Enemy()
	{
		for(int i = 0; i< ENEMY_TYPE_MAX; i++)
		{
			enemyExploreImg[i] = Toolkit.getDefaultToolkit().getImage("image/bomb_enemy_"+i+".png");
		}
	}
	
	public void init(int x, int y)
	{
		mPosX = x;
		mPosY = y;
		enemyState = ENEMY_ALIVE_STATE;
		mEnemyID = 0;
	}
	
	public void drawEnemy(Graphics g, JFrame f)
	{
		Image enemyImg = null; 
		try
		{
			enemyImg = ImageIO.read(new File("image/e1_0.png"));
			g.drawImage(enemyImg, mPosX, mPosY, (ImageObserver)f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void drawEnemy(Graphics g, JPanel p)
	{
		if(enemyState == ENEMY_DEATH_STATE && mEnemyID < ENEMY_TYPE_MAX)
		{
			g.drawImage(enemyExploreImg[mEnemyID++], mPosX, mPosY, (ImageObserver)p);
			return;
		}
		Image enemyImg = Toolkit.getDefaultToolkit().getImage("image/e1_0.png");
		g.drawImage(enemyImg, mPosX, mPosY, (ImageObserver)p);
		
	}
	public void updateEnemy()
	{
		mPosY += ENEMY_STEP_Y;
	}
}
