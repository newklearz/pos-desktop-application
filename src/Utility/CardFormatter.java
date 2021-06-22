package Utility;
import javafx.scene.control.TextFormatter;
import java.text.DecimalFormat;
import java.text.ParsePosition;

/***

 * Formatter pentru campul numarului cardului. Valideaza doar cifre.
 */
public class CardFormatter extends TextFormatter<String> {
    public CardFormatter(){
        super((c ->
        {
            final DecimalFormat format = new DecimalFormat("0");
            if ( c.getControlNewText().isEmpty() )
            {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );

            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
            {
                return null;
            }
            else
            {
                return c;
            }
        }));
    }
}
