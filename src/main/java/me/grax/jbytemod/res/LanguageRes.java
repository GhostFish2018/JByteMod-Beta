package me.grax.jbytemod.res;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.utils.ErrorDisplay;

public class LanguageRes {
  private final HashMap<String, String> map = new HashMap<>();
  private final HashMap<String, String> defaultMap = new HashMap<>();

  public LanguageRes() {
    JByteMod.LOGGER.log("Reading Language XML..");
    this.readXML(map, getXML());
    this.readXML(defaultMap, LanguageRes.class.getResourceAsStream("/locale/en.xml"));
    JByteMod.LOGGER.log("Successfully loaded " +map.size() + " local resources and " + defaultMap.size() + " default resources");
  }

  public String getResource(String desc) {
    return map.getOrDefault(desc, defaultMap.getOrDefault(desc, desc));
  }

  private void readXML(Map<String, String> m, InputStream is) {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);
      doc.getDocumentElement().normalize();
      Element resources = doc.getDocumentElement();
      NodeList nodes = resources.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node e = (Node) nodes.item(i);
        if (e.getNodeName().equals("string")) {
          Element el = (Element) e;
          m.put(el.getAttribute("name"), e.getTextContent());
        }
      }
    } catch (Exception e) {
      new ErrorDisplay(e);
      JByteMod.LOGGER.err("Failed to load resources: " + e.getMessage());
    }
  }

  private InputStream getXML() {
    InputStream is = LanguageRes.class.getResourceAsStream("/locale/" + this.getLanguage() + ".xml");
    if (is == null) {
      JByteMod.LOGGER.warn("Locale not found, using default en.xml");
      is = LanguageRes.class.getResourceAsStream("/locale/en.xml");
      if (is == null) {
        JByteMod.LOGGER.err("en.xml not found!");
      }
    }
    return is;
  }

  private String getLanguage() {
    return System.getProperty("user.language");
  }
}
