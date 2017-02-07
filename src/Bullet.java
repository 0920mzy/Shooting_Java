import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class Bullet
{
	static final int BULLET_STEP_X = 3;
	static final int BULLET_STEP_Y = 15;
	static final int BULLET_TYPE_MAX = 4;
	
	public int mPosX = 0;
	public int mPosY = 0;
	
	boolean mFacus = true;
	
	private Image bulletImg[] = null;
	int mBulletID = 0;
	
	public Bullet()
	{
		bulletImg = new Image[BULLET_TYPE_MAX];
		for(int i = 0; i<BULLET_TYPE_MAX; i++)
		{
			bulletImg[i] = Toolkit.getDefaultToolkit().getImage("image/bullet_"+i+".png");
		}
	}
	
	public void init(int x, int y)
	{
		mPosX = x;
		mPosY = y;
		mFacus = true;
	}
	
	public void drawBullet(Graphics g, JPanel p)
	{
		g.drawImage(bulletImg[mBulletID++], mPosX, mPosY, (ImageObserver)p);
		if(mBulletID >= BULLET_TYPE_MAX)
			mBulletID = 0;
	}
	
	public void updateBullet()
	{
		if(mFacus)
		{
			mPosY -= BULLET_STEP_Y;
		}
	}
}
