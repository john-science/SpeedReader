import scala.swing._
import scala.swing.event._
import GridBagPanel._
import java.awt.Color
import java.awt.event._
import java.awt.Dimension
import java.awt.Insets
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object SpeedReader extends SimpleSwingApplication {
  val starTxt = "...please enter text"
  var text = starTxt
  var txtList = text.replaceAll("\r", " ").replaceAll("\n", " ").split(" ").filterNot(_ == "")
  var iMax = txtList.length - 1
  var i = 0
  val jumpBig = 10
  val jumpSmall = 1
  var WPM = 200
  def rate: Int = (60000.0 / WPM.toFloat).toInt
  var pause: Boolean = true
  
  // define UI components outside of Panel definition,
  // so I can reference them throughout the code
  val readTxt = new Label(txtList(i))
  val back10Btn = new Button { text="<< 10"; foreground = Color.gray }
  val back1Btn = new Button { text="< 1"; foreground = Color.gray }
  val btnStart = new ToggleButton { text="Start"; foreground = Color.gray }
  val ahead1Btn = new Button { text="1 >"; foreground = Color.gray }
  val ahead10Btn = new Button { text="10 >>"; foreground = Color.gray }
  val comboLabel = new Label { text="WPM: "; foreground = Color.gray }
  val wpms = Range(200,500,25).toList.map(x => x.toString)
  val comboBox = new ComboBox(wpms)
  val loadBtn = new Button { text="Load New Text"; foreground = Color.gray }
  val textEntry = new TextArea{text=starTxt; rows=7;foreground = Color.gray; lineWrap=true}
  val scrollText = new ScrollPane(textEntry)//val scrollText = new ScrollPane{contents=textEntry; size=new Dimension(100, 100)}
  
  // layout the UI
  lazy val ui = new GridBagPanel {
    val c = new Constraints
    val shouldFill = true
    if (shouldFill) {
      c.fill = Fill.Horizontal
    }
    
    // Label used to display the text you're reading
    c.weightx = 0.5
    c.fill = Fill.Horizontal
    c.insets = new Insets(20,10,20,10);  // padding
    c.gridx = 1;
    c.gridwidth = 4
    c.gridy = 0;
    layout(readTxt) = c
    
    // button to move back 10 words
    c.insets = new Insets(0,0,0,0);
    c.gridx = 0;
    c.gridwidth = 1
    c.gridy = 1;
    layout(back10Btn) = c
    
    // button to move back 1 word
    c.gridx = 1;
    c.gridy = 1;
    layout(back1Btn) = c
    
    // "Start" button
    c.gridx = 2;
    c.gridwidth = 2
    layout(btnStart) = c
    c.gridy = 1;
    
    // button to move ahead 1 word
    c.gridx = 4;
    c.gridwidth = 1
    c.gridy = 1;
    layout(ahead1Btn) = c
    
    // button to move ahead 10 words
    c.gridx = 5;
    c.gridy = 1;
    layout(ahead10Btn) = c
    
    // "WPM: " static text
    c.insets = new Insets(25,10,0,0);
    c.gridx = 0;
    c.gridy = 2;
    layout(comboLabel) = c
    
    // drop down to choose your reading speed
    c.insets = new Insets(25,0,0,0);
    c.gridx = 1;
    c.gridy = 2;
    layout(comboBox) = c
    
    // "Load New Text" button
    c.insets = new Insets(25,0,0,10);
    c.gridx = 4;
    c.gridwidth = 2
    c.gridy = 2;
    layout(loadBtn) = c
    
    // Scrolling text input box
    c.insets = new Insets(10,10,10,10);  // padding
    c.weightx = 1.0  //request any extra horizontal space
    c.weighty = 1.0  //request any extra vertical space
    c.fill = Fill.Both
    c.gridx = 0;
    c.gridwidth = 6
    c.gridy = 3;
    layout(scrollText) = c
  }
  
  // manage GUI interaction
  def top = new MainFrame {
    title = "SpeedReader"  // Add a title text to the app
    contents = ui
    peer.setLocationRelativeTo(null)  // Center frame on the screen
    
    val s = new Dimension(300, 300)
    minimumSize = s
    maximumSize = s
    preferredSize = s
    
    listenTo(back10Btn, back1Btn, btnStart, ahead1Btn, ahead10Btn, comboBox.selection, loadBtn)
    
    reactions += {
      case ButtonClicked(component) if component == back10Btn => {
        pause = true
        btnStart.text = "Start"
        btnStart.selected = false
        if (i > jumpBig) { i -= jumpBig }
        else { i = 0 }
        readTxt.text = txtList(i)
      }
      case ButtonClicked(component) if component == back1Btn => {
        pause = true
        btnStart.text = "Start"
        btnStart.selected = false
        if (i > jumpSmall) { i -= jumpSmall }
        else { i = 0 }
        readTxt.text = txtList(i)
      }
      case ButtonClicked(component) if component == btnStart => {
        btnStart.text = if (btnStart.selected) "Pause" else "Start"
        if (btnStart.selected) {pause = false} else {pause = true}
      }
      case ButtonClicked(component) if component == ahead1Btn => {
        pause = true
        btnStart.text = "Start"
        btnStart.selected = false
        if (i < iMax - jumpSmall) { i += jumpSmall }
        else { i = iMax }
        readTxt.text = txtList(i)
      }
      case ButtonClicked(component) if component == ahead10Btn => {
        pause = true
        btnStart.text = "Start"
        btnStart.selected = false
        if (i < iMax - jumpBig) { i += jumpBig }
        else { i = iMax }
        readTxt.text = txtList(i)
      }
      case SelectionChanged(component) if component == comboBox => {
        WPM = comboBox.selection.item.split(" ")(0).toInt
      }
      case ButtonClicked(component) if component == loadBtn => {
        pause = true
        btnStart.text = "Start"
        btnStart.selected = false
        text = textEntry.text
        txtList = text.replaceAll("\r", " ").replaceAll("\n", " ").replaceAll("-", "- ").replaceAll("—", "— ").split(" ").filterNot(_ == "")
        iMax = txtList.length - 1
        i = 0
        readTxt.text = txtList(0)
      }
    }
    
  }
    
  // TODO: Does this thread get abandoned when you shut the GUI?
  // Start separate thread to play text
  val f = Future {
    while (true) {
      if (!pause && i < iMax ) {
        i += 1
        readTxt.text = txtList(i)
        if (txtList(i).contains(".")) { Thread.sleep(rate) }
      }
      Thread.sleep(rate)
    }
  }
  
  f.onComplete {
    case Success(value) => pause = true
    case Failure(e) => e.printStackTrace
  }
}
