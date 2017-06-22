package bz.sunlight.excelUtil;



public final class XMLEncoder
{
    private XMLEncoder()
    {
    }

    private static final String[] xmlCode = new String[256];

    static
    {
        // Special characters
        xmlCode['\''] = "&apos;";
        xmlCode['\"'] = "&quot;"; // double quote
        xmlCode['&'] = "&amp;"; // ampersand
        xmlCode['<'] = "&lt;"; // lower than
        xmlCode['>'] = "&gt;"; // greater than
    }

    public static String encode(String string)
    {
        if(string == null)
            return "";
        int n = string.length();
        char character;
        String xmlchar;
        StringBuffer buffer = new StringBuffer();
        // loop over all the characters of the String.
        for(int i = 0; i < n; i++)
        {
            character = string.charAt(i);
            // the xmlcode of these characters are added to a StringBuffer one by one
            try
            {
                xmlchar = xmlCode[character];
                if(xmlchar == null)
                {
                    buffer.append(character);
                }
                else
                {
                    buffer.append(xmlCode[character]);
                }
            }
            catch(ArrayIndexOutOfBoundsException aioobe)
            {
                buffer.append(character);
            }
        }
        return buffer.toString();
    }

    public static void main(String[] args)
    {
        String test = "中言\'\"4&<2>1sdfsdf";
        System.out.println(encode(test));
    }

}
