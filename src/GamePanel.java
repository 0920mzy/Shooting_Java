import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener
{
	//Screen size
	private int mScreenWidth = 320;
	private int mScreenHeight = 480;

	private static final int STATE_GAME = 0;

	private int mState = STATE_GAME;

	private Image mBitMenuBG0 = null;
	private Image mBitMenuBG1 = null;

	private int mBitposY0 = 0;	//Map 0 position
	private int mBitposY1 = 0;	//Map 1 position

	final static int BULLET_POOL_COUNT = 15;

	final static int PLAN_STEP = 10;

	final static int PLAN_TIME = 500;

	final static int ENEMY_POOL_COUNT = 5;

	final static int ENEMY_POS_OFF = 65;

	private Thread mThread = null;

	private boolean mIsRunning = false;

	public int mAirPosX = 0;
	public int mAirPosY = 0;

	Enemy mEnemy[] = null;

	Bullet mBullet[] = null;

	public int mSendId = 0;

	public Long mSendTime = 0L;
	Image myPlanePic[];
	public int myPlaneID = 0; 

	public GamePanel()
	{
		setPreferredSize(new Dimension(mScreenWidth, mScreenHeight));
		setFocusable(true);
		addKeyListener(this);
		init();
		setGameState(STATE_GAME);
		mIsRunning = true;
		mThread = new Thread(this);
		mThread.start();
		setVisible(true);
	}
	
	private void init() 
	{
		try 
		{
			mBitMenuBG0 = Toolkit.getDefaultToolkit().getImage("image/map_0.png");
			mBitMenuBG1 = Toolkit.getDefaultToolkit().getImage("image/map_1.png");
			ImageIO.read(new File("image/map_1.png"));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		mBitposY0 = 0;
		mBitposY1 = -mScreenHeight;

		mAirPosX = 150;
		mAirPosY = 400;
		myPlanePic = new Image[6];
		for (int i = 0; i < 6; i++)
			myPlanePic[i] = Toolkit.getDefaultToolkit().getImage("image/plan_" + i + ".png");

		mEnemy = new Enemy[ENEMY_POOL_COUNT];

		for (int i = 0; i < ENEMY_POOL_COUNT; i++) 
		{
			mEnemy[i] = new Enemy();
			mEnemy[i].init(i * ENEMY_POS_OFF, i * ENEMY_POS_OFF-300);
		}

		mBullet = new Bullet[BULLET_POOL_COUNT];
		for (int i = 0; i < BULLET_POOL_COUNT; i++) 
		{
			mBullet[i] = new Bullet();
		}
		mSendTime = System.currentTimeMillis();
	}

	private void setGameState(int newState) 
	{
		mState = newState;
	}
	
	public void renderBg() 
	{
		myPlaneID++;
		if (myPlaneID == 6)
			myPlaneID = 0;
		repaint();
	}

	public void paint(Graphics g) 
	{
		g.drawImage(mBitMenuBG0, 0, mBitposY0, this);
		g.drawImage(mBitMenuBG1, 0, mBitposY1, this);
		g.drawImage(myPlanePic[myPlaneID], mAirPosX, mAirPosY, this);
		for (int i = 0; i < BULLET_POOL_COUNT; i++)
			mBullet[i].drawBullet(g, this);

		for (int i = 0; i < ENEMY_POOL_COUNT; i++)
			mEnemy[i].drawEnemy(g, this);
	}

	private void updateBg() 
	{
		mBitposY0 += 10;
		mBitposY1 += 10;
		if (mBitposY0 == mScreenHeight) {
			mBitposY0 = -mScreenHeight;
		}
		if (mBitposY1 == mScreenHeight) {
			mBitposY1 = -mScreenHeight;
		}

		for (int i = 0; i < BULLET_POOL_COUNT; i++) 
		{
			mBullet[i].updateBullet();
		}

		for (int i = 0; i < ENEMY_POOL_COUNT; i++) 
		{
			mEnemy[i].updateEnemy();
			if (mEnemy[i].enemyState == Enemy.ENEMY_DEATH_STATE && 
				mEnemy[i].mEnemyID == 6 || 
				mEnemy[i].mPosY >= mScreenHeight) 
			{
				mEnemy[i].init(UtilRandom(0, ENEMY_POOL_COUNT) * ENEMY_POS_OFF,0);
			}

		}

		if (mSendId < BULLET_POOL_COUNT) 
		{
			long now = System.currentTimeMillis();
			if (now - mSendTime >= PLAN_TIME) 
			{
				mBullet[mSendId].init(mAirPosX-5, mAirPosY-40);
				mSendTime = now;
				mSendId++;
			}
		} 
		else 
		{
			mSendId = 0;
		}

		Collision();

	}

	public void Collision() 
	{
		for (int i = 0; i < BULLET_POOL_COUNT; i++) 
		{
			for (int j = 0; j < ENEMY_POOL_COUNT; j++) 
			{
				if (mBullet[i].mPosX >= mEnemy[j].mPosX && 
					mBullet[i].mPosX <= mEnemy[j].mPosX + 30 && 
					mBullet[i].mPosY >= mEnemy[j].mPosY && 
					mBullet[i].mPosY <= mEnemy[j].mPosY + 30) 
				{
					mEnemy[j].enemyState = Enemy.ENEMY_DEATH_STATE;
				}
			}

		}
	}


	private int UtilRandom(int botton, int top) 
	{
		return ((Math.abs(new Random().nextInt()) % (top - botton)) + botton);
	}

	public void run() 
	{
		while (mIsRunning) 
		{
			Draw();
			try 
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	protected void Draw() 
	{
		switch (mState) 
		{
			case STATE_GAME:
				renderBg();
				updateBg();
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
//		System.out.println(key);
		if (key == KeyEvent.VK_UP)
			mAirPosY -= PLAN_STEP;
		if (key == KeyEvent.VK_DOWN)
			mAirPosY += PLAN_STEP;
		if (key == KeyEvent.VK_LEFT)
		{
			mAirPosX -= PLAN_STEP;
			if (mAirPosX < 0)
				mAirPosX = 0;
		}
		if (key == KeyEvent.VK_RIGHT)
		{
			mAirPosX += PLAN_STEP;
			if (mAirPosX > mScreenWidth - 30)
				mAirPosX = mScreenWidth - 30;
		}
//		System.out.println(mAirPosX + ":" + mAirPosY);				
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	
}
