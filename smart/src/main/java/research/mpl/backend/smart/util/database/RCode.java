//package research.mpl.backend.smart.util.database;
//
//import graphics.GraphicsType;
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.swing.ImageIcon;
//
//import rcaller.Globals;
//import rcaller.RPlotViewer;
//
//
//public class RCode extends rcaller.RCode{
//
//
//  public RCode(){
//    super();
//  }
//
//  public RCode(StringBuffer sb){
//    super(sb);
//  }
//
//  public File startPlot() throws IOException {
//    return(startPlot(GraphicsType.png));
//  }
//
//
//  public File startPlot(GraphicsType type) throws IOException {
//    File f = File.createTempFile("RPlot", "." + type.name());
//    switch (type) {
//      case png:
//        addRCode("png(\"" + f.toString().replace("\\", "/") + "\", width=1600, height=1200, res=120)");
//        break;
//      case jpeg:
//        addRCode("jpeg(\"" + f.toString().replace("\\", "/") + "\")");
//        break;
//      case tiff:
//        addRCode("tiff(\"" + f.toString().replace("\\", "/") + "\")");
//        break;
//      case bmp:
//        addRCode("bmp(\"" + f.toString().replace("\\", "/") + "\")");
//        break;
//      default:
//        addRCode("png(\"" + f.toString().replace("\\", "/") + "\")");
//    }
//    addRCode(Globals.theme.generateRCode());
//    return (f);
//  }
//
//  public void endPlot() {
//    addRCode("dev.off()");
//  }
//
//  public ImageIcon getPlot(File f) {
//    ImageIcon img = new ImageIcon(f.toString());
//    return (img);
//  }
//
//  public void showPlot(File f) {
//    ImageIcon plot = getPlot(f);
//    RPlotViewer plotter = new RPlotViewer(plot);
//    plotter.setVisible(true);
//  }
//
//}
//
