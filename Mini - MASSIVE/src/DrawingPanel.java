import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * <hr>
 * General Description: <br />
 * A DrawingPanel is a window in which simple graphic elements can be drawn. It
 * includes a status bar at the bottom of the graphics window that displays
 * information about the location of the mouse, which mouse buttons are being
 * held down, and which keys are being held down. It allows a client program to
 * easily perform actions based on the state of the mouse and keyboard input.
 * 
 * @author Dr. Wayne Brown <Wayne.Brown@usafa.edu>
 * 
 * @version 1.4 16-July-2013
 * 
 */

// =====================================================================
public class DrawingPanel extends Thread
    implements MouseInputListener, KeyListener {

  public static final int  LEFT_BUTTON         = 0;
  public static final int  MIDDLE_BUTTON       = 1;
  public static final int  RIGHT_BUTTON        = 2;
  public static final int  ANY_BUTTON          = 3;
  private static final int NO_BUTTON           = 4;

  public static final int  NO_KEY_PRESSED      = 0;
  public static final int  ANY_KEY             = 1;

  public static final int  F1_KEY              = KeyEvent.VK_F1;
  public static final int  F2_KEY              = KeyEvent.VK_F2;
  public static final int  F3_KEY              = KeyEvent.VK_F3;
  public static final int  F4_KEY              = KeyEvent.VK_F4;
  public static final int  F5_KEY              = KeyEvent.VK_F5;
  public static final int  F6_KEY              = KeyEvent.VK_F6;
  public static final int  F7_KEY              = KeyEvent.VK_F7;
  public static final int  F8_KEY              = KeyEvent.VK_F8;
  public static final int  F9_KEY              = KeyEvent.VK_F9;
  public static final int  F10_KEY             = KeyEvent.VK_F10;
  public static final int  F11_KEY             = KeyEvent.VK_F11;
  public static final int  F12_KEY             = KeyEvent.VK_F12;

  public static final int  LEFT_ARROW_KEY      = KeyEvent.VK_LEFT;
  public static final int  RIGHT_ARROW_KEY     = KeyEvent.VK_RIGHT;
  public static final int  UP_ARROW_KEY        = KeyEvent.VK_UP;
  public static final int  DOWN_ARROW_KEY      = KeyEvent.VK_DOWN;

  public static final int  INSERT_KEY          = KeyEvent.VK_INSERT;
  public static final int  HOME_KEY            = KeyEvent.VK_HOME;
  public static final int  DELETE_KEY          = KeyEvent.VK_DELETE;
  public static final int  END_KEY             = KeyEvent.VK_END;
  public static final int  PAGE_UP_KEY         = KeyEvent.VK_PAGE_UP;
  public static final int  PAGE_DOWN_KEY       = KeyEvent.VK_PAGE_DOWN;

  public static final int  ESC_KEY             = KeyEvent.VK_ESCAPE;
  public static final int  TAB_KEY             = KeyEvent.VK_TAB;
  public static final int  SHIFT_KEY           = KeyEvent.VK_SHIFT;
  public static final int  ENTER_KEY           = KeyEvent.VK_ENTER;
  public static final int  SPACE_KEY           = KeyEvent.VK_SPACE;

  private static final int INITIAL_DELAY       = 250;                  // milliseconds

  private static final int STATUS_BAR_HEIGHT   = 30;

  private static final int MAXIMUM_ACTIVE_KEYS = 256;

  // @formatter:off

  // The window objects
  private int              width, height;     // dimensions of window frame
  private JFrame           frame;             // overall window frame
  private MyCanvas         canvas;            // drawing canvas for window (inside panel)
  private BufferedImage    image;             // remembers drawing commands
  private Graphics2D       offscreenGraphics; // buffered graphics context for painting
  private JLabel           statusBar;         // status bar showing mouse position
  private Thread           application;

  // The status of interactive mouse clicks
  private boolean []       buttonDown;
  private boolean []       mouseClicked;      // each button is tracked separately
  private int []           mouseClickedX;
  private int []           mouseClickedY;

  // A mouse click that ends a "waitForMouseClick" blocking action
  private int              waitingForThisButton;
  private int              mostRecentMouseButton;
  private int              mostRecentMouseX;
  private int              mostRecentMouseY;

  // A key event that ends a "waitForKey()" event.
  private boolean          waitingForKey;
  private int              mostRecentKeyHit;

  // Key events are tracked. A key that is held down calls the 
  // "keyPressed" event handler repeatedly, but we only what to report the key
  private int              numberActiveKeys;
  private int []           activeKeys;
  private boolean []       keyHasBeenRetreivedByApplication;
  
  // Interactive status of individual keys.
  private boolean []       keyIsDown;
  private int              indexOfKeyToReturn;
  
  private String           debug;
  
  // @formatter:on

  // ----------------------------------------------------------------------------
  /**
   * Construct a drawing panel of a given width and height enclosed in a window.
   * 
   * @param desiredWidth the width of the drawing panel window, in pixels
   * @param desiredHeight the height of the drawing panel window, in pixels
   */
  public DrawingPanel(int desiredWidth, int desiredHeight) {
    // Keep a reference to the user application so that it can be suspended to
    // wait for mouse and keyboard events.
    width = desiredWidth;
    height = desiredHeight;
    application = Thread.currentThread();

    // Start the drawing panel in its own thread
    this.run();
  }

  // ----------------------------------------------------------------------------
  /*
   * Run the GUI drawing panel in a separate thread so that the application that
   * is drawing to the window can be paused and restarted. (run() is never
   * called by an application).
   */
  public void run() {
    // Construct a buffered image (an offscreen image that is stored in RAM)
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    offscreenGraphics = image.createGraphics();
    offscreenGraphics.setColor(Color.BLACK);

    // Create a AWT canvas object that can be drawn on - the offscreen image
    // will
    // be drawn onto this canvas
    canvas = new MyCanvas(this);
    canvas.setPreferredSize(new Dimension(width, height));
    canvas.setBounds(0, 0, width, height);
    canvas.setBackground(Color.WHITE);
    canvas.addMouseListener(this);
    canvas.addMouseMotionListener(this);
    canvas.addKeyListener(this);

    // Create a swing label to display the location of the cursor
    statusBar = new JLabel(" ");
    statusBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    statusBar.setBackground(Color.WHITE);
    statusBar.setForeground(Color.BLACK);
    statusBar.setBounds(0, STATUS_BAR_HEIGHT, width, 20);

    // Create the window
    frame = new JFrame();
    frame.setTitle("Drawing Panel");
    frame.setResizable(false);

    frame.setLayout(new BorderLayout());
    frame.getContentPane().add(canvas, "North");
    frame.getContentPane().add(statusBar, "South");

    frame.pack();
    frame.setFocusable(true);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.toFront();

    // Initialize the mouse states

    // Mouse status for interactive tracking of the mouse and its buttons
    mouseClicked = new boolean[4];
    mouseClickedX = new int[4];
    mouseClickedY = new int[4];
    buttonDown = new boolean[4];
    for (int j = 0; j < 4; j++) {
      mouseClicked[j] = false;
      mouseClickedX[j] = 0;
      mouseClickedY[j] = 0;
      buttonDown[j] = false;
    }

    // Mouse status for the blocking function waitForMouseClick
    waitingForThisButton = NO_BUTTON;
    mostRecentMouseButton = NO_BUTTON;
    mostRecentMouseX = -1;
    mostRecentMouseY = -1;

    // Initialize the key (keyboard) states
    activeKeys = new int[MAXIMUM_ACTIVE_KEYS];
    keyHasBeenRetreivedByApplication = new boolean[MAXIMUM_ACTIVE_KEYS];
    keyIsDown = new boolean[MAXIMUM_ACTIVE_KEYS];
    numberActiveKeys = 0;
    indexOfKeyToReturn = -1;
    waitingForKey = false;
    mostRecentKeyHit = 0;

    debug = "";

    // Wait for one second to give the swing library time to create the GUI
    // and get it on the screen. This makes sure that the AWT canvas
    // exists on the screen by the time the application tries to display
    // graphics to it.
    sleep(INITIAL_DELAY);

    // Make the canvas have the focus so that events are immediately sent to it.
    canvas.requestFocus();
    frame.setAlwaysOnTop(false);
  }

  // ----------------------------------------------------------------------------
  /**
   * Set the window's name -- which appears in the window's header.
   * 
   * @param name the name of the window
   */
  public void setWindowTitle(String name) {
    frame.setTitle(name);
  }

  // ----------------------------------------------------------------------------
  /**
   * Close the DrawingPanel. (This actually closes the JFrame that encloses the
   * drawing panel.) The window will disappear from the screen. Any attempts to
   * access the DrawingPanel after it is closed will generate an error.
   */
  public void closeWindow() {
    frame.dispose();
  }

  // ----------------------------------------------------------------------------
  /**
   * Return a reference to the JFrame window so that the application can modify
   * the window, such as change its location.<br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *    JFrame windowFrame = window.getWindow();
   *    windowFrame.setLocation(500, 600);
   *    windowFrame.setAlwaysOnTop(true);
   * </code>
   * </pre>
   */
  public JFrame getWindow() {
    return frame;
  }

  // ============================================================================
  // Implement all the MouseInputListener methods
  // ============================================================================

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  private void updateStatusBar() {
    final String [] buttonNames = {"left ", "middle ", "right "};

    // Start with the current location of the mouse.
    String text = "(" + mostRecentMouseX + ", " + mostRecentMouseY + ")";

    // If any of the buttons are down, shown which ones.
    String buttonStatus = "";
    for (int button = 0; button < 3; button++) {
      if (buttonDown[button]) {
        buttonStatus += buttonNames[button];
      }
    }

    if (buttonStatus.length() > 0) {
      text += "  Button(s) down: " + buttonStatus;
    }

    // Add the active keys to the status line
    String activeKeysStatus = "";
    if (numberActiveKeys > 0) {
      for (int j = 0; j < numberActiveKeys; j++) {
        if (keyIsDown[j]) {
          activeKeysStatus += " " + getKeyString(activeKeys[j]);
        }
      }
    }
    if (activeKeysStatus.length() > 0) {
      text += " Key(s) down: " + activeKeysStatus;
    }

    text += debug;

    statusBar.setText(text);
  }

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  public void mouseMoved(MouseEvent e) {
    mostRecentMouseX = e.getX();
    mostRecentMouseY = e.getY();
    updateStatusBar();
  }

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  public void mouseEntered(MouseEvent e) {
    mouseMoved(e);
  }

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  public void mouseExited(MouseEvent e) {
    mostRecentMouseX = -1;
    mostRecentMouseY = -1;
    updateStatusBar();
  }

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  public void mousePressed(MouseEvent e) {

    int mouseButtonIndex = convertMouseEventCodeToArrayIndex(e.getButton());

    mostRecentMouseButton = mouseButtonIndex;
    mostRecentMouseX = e.getX();
    mostRecentMouseY = e.getY();

    buttonDown[mostRecentMouseButton] = true;
    updateStatusBar();
  }

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  public void mouseReleased(MouseEvent e) {

    int mouseButtonIndex = convertMouseEventCodeToArrayIndex(e.getButton());

    mostRecentMouseButton = mouseButtonIndex;
    mostRecentMouseX = e.getX();
    mostRecentMouseY = e.getY();

    buttonDown[mostRecentMouseButton] = false;

    mouseClicked[mostRecentMouseButton] = true;
    mouseClickedX[mostRecentMouseButton] = mostRecentMouseX;
    mouseClickedY[mostRecentMouseButton] = mostRecentMouseY;
    updateStatusBar();
  }

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  public void mouseDragged(MouseEvent e) {
    mouseMoved(e);
  }

  // ----------------------------------------------------------------------------
  /*
   * MouseInputListener Callback. (The application never calls this method.)
   * 
   * @param e the mouse event
   */
  public void mouseClicked(MouseEvent e) {

    int mouseButtonIndex = convertMouseEventCodeToArrayIndex(e.getButton());

    // Get the mouse click information and save it for retrieval later
    mouseClicked[mouseButtonIndex] = true;
    mouseClickedX[mouseButtonIndex] = e.getX();
    mouseClickedY[mouseButtonIndex] = e.getY();

    // System.out.println("Recognized click button " + whichButton);
    if (waitingForThisButton == ANY_BUTTON
        || waitingForThisButton == mouseButtonIndex) {

      // Stop waiting and restart the application thread
      // System.out.println("mouse click = " + mouseButton + " x = " + mouseX +
      // " y = " + mouseY);
      // Resume the application
      synchronized (application) {
        application.notify();
      }

      // The application is not waiting for this button any more.
      waitingForThisButton = NO_BUTTON;

    }
  }

  // ----------------------------------------------------------------------------
  /*
   * Convert a mouse event button code into an array index.
   * 
   * @param mouseEventCode the button code for button that generated an event
   * 
   * @return an index into an array of button data.
   */
  // 
  private int convertMouseEventCodeToArrayIndex(int mouseEventCode) {
    if (mouseEventCode == MouseEvent.BUTTON1)
      return LEFT_BUTTON;
    else if (mouseEventCode == MouseEvent.BUTTON2)
      return MIDDLE_BUTTON;
    else if (mouseEventCode == MouseEvent.BUTTON3)
      return RIGHT_BUTTON;
    else
      return NO_BUTTON;
  }

  // ============================================================================
  // Implement the "KeyListener" methods
  // ============================================================================

  // ----------------------------------------------------------------------------
  /*
   * Internal method to search for a key to see if it is in the "active key"
   * list (applications will never call this method).
   * 
   * @param keyCode the code for a particular key
   */
  private int findKeyCodeIndexInList(int keyCode) {
    for (int j = 0; j < numberActiveKeys; j++) {
      if (activeKeys[j] == keyCode)
        return (j);
    }
    return (-1);
  }

  // ----------------------------------------------------------------------------
  // /**
  // * Internal debugging method - print the keyboard buffer.
  // */
  // private void printActiveKeys(String description)
  // {
  // // Debugging
  // System.out.print(description + numberActiveKeys + " keys in buffer: ");
  // for (int j=0; j<numberActiveKeys; j++)
  // System.out.printf("%d %c %b %b ", activeKeys[j], (char) activeKeys[j],
  // keyHasBeenRetreivedByApplication[j] ,keyIsDown[j] );
  // System.out.println();
  // }

  // ----------------------------------------------------------------------------
  /*
   * Internal method to remove a key from the "active key" list. (An application
   * will never call this method).
   * 
   * @param index which key to remove from the list
   */
  private void removeKeyFromActiveKeyList(int index) {
    for (int j = index; j < numberActiveKeys - 1; j++) {
      activeKeys[j] = activeKeys[j + 1];
      keyHasBeenRetreivedByApplication[j] =
          keyHasBeenRetreivedByApplication[j + 1];
      keyIsDown[j] = keyIsDown[j + 1];
    }

    numberActiveKeys--;
    if (numberActiveKeys < 0) {
      numberActiveKeys = 0;
    }
  }

  // ----------------------------------------------------------------------------
  /*
   * Callback method for "key pressed" events (never call explicitly).
   * 
   * Known problem: if multiple keys are being held down simultaneously, the
   * operating system does not always generate new key pressed events for new
   * keys pressed.
   * 
   * @param e key event
   */
  public void keyPressed(KeyEvent e) {
    // System.out.printf("KEY PRESSED EVENT  keycode = %d %c\n", e.getKeyCode(),
    // e.getKeyCode());

    // Add this key code to the list of keys that are currently down
    if (numberActiveKeys < MAXIMUM_ACTIVE_KEYS) {
      int keyCode = e.getKeyCode();

      // Only add the key to the active list if it is not already in the list.
      // Note: the operating system is sending repeated events for the
      // same key if the key is being held down.
      int index = findKeyCodeIndexInList(keyCode);
      if (index == -1) {
        // This is the first report of the key being pressed, so add it to the
        // list.
        index = numberActiveKeys;
        activeKeys[index] = keyCode;
        numberActiveKeys++;
      }

      // Update the status of the key.
      keyHasBeenRetreivedByApplication[index] = false;
      keyIsDown[index] = true;

      updateStatusBar();
    }
  }

  // ----------------------------------------------------------------------------
  /*
   * Callback method for "key release" events (never call explicitly).
   * 
   * @param e key event
   */
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();

    // find the key in the list of active keys.
    int index = findKeyCodeIndexInList(keyCode);

    // Remove the key if it has been consumed at least once by the application.
    if (index >= 0) {
      keyIsDown[index] = false;
      if (keyHasBeenRetreivedByApplication[index]) {
        removeKeyFromActiveKeyList(index);
      }
    }

    updateStatusBar();

    // If the application is waiting for a key hit, wake the application up
    if (waitingForKey) {
      // Set this key event as the most recent key released.
      mostRecentKeyHit = keyCode;

      synchronized (application) {
        application.notify(); // stop waiting
      }

      waitingForKey = false;
    }
  }

  // ----------------------------------------------------------------------------
  /*
   * Callback method for "key typed" events (never used - never call
   * explicitly).
   */
  public void keyTyped(KeyEvent e) {
  }

  // ============================================================================
  // Implement the "DrawingPanel's" methods
  // ============================================================================

  // ----------------------------------------------------------------------------
  /**
   * Obtain the Graphics object needed to draw on the DrawingPanel's offscreen
   * graphics buffer. Make sure you call copyGraphicsToScreen() after all of your
   * drawing methods have been called to copy the offscreen graphics to the
   * screen. <br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *    Graphics2D pen = window.getGraphics();
   *    pen.setColor(Color.RED);
   *    pen.fillOval(50, 50, 100, 100);
   *    window.copyGraphicsToScreen();
   * </code>
   * <pre>
   */
  //
  public Graphics2D getGraphics() {
    return offscreenGraphics;
  }

  // ----------------------------------------------------------------------------
  /**
   * Clears the DrawingPanel's graphics window to the specified color. All
   * previously drawn graphics are erased. (Note: this changes the offscreen 
   * graphics window. You will not see the new background until you call 
   * copyGraphicsToScreen().) <br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *    window.setBackground(Color.RED);
   * </code>
   * <pre>
   * 
   * @param c the color to use for the cleared background
   */
  public void setBackground(Color c) {
    // remember the current color so it can be restored
    Color currentColor = offscreenGraphics.getColor();

    offscreenGraphics.setColor(c);
    offscreenGraphics.fillRect(0, 0, width, height);

    // restore color
    offscreenGraphics.setColor(currentColor);
  }

  // ----------------------------------------------------------------------------
  /**
   * Copy the offscreen graphics buffer to the screen. No graphics are visible
   * until this method is called. This is an "expensive operation" and it should 
   * be called as few times as possible.
   */
  public void copyGraphicsToScreen() {
    Graphics2D myG = (Graphics2D) canvas.getGraphics();
    myG.drawImage(image, 0, 0, width, height, null);
  }

  // ----------------------------------------------------------------------------
  /**
   * Make your application "sleep" for the specified number of milliseconds.
   * 
   * @param millis the number of milliseconds to sleep
   */
  public void sleep(int millis) {
    synchronized (application) {
      try {
        Thread.sleep(millis);
      } catch (InterruptedException e) {
      }
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Wait for a mouse click on a specific (or any) mouse button. Your
   * application goes to sleep until the specified mouse button has been
   * clicked. (A mouse click is a rapid press and release of a mouse button at a
   * specific location. To be recognized as a "click" the mouse cursor can't
   * move its location between the press and the release.) <br /> <br />
   * 
   * When your program resumes execution after a mouse click, you can get the location of
   * the mouse by calling getMouseClickX(whichButton) and
   * getMouseClickY(whichButton). The functions getMouseX() and getMouseY() can also be called, but they
   * return the current location of the mouse, which may be different than the
   * location of the click if the user is moving the mouse around quickly. <br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *   window.waitForMouseClick(DrawingPanel.RIGHT_BUTTON);
   *   int mouseX = window.getMouseClickX(DrawingPanel.RIGHT_BUTTON);
   *   int mouseY = window.getMouseClickY(DrawingPanel.RIGHT_BUTTON);
   *   // do something at (mouseX, mouseY)
   * </code>
   * </pre>
   *   
   * @param whichButton which mouse button you want to wait for. It must be one of the following values:
   *          <ul>
   *          <li>DrawingPanel.LEFT_BUTTON</li>
   *          <li>DrawingPanel.MIDDLE_BUTTON</li>
   *          <li>DrawingPanel.RIGHT_BUTTON</li>
   *          <li>DrawingPanel.ANY_BUTTON</li>
   *          </ul>
   */
  public void waitForMouseClick(int whichButton) {
    if (whichButton == LEFT_BUTTON || whichButton == MIDDLE_BUTTON
        || whichButton == RIGHT_BUTTON || whichButton == ANY_BUTTON) {
      waitingForThisButton = whichButton;
      // System.out.println("Waiting for button " + whichButton +
      // ". Application waiting.");
      synchronized (application) {
        try {
          application.wait();
        } catch (InterruptedException e) {
        }
        ;
      }
    } else {
      System.out.printf("%d is an invalid parameter to waitForMouseClick\n",
          whichButton);
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Determine if a mouse click has occurred on a specific button. Calling this
   * method always resets the status of the specified button to "unclicked" so
   * that mouse button clicks are only processed once. If
   * you want the location of the click, immediately call
   * getMouseClickX(whichButton) and/or getMouseClickY(whichButton). This method 
   * is typically called inside an if-statement to perform operations only if 
   * the user has clicked a specific mouse button.<br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *   if ( window.mouseClickHasOccurred(DrawingPanel.LEFT_BUTTON) ) {
   *     int mouseX = window.getMouseClickX(DrawingPanel.LEFT_BUTTON);
   *     int mouseY = window.getMouseClickY(DrawingPanel.LEFT_BUTTON);
   *     // do something at (mouseX, mouseY)
   *   }
   * </code>
   * </pre>
   * 
   * @param whichButton which mouse button you want to check for a click
   *          (DrawingPanel.LEFT_BUTTON, DrawingPanel.MIDDLE_BUTTON,
   *          DrawingPanel.RIGHT_BUTTON, or DrawingPanel.ANY_BUTTON).
   * 
   * @return true if a mouse click using the specified button has occurred since
   *         the last call to this method, false otherwise.
   */
  public boolean mouseClickHasOccurred(int whichButton) {

    // I don't know why this is needed, but if you don't slow the DrawingPanel
    // thread down, it misses events. This is very baffling!
    this.sleep(1);

    if (whichButton == ANY_BUTTON) {
      // If any button has been clicked, reset all buttons
      if (mouseClicked[LEFT_BUTTON] || mouseClicked[MIDDLE_BUTTON]
          || mouseClicked[RIGHT_BUTTON]) {
        mouseClicked[LEFT_BUTTON] = false;
        mouseClicked[MIDDLE_BUTTON] = false;
        mouseClicked[RIGHT_BUTTON] = false;
        return true;
      } else {
        return false;
      }
    } else if (whichButton == LEFT_BUTTON || whichButton == MIDDLE_BUTTON
        || whichButton == RIGHT_BUTTON) {
      boolean status = mouseClicked[whichButton];
      mouseClicked[whichButton] = false;
      return status;
    } else {
      System.out.printf(
          "%d is an invalid parameter to mouseClickHasOccurred\n", whichButton);
    }
    return false;
  }

  // ----------------------------------------------------------------------------
  /**
   * Determine if a specific mouse button is down at the time of the method
   * call. If the input parameter is ANY_BUTTON, then it returns true if any of
   * the mouse buttons are down. Calling this method does not change the status
   * of the mouse button. This method is typically used to control an if-statement
   * or a loop.<br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *   while ( window.isMouseButtonDown(DrawingPanel.LEFT_BUTTON) ) {
   *     int mouseX = window.getMouseX(DrawingPanel.LEFT_BUTTON);
   *     int mouseY = window.getMouseY(DrawingPanel.LEFT_BUTTON);
   *     // do something at (mouseX, mouseY)
   *   }
   * </code>
   * </pre>
   * 
   * @param whichButton which mouse button you want to check the status of
   *          (DrawingPanel.LEFT_BUTTON, DrawingPanel.MIDDLE_BUTTON, or
   *          DrawingPanel.RIGHT_BUTTON, or DrawingPanel.ANY_BUTTON).
   * 
   * @return true if a mouse button is down, false otherwise.
   */
  public boolean isMouseButtonDown(int whichButton) {
    // System.out.println("whichButton = " + whichButton);

    // I don't know why this is needed, but if you don't slow the DrawingPanel
    // thread down, it misses events. This is very baffling!
    this.sleep(1);

    boolean status = false;

    if (whichButton == LEFT_BUTTON || whichButton == MIDDLE_BUTTON
        || whichButton == RIGHT_BUTTON) {
      status = buttonDown[whichButton];
      // debug = "0: " + whichButton; // + " " + buttonDown[0]; // + " " +
      // buttonDown[1]; // + " " + buttonDown[2] + " ";
    } else if (whichButton == ANY_BUTTON) {
      status =
          (buttonDown[LEFT_BUTTON] || buttonDown[MIDDLE_BUTTON] || buttonDown[RIGHT_BUTTON]);
      // debug = "1 button = " + whichButton;
    } else {
      System.out.printf("%d is an invalid parameter to isMouseButtonDown\n",
          whichButton);
      // debug = "2 button = " + whichButton;
    }
    return status;
  }

  // ----------------------------------------------------------------------------
  /**
   * Get which mouse button was most recently clicked. If no mouse button has
   * been clicked, it returns an invalid button value. This method should not be
   * called unless the mouseClickHasOccurred() method indicates a mouse click
   * has happened.<br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *   if ( window.mouseClickHasOccurred() ) {
   *     int whichButton = getMouseButton();
   *     int mouseX = window.getMouseClickX(whichButton);
   *     int mouseY = window.getMouseClickY(whichButton);
   *     // do something at (mouseX, mouseY)
   *   }
   * </code>
   * </pre>
   * 
   * @return button code (DrawingPanel.LEFT_BUTTON, DrawingPanel.MIDDLE_BUTTON,
   *         or DrawingPanel.RIGHT_BUTTON)
   */
  public int getMouseButton() {
    return mostRecentMouseButton;
  }

  // ----------------------------------------------------------------------------
  /**
   * Get the current x coordinate of the mouse. This is <b>not</b> associated
   * with any mouse clicks or the status of the mouse buttons.
   * 
   * @return x coordinate of the mouse in pixels (left side of window is 0,
   *         right side is the (DrawingPanel's width - 1).
   */
  public int getMouseX() {
    return mostRecentMouseX;
  }

  // ----------------------------------------------------------------------------
  /**
   * Get the current y coordinate of the mouse. This is <b>not</b> associated
   * with any mouse clicks or the status of the mouse buttons.
   * 
   * @return y coordinate of the mouse in pixels (top of window is 0, bottom is
   *         the (DrawingPanel's height - 1).
   */
  public int getMouseY() {
    return mostRecentMouseY;
  }

  // ----------------------------------------------------------------------------
  /**
   * Get the x coordinate of the mouse's location when the most recent mouse
   * click of the specified button occurred. Call the
   * mouseClickHasOccurred(whichButton) method to determine if a mouse click has
   * occurred. If no mouse click has occurred, this method returns an invalid x
   * coordinate.
   * 
   * @return x coordinate of the mouse (in pixels) when the most recent mouse
   *         click for the specified button occurred.
   */
  public int getMouseClickX(int whichButton) {
    if (whichButton >= LEFT_BUTTON && whichButton <= RIGHT_BUTTON) {
      mouseClicked[whichButton] = false;
      return mouseClickedX[whichButton];
    } else {
      System.out.printf("%d is an invalid parameter to getMouseClickX\n",
          whichButton);
    }
    return -1; // bogus value
  }

  // ----------------------------------------------------------------------------
  /**
   * Get the y coordinate of the mouse's location when the most recent mouse
   * click of the specified button occurred. Call the
   * mouseClickHasOccurred(whichButton) method to determine if a mouse click has
   * occurred. If no mouse click has occurred, this method returns an invalid y
   * coordinate.
   * 
   * @return y coordinate of the mouse (in pixels) when the most recent mouse
   *         click for the specified button occurred.
   */
  public int getMouseClickY(int whichButton) {
    if (whichButton >= LEFT_BUTTON && whichButton <= RIGHT_BUTTON) {
      mouseClicked[whichButton] = false;
      return mouseClickedY[whichButton];
    } else {
      System.out.printf("%d is an invalid parameter to getMouseClickY\n",
          whichButton);
    }
    return -1;
  }

  // ----------------------------------------------------------------------------
  /**
   * Wait for the user to hit a key on the keyboard -- your application goes to
   * sleep until a key is pressed and released. You can call the getKeyHitCode() 
   * method to determine which key was hit.<br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *   while (true) {
   *     window.waitForKey();
   *     int keyCode = window.getKeyHitCode();
   *     
   *     if (keyCode == (int) 'R') {
   *       pen.setColor(Color.RED);
   *     } else if (keyCode == (int) 'G') {
   *       pen.setColor(Color.GREEN);
   *     } else if (keyCode == (int) 'B') {
   *       pen.setColor(Color.BLUE);
   *     } else if (keyCode == DrawingPanel.ESC_KEY) {
   *       break;
   *     }
   *     
   *     // draw something
   *   }
   * </code>
   * </pre>
   * 
   */
  //
  public void waitForKey() {
    waitingForKey = true;
    synchronized (application) {
      try {
        application.wait();
      } catch (InterruptedException e) {
      }
      ;
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Is the specified key down at this moment in time?<br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *   int x = window.mouseX();
   *   int y = window.mouseY();
   *   while ( isKeyDown(DrawingPanel.UP_ARROW_KEY) ) {
   *     pen.fillOval(x,y,20,20);
   *     y -= 1;
   *     window.copyGraphicsToScreen();
   *   }
   * </code>
   * </pre>
   * 
   * @param whichKeyCode which key to check
   * 
   * @return true if the specified key is down, false otherwise.
   */
  public boolean isKeyDown(int whichKeyCode) {
    if (whichKeyCode == ANY_KEY) {
      // if any key is in the active buffer and down, then return true
      for (int j = 0; j < numberActiveKeys; j++)
        if (keyIsDown[j])
          return true;
    } else {
      int index = findKeyCodeIndexInList(whichKeyCode);
      if (index >= 0)
        return keyIsDown[index];
    }
    return false;
  }

  // ----------------------------------------------------------------------------
  /**
   * Has the specified key been hit (pressed and released)?<br /> <br />
   * 
   * Example code:
   * <pre>
   * <code>
   *   if ( window.keyHasBeenHit('R') ) {
   *     pen.setColor(Color.RED);
   *   }
   * </code>
   * </pre>
   * 
   * @param whichKeyCode which key to check
   * 
   * @return true if the specified key has been hit, false otherwise.
   */
  public boolean keyHasBeenHit(int whichKeyCode) {
    if (whichKeyCode == ANY_KEY) {
      // if any key is in the active buffer and not down, then return true
      for (int j = 0; j < numberActiveKeys; j++)
        if (!keyIsDown[j]) {
          removeKeyFromActiveKeyList(j);
          return true;
        }
    } else // there is a specific key the user wants to query
    {
      int index = findKeyCodeIndexInList(whichKeyCode);
      if (index >= 0) {
        if (!keyIsDown[index] && !keyHasBeenRetreivedByApplication[index]) {
          removeKeyFromActiveKeyList(index);
          return true;
        }
      }
    }
    return false;
  }

  // ----------------------------------------------------------------------------
  /**
   * Returns a single "key code" for a key that was <b>pressed and released</b>.
   * If multiple keys are hit at the same time, the order of the keys returned
   * is indeterminate. Key hits are buffered. Therefore, if multiple keys are
   * hit at the same time, subsequent calls to getKeyHitCode will return the
   * other keys. A "key hit" will only be returned once. To determine the number
   * of keys that have been hit, call numberOfKeysHit().<br /> <br />
   * 
   * Example code if the user will only hit one key at a time:
   * <pre>
   * <code>
   *   window.waitForKey();
   *   int keyCode = window.getKeyHitCode();
   *   
   *   if ( keyCode == DrawingPanel.DELETE_KEY) ) {
   *     // do something
   *   } else if ( keyCode == DrawingPanel.HOME_KEY) ) {
   *     // do something
   *   }
   * </code>
   * </pre>
   * <br /> <br />
   * 
   * Example code if the user might hit more than one key at a time:
   * <pre>
   * <code>
   *   window.waitForKey();
   *   int numberKeys = window.numberOfKeysHit();
   *   for (int j=0; j&#60;numberKeys; j++) {
   *     int keyCode = window.getKeyHitCode();
   *     
   *     if ( keyCode == DrawingPanel.DELETE_KEY) ) {
   *       // do something
   *     } else if ( keyCode == DrawingPanel.HOME_KEY) ) {
   *       // do something
   *     }
   *   }
   * </code>
   * </pre>
   *    * 
   * @return returns a key code for the latest key that was pressed and released
   *         by a user.
   * 
   *         <p>
   *         The integer values represents the key that was pressed (and not the
   *         upper or lower case of the key).
   *         <ul>
   *         <li>For the digits, the codes are equivalent to the unicode
   *         character '0' - '9'</li>
   *         <li>For the characters, the codes are equivalent to the unicode
   *         characters 'A' - 'Z' (NOTE: CAPITAL LETTERS)</li>
   *         <li>For the function keys, use the named constants F1_KEY, ...
   *         F12_KEY</li>
   *         <li>For the other special keys, use:
   *         <ul>
   *         <li>DrawingPanel.LEFT_ARROW_KEY</li>
   *         <li>DrawingPanel.RIGHT_ARROW_KEY</li>
   *         <li>DrawingPanel.UP_ARROW_KEY</li>
   *         <li>DrawingPanel.DOWN_ARROW_KEY</li>
   *         <li>DrawingPanel.INSERT_KEY</li>
   *         <li>DrawingPanel.HOME_KEY</li>
   *         <li>DrawingPanel.DELETE_KEY</li>
   *         <li>DrawingPanel.END_KEY</li>
   *         <li>DrawingPanel.PAGE_UP_KEY</li>
   *         <li>DrawingPanel.PAGE_DOWN_KEY</li>
   *         <li>DrawingPanel.ESC_KEY</li>
   *         <li>DrawingPanel.TAB_KEY</li>
   *         <li>DrawingPanel.SHIFT_KEY</li>
   *         <li>DrawingPanel.ENTER_KEY</li>
   *         <li>DrawingPanel.SPACE_KEY</li>
   *         </ul>
   *         </li>
   *         </ul>
   * 
   */
  public int getKeyHitCode() {
    int returnKeyCode = NO_KEY_PRESSED; // assume there are no active keys

    if (numberActiveKeys > 0) {
      // Debugging
      // printActiveKeys("Start of getKeyHitCode: " );
      // System.out.println("indexOfKeyToReturn = " + indexOfKeyToReturn);

      // Make sure there is a valid index into the keys hit buffer
      if (indexOfKeyToReturn < 0 || indexOfKeyToReturn >= numberActiveKeys)
        indexOfKeyToReturn = 0;

      // Only return the active key if it has been released and it has not been
      // returned previously.
      if (!keyIsDown[indexOfKeyToReturn]
          && !keyHasBeenRetreivedByApplication[indexOfKeyToReturn]) {
        returnKeyCode = activeKeys[indexOfKeyToReturn];

        removeKeyFromActiveKeyList(indexOfKeyToReturn);
        if (indexOfKeyToReturn >= numberActiveKeys)
          indexOfKeyToReturn = 0;
      } else
        indexOfKeyToReturn = (indexOfKeyToReturn + 1) % numberActiveKeys;
    }

    return returnKeyCode;
  }

  // ----------------------------------------------------------------------------
  /*
   * Return a string that describes a specified key code.
   * 
   * @return a string that describes a specified key code.
   */
  private String getKeyString(int code) {
    String str;

    if ((code >= 'A' && code <= 'Z') || (code >= '0' && code <= '9')) {
      str = "" + (char) code;
    } else if (code >= F1_KEY && code <= F12_KEY) {
      str = "F" + (code - F1_KEY + 1);
    } else if (code == DrawingPanel.LEFT_ARROW_KEY) {
      str = "left";
    } else if (code == DrawingPanel.RIGHT_ARROW_KEY) {
      str = "right";
    } else if (code == DrawingPanel.UP_ARROW_KEY) {
      str = "up";
    } else if (code == DrawingPanel.DOWN_ARROW_KEY) {
      str = "down";
    } else if (code == DrawingPanel.INSERT_KEY) {
      str = "insert";
    } else if (code == DrawingPanel.HOME_KEY) {
      str = "home";
    } else if (code == DrawingPanel.DELETE_KEY) {
      str = "del";
    } else if (code == DrawingPanel.END_KEY) {
      str = "end";
    } else if (code == DrawingPanel.PAGE_UP_KEY) {
      str = "page_up";
    } else if (code == DrawingPanel.PAGE_DOWN_KEY) {
      str = "page_down";
    } else if (code == DrawingPanel.ESC_KEY) {
      str = "esc";
    } else if (code == DrawingPanel.TAB_KEY) {
      str = "tab";
    } else if (code == DrawingPanel.SHIFT_KEY) {
      str = "shift";
    } else if (code == DrawingPanel.ENTER_KEY) {
      str = "enter";
    } else if (code == DrawingPanel.SPACE_KEY) {
      str = "space";
    } else {
      str = "error";
    }

    // System.out.printf("%d %s\n", code, str);
    return str;
  }

  // ----------------------------------------------------------------------------
  /**
   * Return the number of keys that are currently being pressed down on the
   * keyboard. (Note: The Microsoft Windows operating system is sporadic in the 
   * number of simultaneous keys it will be report as being down. If a user
   * holds down 3 or more keys simultaneously, the operating system does not 
   * typically report all of the keys to the program.) 
   * 
   * @return return the number of keys that are currently being pressed on the
   *         keyboard.
   */
  public int numberOfKeysDown() {
    int count = 0;

    for (int j = 0; j < numberActiveKeys; j++)
      if (keyIsDown[j]) {
        count++;
      }

    return count;
  }

  // ----------------------------------------------------------------------------
  /**
   * Return the number of keys that have been hit but not retrieved by the
   * client program. See a code example in the description of the getKeyHitCode()
   * method.
   * 
   * @return return the number of keys that are currently being pressed on the
   *         keyboard.
   */
  public int numberOfKeysHit() {
    int count = 0;

    for (int j = 0; j < numberActiveKeys; j++)
      if (!keyIsDown[j] && !keyHasBeenRetreivedByApplication[j]) {
        count++;
      }

    return count;
  }

  // ----------------------------------------------------------------------------
  /**
   * Return an integer representation of the color at a specified pixel
   * location.
   * 
   * @param x the x coordinate of the pixel you want the color of
   * @param y the y coordinate of the pixel you want the color of
   */
  public int getRGB(int x, int y) {
    try {
      return image.getRGB(x, y);
    } catch (Exception e) {
      return 0;
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Change the color of a specified pixel to a specified color.
   * 
   * @param x the x coordinate of the pixel you want to change
   * @param y the y coordinate of the pixel you want to change
   * @param RGB the color you want to change the pixel to
   */
  public void setRGB(int x, int y, int RGB) {
    try {
      image.setRGB(x, y, RGB);
    } catch (Exception e) {
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Save the current graphics (in the offscreen buffer) to a file. The file
   * name should have an appropriate image file extension, such as ".bmp" or
   * ".jpg"
   * 
   * @param filename the name of the file (including an appropriate image file
   *          extension)
   */
  public void saveGraphics(String filename) {
    String extension = filename.substring(filename.lastIndexOf(".") + 1);

    // write file
    try {
      ImageIO.write(image, extension, new java.io.File(filename));
    } catch (java.io.IOException e) {
      System.err.println("Unable to save image:\n" + e);
    }
  }

  // ----------------------------------------------------------------------------
  /**
   * Load an image into memory.
   * 
   * @param filename the name of the image file (including the file extension)
   * 
   * @return a BufferedImage object that contains the image
   */
  public static BufferedImage loadBitmap(String filename) {
    // read file
    try {
      BufferedImage image = ImageIO.read(new java.io.File(filename));
      return image;
    } catch (java.io.IOException e) {
      System.err.println("Unable to read image file :\n" + e);
      return null;
    }
  }

}

// =====================================================================
class MyCanvas extends Canvas {

  private static final long serialVersionUID = 1L;
  // -------------------------------------------------------------------
  private DrawingPanel      panel;

  // -------------------------------------------------------------------
  public MyCanvas(DrawingPanel thisPanel) {
    super();
    panel = thisPanel;
  }

  // -------------------------------------------------------------------
  @Override
  public void paint(Graphics g) {
    panel.copyGraphicsToScreen();
  }
}

/*<hr />
 * Original Documentation by Stuart Reges and Marty Stepp (textbook authors)
 * 07/01/2005
 * 
 * The DrawingPanel class provides a simple interface for drawing persistent
 * images using a Graphics object. An internal BufferedImage object is used to
 * keep track of what has been drawn. A client of the class simply constructs a
 * DrawingPanel of a particular size and then draws on it with the Graphics
 * object, setting the background color if they so choose.
 * 
 * To ensure that the image is always displayed, a timer calls repaint at
 * regular intervals.
 * 
 * <hr />
 * Versions<br />
 * <ul>
 * <li>version 1.4 16 July 2013<br />
 * <ul>
 * <li>Fixed timing issue where the mouse status was not being reported
 * correctly.</li>
 * <li>Updated the status bar to display more information about the mouse and
 * keyboard events.</li>
 * <li>Simplified the waitForKey() blocking functions.</li>
 * <li>Code examples added to the documentation</li>
 * </ul>
 * </li>
 * 
 * <li>version 1.3 18 July 2012 <br />
 * <ul>
 * <li>getGraphics() returns a Graphics2D object (not Graphics).</li>
 * <li>getWindow() will return the JFrame that defines the window.</li>
 * <li>loadBitmap() is now a static method that can be used without creating a
 * DrawingPanel window.</li>
 * <li>waitForMouseClick() can now wait on ANY_BUTTON (not a specified mouse
 * button).</li>
 * </ul>
 * </li>
 * 
 * <li>version 1.2 31 Aug 2008 <br />
 * <ul>
 * <li>All graphics are drawn to an offscreen buffer.</li>
 * <li>Keyboard and Mouse input enhanced. Documentation enhanced.
 * <li>
 * </ul>
 * 
 * </li>
 * 
 * <li>version 1.1 19 Sept 2007 <br />
 * Added additional user input for mouse and keyboard
 * 
 * </li>
 * 
 * <li>version 1.0 July 2007 <br />
 * Original modification of textbook class</li>
 * </ul>
 * 
 * Changes made by Dr.Brown to the original code provided by the textbook:
 * <ol>
 * <li>Draws to an "offscreen graphics" window that the user must then copy to
 * the screen.</li>
 * <li>The GUI runs in a separate thread to allow for the suspension and
 * resumption of the application program (so it can wait for mouse and key
 * events).</li>
 * <li>Removed the timer that refreshed the graphics image.</li>
 * </ol>
 * <hr />
 * 
 * 
 */
