// Adrian Melendez
// A1540936
// COP3503C-15Fall 0001
// FloodFill


import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Floodfill extends Applet implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	Color m_objSelectedColor = Color.blue;
	int m_nSelectedColor = 0xff0000ff;
	BufferedImage m_objShape;
	MediaTracker tracker = new MediaTracker(this);
	
	Image offScreen;
	
	int m_nTestShapeX = 100;
	int m_nTestShapeY = 100;
	
	static Color[] m_Colors =
	{
		Color.blue, Color.red, Color.green, Color.yellow,
		Color.gray, Color.magenta, Color.orange, Color.cyan
	};
	
	int m_nUpperLeftX = 10;
	int m_nUpperLeftY = 10;
	int m_nColorWidth = 50;
	int m_nColorHeight = 50;
	int m_nLowerRightX;
	int m_nLowerRightY;
	
	// To make the Selected Color obvious
	int nColorIndex = 0;
	
    CheckboxGroup lngGrp = new CheckboxGroup();
    Checkbox full = new Checkbox("Full Recursion", lngGrp, true);
    Checkbox partial = new Checkbox("Partial Recursion", lngGrp, true);
    
	public void init()
	{
		addMouseListener(this);
        setSize(1020,700);

        add(partial);
        add(full);
        
        offScreen = createImage(1020, 700);
        
        try 
        {
			m_objShape = ImageIO.read(Floodfill.class.getResourceAsStream("Untitled.png"));
			tracker.addImage(m_objShape, 100);
			tracker.waitForAll();
		} 
        catch (Exception e1) 
        {
		}
		
	}

	void DrawColors( Graphics canvas )
	{
		for( int i=0; i<m_Colors.length; i++ )
		{
			canvas.setColor( m_Colors[i] );
			canvas.fillRect(m_nUpperLeftX, m_nUpperLeftY + i * m_nColorHeight, m_nColorWidth, m_nColorHeight );
			canvas.setColor( Color.black );
			
			m_nLowerRightX = m_nUpperLeftX + m_nColorWidth;
			m_nLowerRightY = ( i + 1 ) * m_nColorHeight;
		}
		
		m_nUpperLeftX = 10;
		m_nUpperLeftY = 10;
		m_nColorWidth = 50;
		m_nColorHeight = 50;
		
		for (int i = 0; i < m_Colors.length; i++) {
			if (nColorIndex != i) {
				canvas.drawRect(m_nUpperLeftX, m_nUpperLeftY + i * m_nColorHeight, m_nColorWidth, m_nColorHeight );
			} else {
				Graphics2D g2 = (Graphics2D) canvas;
				Stroke old = g2.getStroke();
				g2.setStroke(new BasicStroke(5));
				canvas.drawRect(m_nUpperLeftX, m_nUpperLeftY + i * m_nColorHeight, m_nColorWidth, m_nColorHeight );
				g2.setStroke(old);
				
			}
			
		}
		
	}
	
	void DrawTestShape( Graphics canvas )
	{
		canvas.drawImage(m_objShape, m_nTestShapeX, m_nTestShapeY, null);
	}
	
	void SetPixel( int x, int y, Graphics canvas )
	{
		canvas.drawLine(x, y, x, y);
	}
	
	void SetPixel( int x, int y, int nColor )
	{
		m_objShape.setRGB(x, y, nColor);
	}
	
	public int GetPixel( int x, int y )
	{
		return( m_objShape.getRGB(x, y) );
	}
	
	public void paint( Graphics canvas )
	{
		DrawColors( canvas );
		DrawTestShape( canvas );
	}
	
	public void repaint() {
		Graphics g = offScreen.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1020, 700);
		paint(g);
		getGraphics().drawImage(offScreen, 0, 0, null);
	}
	
	void DoRecursiveFill( int x, int y )
	{
		x -= m_nTestShapeX;
		y -= m_nTestShapeY;
		m_nStartColor = GetPixel(x,y) | 0xff000000;
		Graphics canvas = getGraphics();
		canvas.setColor( m_objSelectedColor );
		
		int w = m_objShape.getWidth();
		int h = m_objShape.getHeight();

		if( m_nStartColor == m_nSelectedColor)
		{
			return;
		}
		
		RecursiveFill( x, y, w, h, canvas);
	}
	
	void RecursiveFill( int x, int y, int w, int h, Graphics canvas )
	{
		// Base Cases
		if (x < 0 || x >= w || y < 0 || y >= h || 
				GetPixel(x, y) == m_nSelectedColor || 
				GetPixel(x, y) != m_nStartColor || 
				GetPixel(x, y) == -16777216){ // -16777216 is Black
			return;
		}
		
		// Setting the current pixel
		SetPixel(x + 100, y + 100, canvas);
		SetPixel(x, y, m_nSelectedColor);
		
		// Go Up, Down, Left, Right (not in that order)
		// This will blow up the stack quickly
		RecursiveFill(x + 1, y, w, h, canvas);
		RecursiveFill(x - 1, y, w, h, canvas);
		RecursiveFill(x, y + 1, w, h, canvas);
		RecursiveFill(x, y - 1, w, h, canvas);
	}
	
	int m_nStartX, m_nStartY, m_nStartColor;
	void DoFloodFill( int x, int y )
	{
		x -= m_nTestShapeX;
		y -= m_nTestShapeY;
		m_nStartColor = GetPixel(x,y) | 0xff000000;
		Graphics canvas = getGraphics();
		canvas.setColor( m_objSelectedColor );
		
		int w = m_objShape.getWidth();
		int h = m_objShape.getHeight();

		if( m_nStartColor == m_nSelectedColor)
		{
			return;
		}
		
		FloodFill( x, y, w, h, canvas);
	}
	
	void FloodFill( int x, int y, int w, int h, Graphics canvas )
	{
		// Base Cases
		if (x < 0 || x >= w || y < 0 || y >= h ||  
				GetPixel(x, y) != m_nStartColor ||
				GetPixel(x, y) == -16777216){
			return;
		}
		
		// Setting color of current Pixel
		SetPixel(x + 100, y + 100, canvas);
		SetPixel(x, y, m_nSelectedColor);
		
		// Looping to the left until we reach an end and saving the leftmost position
		int tempLeft = x - 1;
		while (tempLeft >= 0 && GetPixel(tempLeft, y) == m_nStartColor) {
			SetPixel(tempLeft + 100, y + 100, canvas);
			SetPixel(tempLeft, y, m_nSelectedColor);
			tempLeft--;
		}
		tempLeft++;
		
		// Looping to the Right and saving the rightmost position
		int tempRight = x + 1;
		while (tempRight < w && GetPixel(tempRight, y) == m_nStartColor) {
			SetPixel(tempRight + 100, y + 100, canvas);
			SetPixel(tempRight, y, m_nSelectedColor);
			tempRight++;
		}
		
		// Checking each pixel above and below the most recently created line
		for (int i = tempLeft; i < tempRight; i++) {
			if (y > 0 && GetPixel(i, y - 1) == m_nStartColor) {
				FloodFill(i, y - 1, w, h, canvas);
			}
			
			if (y < h - 1 && GetPixel(i, y + 1) == m_nStartColor) {
				FloodFill(i, y + 1, w, h, canvas);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent ms) 
	{
	}
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
	}
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
	}
	
	@Override
	public void mousePressed(MouseEvent ms) 
	{
		if( ms.getX() >= m_nUpperLeftX &&
				ms.getY() >= m_nUpperLeftY &&
				ms.getX() < m_nLowerRightX &&
				ms.getY() < m_nLowerRightY )
			{
				nColorIndex = ( ms.getY() - m_nUpperLeftY ) / m_nColorHeight;
				if( nColorIndex >= 0 && nColorIndex <= 7 )
				{
					m_objSelectedColor = m_Colors[nColorIndex];
					m_nSelectedColor = m_Colors[nColorIndex].getRGB();
				}
			}
			
			else if( ms.getX() >= m_nTestShapeX &&
				ms.getY()>=m_nTestShapeY &&
				ms.getX() < m_nTestShapeX + m_objShape.getWidth() &&
				ms.getY() < m_nTestShapeY + m_objShape.getHeight())
			{
				if( full.getState() )
				{
					DoRecursiveFill( ms.getX(), ms.getY());
				}
				else
				{
					DoFloodFill( ms.getX(), ms.getY());
				}
			}
		
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
	}
	
}
