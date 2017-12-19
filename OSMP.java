package OSMPParser;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.hbase.client.HTable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;


public class OSMP {
   public static void main(String[] args) throws IOException
{
        Path pt0 = new Path(args[0]);
        File file = new File(pt0.getName());

        Configuration config = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(config);

        HTableDescriptor htable = new HTableDescriptor("OpenStreetMapDD09");
        htable.addFamily( new HColumnDescriptor("OSMData"));
        HBaseAdmin hbase_admin = new HBaseAdmin(config);
        hbase_admin.createTable(htable);
        Integer key = new Integer(1);
//      String amenity;
//      String name;

              try {

                String amenity;
                String name;
//              Table tab = conn.getTable(TableName.valueOf("OpenStreetMapDD09"));
                HTable tab = new HTable(config, "OpenStreetMapDD09");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();

                NodeList nl = doc.getElementsByTagName("node");
                if( nl.getLength() > 0 )
                {
                for( int i = 0; i < nl.getLength(); i++ )
                {       amenity = null;
                        name = null;
                        Node nod1 = nl.item(i);
                        Element et = (Element) nod1;
                        NodeList nlist = et.getElementsByTagName("tag");
                        if( nlist.getLength() > 0 )
                        {
                        for( int j = 0; j < nlist.getLength(); j++ )
                        {
                                Node nod2 = nlist.item(j);
                                Element et2 = (Element) nod2;

                                if ( et2.getAttribute("k").toString().equals( new String("amenity")) )
                                {
                                amenity = et2.getAttribute("v").toString();
                                continue;
                                }
                                if ( et2.getAttribute("k").toString().equals( new String("name")) )
                                {
                                name = et2.getAttribute("v").toString();
				break;
                                }
                        }
                        }

                        if ( amenity == null || name == null)
                        {
                        continue;
                        }
                        String id = et.getAttribute("id").toString();
                        String OSMlat = et.getAttribute("lat").toString();
                        String OSMlon = et.getAttribute("lon").toString();
                        Put p = new Put(Bytes.toBytes(key.toString()));
                        p.add(Bytes.toBytes("OSMData"), Bytes.toBytes("NodeId"), Bytes.toBytes(id));
                        p.add(Bytes.toBytes("OSMData"), Bytes.toBytes("Amenity"), Bytes.toBytes(amenity));
                        p.add(Bytes.toBytes("OSMData"), Bytes.toBytes("Name"), Bytes.toBytes(name));
                        p.add(Bytes.toBytes("OSMData"), Bytes.toBytes("Latitude"), Bytes.toBytes(OSMlat));
                        p.add(Bytes.toBytes("OSMData"), Bytes.toBytes("Longitude"), Bytes.toBytes(OSMlon));
                        tab.put(p);
                        key++;
                }
                }
//              tab.close();
          }
        catch (Exception e) {
         e.printStackTrace();
        }
     }
}
