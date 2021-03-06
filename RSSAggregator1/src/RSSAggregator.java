import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to convert an XML RSS (version 2.0) feed from a given URL into the
 * corresponding HTML output file.
 *
 * @author Martin Hobby
 *
 */
public final class RSSAggregator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RSSAggregator() {
    }

    /**
     * Outputs the "opening" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * <html> <head> <title>feeds title tag as title</title> </head> <body>
     * <ul>
     * list of feeds for the index page <channel> link
     * <ul>
     * </body> </html>
     *
     * @param feeds
     *            the feeds element XMLTree
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the root of channel is a <channel> tag] and out.is_open
     * @ensures out.content = #out.content * [the HTML "opening" tags]
     */
    private static void indexOutputHeader(XMLTree feeds, SimpleWriter out) {

        String title = feeds.attributeValue("title");

        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");
        out.println(title);
        out.println("</head>");
        out.println("<body>");
        out.println("<ul>");

        for (int i = 0; feeds.numberOfChildren() > i; i++) {
            if (feeds.child(i).label().equals("feed")) {
                processFeed(feeds.child(i).attributeValue("url"), feeds
                        .child(i).attributeValue("file"), out);
                out.println("<li><a href=\""
                        + feeds.child(i).attributeValue("url") + "\">"
                        + feeds.child(i).attributeValue("name") + "</a></li>");
            }

        }
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Processes one XML RSS (version 2.0) feed from a given URL converting it
     * into the corresponding HTML output file.
     *
     * @param url
     *            the URL of the RSS feed
     * @param file
     *            the name of the HTML output file
     * @param out
     *            the output stream to report progress or errors
     * @updates out.content
     * @requires out.is_open
     * @ensures <pre>
     * [reads RSS feed from url, saves HTML document with table of news items
     *   to file, appends to out.content any needed messages]
     * </pre>
     */
    private static void processFeed(String url, String file, SimpleWriter out) {
        XMLTree xml = new XMLTree1(url);
        SimpleWriter fileOut = new SimpleWriter1L(file);

        outputHeader(xml.child(0), fileOut);
        outputFooter(fileOut);
    }

    /**
     * Outputs the "opening" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * <html> <head> <title>the channel tag title as the page title</title>
     * </head> <body> <h1>the page title inside a link to the <channel> link<h1>
     * <p>
     * the channel description
     * </p>
     * <table>
     * <tr>
     * <th>Date</th>
     * <th>Source</th>
     * <th>News</th>
     * </tr>
     *
     * @param channel
     *            the channel element XMLTree
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the root of channel is a <channel> tag] and out.is_open
     * @ensures out.content = #out.content * [the HTML "opening" tags]
     */
    private static void outputHeader(XMLTree channel, SimpleWriter out) {
        assert channel != null : "Violation of: channel is not null";
        assert out != null : "Violation of: out is not null";
        assert channel.isTag() && channel.label().equals("channel") : ""
                + "Violation of: the label root of channel is a <channel> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        /*
         * Assigns title variable either data from 'title' child, or
         * 'description child if 'title' is empty. If neither exist, assigns
         * value of "No information" to title.
         */
        String title = "";
        if (getChildElement(channel, "title") > 0) {
            title = channel.child(getChildElement(channel, "title")).label();
        } else if (getChildElement(channel, "description") > 0) {
            title = channel.child(getChildElement(channel, "description"))
                    .label();
        } else {
            title = "No information";
        }

        /*
         * runs getChildElement to assign value to String description. If there
         * is no description value, "No Description" is assigned to the
         * description string
         */
        String description = "";
        if (channel.child(getChildElement(channel, "description"))
                .numberOfChildren() < 1) {
            description = "No Description.";
        } else {
            description = channel
                    .child(getChildElement(channel, "description")).child(0)
                    .label();
        }

        // Prints all the necessary html format to the outfile
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1><a href=\""
                + channel.child(getChildElement(channel, "link")).child(0)
                .label() + "\">" + title + "</a></h1>");
        out.println("<p>" + description + "</p>");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Date</th>");
        out.println("<th>Source</th>");
        out.println("<th>News</th>");
        out.println("</tr>");

        // Goes through each of 'channel' children and creates a table in html via the processItem method
        int i = 0;
        while (channel.numberOfChildren() > i) {
            if (channel.child(i).label().equals("item")) {
                processItem(channel.child(i), out);
            }
            i++;
        }

    }

    /**
     * Finds the first occurrence of the given tag among the children of the
     * given {@code XMLTree} and return its index; returns -1 if not found.
     *
     * @param xml
     *            the {@code XMLTree} to search
     * @param tag
     *            the tag to look for
     * @return the index of the first child of type tag of the {@code XMLTree}
     *         or -1 if not found
     * @requires [the label of the root of xml is a tag]
     * @ensures <pre>
     * getChildElement =
     *  [the index of the first child of type tag of the {@code XMLTree} or
     *   -1 if not found]
     * </pre>
     */
    private static int getChildElement(XMLTree xml, String tag) {
        assert xml != null : "Violation of: xml is not null";
        assert tag != null : "Violation of: tag is not null";
        assert xml.isTag() : "Violation of: the label root of xml is a tag";

        /*
         * If xml has an attribute called 'tag', it will provide the index value
         * for the position of the 'tag' attribute. If there is no attribute
         * called 'tag', then it returns -1.
         */
        int i = xml.numberOfChildren(), index = -1;

        while (i > 0 && index < 0) {
            i--;
            if (xml.child(i).label().equals(tag)) {
                index = i;
            }
        }

        return index;
    }

    /**
     * Processes one news item and outputs one table row. The row contains three
     * elements: the publication date, the source, and the title (or
     * description) of the item.
     *
     * @param item
     *            the news item
     * @param out
     *            the output stream
     * @updates out.content
     * @requires <pre>
     * [the label of the root of item is an <item> tag] and out.is_open
     * </pre>
     * @ensures <pre>
     * out.content = #out.content *
     *   [an HTML table row with publication date, source, and title of news item]
     * </pre>
     */
    private static void processItem(XMLTree item, SimpleWriter out) {
        assert item != null : "Violation of: item is not null";
        assert out != null : "Violation of: out is not null";
        assert item.isTag() && item.label().equals("item") : ""
                + "Violation of: the label root of item is an <item> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        out.println("<tr>");

        // pubDate string is assigned a value if pubDate returns a value, otherwise it is assigned a value of "No Date Available"
        String pubDate = "";
        if (item.child(getChildElement(item, "pubDate")).numberOfChildren() < 1) {
            pubDate = "No Date Available";
        } else {
            pubDate = item.child(getChildElement(item, "pubDate")).child(0)
                    .label();
        }
        out.println("<th>" + pubDate + "</th>");

        // source string is assigned a value if a label of one of item's children returns a value "source", otherwise it is assigned an empty value. The results are then printed to the outfile
        String source = "No Source Available.";
        String sourceURL = "";
        int i = 0, x = -1;
        while (item.numberOfChildren() > i) {
            if (item.child(i).label().equals("source")) {
                source = item.child(i).child(0).label();
                sourceURL = item.child(i).attributeValue("url");
                out.println("<th><a href=\"" + sourceURL + "\">" + source
                        + "</th>");
                x = 0;
            }
            i++;
        }
        if (x != 0) {
            out.println("<th>" + source + "</th>");
        }

        // news string is assigned a value if "description" returns a value greater than 1 from getChildElement. If "description" does not return a value, "title" will be run in getChildElement.If neither return any value, the default "No description" will be printed
        String news = "No description";
        if (getChildElement(item, "description") > 0) {
            news = item.child(getChildElement(item, "description")).child(0)
                    .label();
        } else if (getChildElement(item, "title") > 0) {
            news = item.child(getChildElement(item, "title")).child(0).label();
        }

        String link = "";
        if (getChildElement(item, "link") > 0) {
            link = item.child(getChildElement(item, "link")).child(0).label();
        }

        out.println("<th><a href=\"" + link + "\">" + news + "</th>");
        out.println("</tr>");
    }

    /**
     * Outputs the "closing" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * </table> </body> </html>
     *
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "closing" tags]
     */
    private static void outputFooter(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        // Prints the closing html data to the outfile
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // Requests user input xml file that contains url feeds and then asks user to name output file.
        out.print("Please enter a XML file with URL feeds: ");
        String inputXML = in.nextLine();
        XMLTree xml = new XMLTree1(inputXML);

        out.print("Please name the output file: ");
        String outfileName = in.nextLine();

        SimpleWriter fileOut = new SimpleWriter1L(outfileName);

        indexOutputHeader(xml, fileOut);

        in.close();
        out.close();
    }

}