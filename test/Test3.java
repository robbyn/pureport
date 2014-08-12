import java.text.AttributedString;
import java.util.Iterator;
import java.util.Map;

public class Test3 {
    public static void main(String args[]) {
        try {
            AttributedString as = new AttributedString("A string");
            Map attrs = as.getIterator().getAttributes();
            for (Iterator it = attrs.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry)it.next();
                System.out.println(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
