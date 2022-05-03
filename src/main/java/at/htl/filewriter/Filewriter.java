package at.htl.filewriter;

import at.htl.entity.Node;
import at.htl.entity.Tournament;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import org.jboss.logging.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class Filewriter {

    private static final Logger LOG = Logger.getLogger("FileWriter");

    private static final String ORIGIN = "asciidocs/plantuml/Result.puml";
    private static final String TARGET = "asciidocs/images/generated-diagrams/";

    /*public void writeResult(String team01, String team02, int[] result){

        String resStr = result[0] + ":" + result[1];
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(ORIGIN,true));
            writer.write(
                    "@startsalt \n" +
            "{\n" +
                "{T\n" +
                    "+ Tournament\n" +
                    "++ " + team01 + " vs. " + team02 + " " + resStr + "\n" +
                "}\n" +
            "}\n" +
            "@endsalt\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void writeFinalResult(Node finalNode, Tournament t){

        Node currentNode = finalNode;
        Stack<Node> nodeStack = new Stack<>();
        try{
            FileWriter file = new FileWriter(ORIGIN);

            String content = String.format("""
                @startmindmap %s.png
                title %s
                """, t.getName(), t.getName()
            );

            while(currentNode != null || nodeStack.size() > 0) {
                while (currentNode != null) {
                    content+= "\n"+constructString(currentNode);
                    nodeStack.push(currentNode);
                    currentNode = currentNode.getLeftNode();
                }
                currentNode = nodeStack.pop();
                currentNode = currentNode.getRightNode();
            }
            content+= String.format("""
                caption Winner is: %s 
                @endmindmap
                """,finalNode.getCurMatch().getWinningTeam().getName()
            );

            file.write(content);
            file.close();

            convertToPNG(new File(ORIGIN));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeIntermediateResult(Node node, Tournament t){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(ORIGIN,true));

            writer.write(String.format(
                    """
                    @startmindmap
                    title %s
                    - %s
                    -- %s
                    -- %s
                    caption Winner is: %s
                    @endmindmap
                                            
                    """,
                    t.getName(),
                    node.getCurMatch().getMatchResultString(),
                    node.getLeftNode().getCurMatch().getMatchResultString(),
                    node.getRightNode().getCurMatch().getMatchResultString(),
                    node.getCurMatch().getWinningTeam().getName()
            ));

            //writer.write("@startmindmap \n title "+t.getName()+"\n");
            //writer.write("- "+node.getCurMatch().getMatchResultString()+"\n");
            //writer.write("-- "+node.getLeftNode().getCurMatch().getMatchResultString()+"\n");
            //writer.write("-- "+node.getRightNode().getCurMatch().getMatchResultString()+"\n");
            //writer.write("caption Winner is: "+node.getCurMatch().getWinningTeam().getName() + "\n");
            //writer.write("@endmindmap\n");
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String constructString(Node node){
        String line = "";
        for (int i = 0; i < node.getPhase().getLevel(); i++){
            line += "-";
        }

        return line += " "+node.getCurMatch().getMatchResultString()+"\n";

    }

    public void convertToPNG(File source){
        try {
            SourceFileReader reader = new SourceFileReader(source);
            List<GeneratedImage> list = reader.getGeneratedImages();
            File png = list.get(0).getPngFile();

            png.renameTo(new File(TARGET+png.getName()));

            if(png.createNewFile()) {
                LOG.info(String.format("new file %s created", png.getName()));
            }else
                LOG.error("File already exists");

            png.delete();
        }catch(IOException e){
            LOG.error("no file to convert!");
        }
    }
}