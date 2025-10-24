package com.francescoceliento.network;

import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RssReader {

    private URL url;

    // Oggetto item
    public static class RssItem {
        private String title;
        private String link;
        private String description;
        private String pubDate;

        public RssItem() {
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getDescription() {
            return description;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        @Override
        public String toString() {
            return "RssItem{" +
                   "title='" + title + '\'' +
                   ", link='" + link + '\'' +
                   ", pubDate='" + pubDate + '\'' +
                   ", description='" + (description != null && description.length() > 50 ? description.substring(0, 50) + "..." : description) + '\'' +
                   '}';
        }
    }
      
    /**
     * Configura l'URL del feed RSS da leggere
     * @param urlString
     * @throws IllegalArgumentException
     */
    public void setUrl(String urlString) throws IllegalArgumentException {
        if (urlString == null || urlString.trim().isEmpty()) {
            throw new IllegalArgumentException("L'URL non può essere nullo o vuoto.");
        }
        try {
            this.url = new URL(urlString);
        } catch (java.net.MalformedURLException e) {
            throw new IllegalArgumentException("Formato URL non valido: " + urlString, e);
        }
    }
    
    /**
     * Legge e parsifica il feed RSS dall'URL configurato
     * @return
     * @throws IllegalStateException
     * @throws RssReadException
     */
    public List<RssItem> read() throws IllegalStateException, RssReadException {
        if (this.url == null) {
            throw new IllegalStateException("L'URL del feed RSS deve essere configurato prima di chiamare read(). Utilizza setUrl().");
        }

        List<RssItem> items = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            
            Document doc = dBuilder.parse(this.url.openStream());
            
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    RssItem item = new RssItem();

                    item.setTitle(getTagValue("title", eElement));
                    item.setLink(getTagValue("link", eElement));
                    item.setDescription(getTagValue("description", eElement));
                    item.setPubDate(getTagValue("pubDate", eElement));

                    items.add(item);
                }
            }
        } catch (Exception e) {
            
            throw new RssReadException("Errore durante la lettura o il parsing del feed RSS.", e);
        }
        return items;
    }

    // Metodo di utilità per estrarre il valore del testo da un tag XML
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            NodeList textList = nodeList.item(0).getChildNodes();
            if (textList.getLength() > 0) {
            	
                return textList.item(0).getNodeValue();
            }
        }
        return null;
    }

    // Classe di eccezione personalizzata per gli errori di lettura RSS
	public static class RssReadException extends Exception {
		private static final long serialVersionUID = 1L;

		public RssReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}