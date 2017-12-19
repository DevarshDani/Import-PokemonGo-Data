package OSMPParser;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

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
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class Pokemon {
   public static void main(String[] args) throws IOException
{
        Path pt0 = new Path(args[0]);
        File file = new File(pt0.getName());

        Configuration config = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(config);
        HTableDescriptor htable = new HTableDescriptor("PokemonDD09");
        htable.addFamily( new HColumnDescriptor("PokestopData"));
        HBaseAdmin hbase_admin = new HBaseAdmin(config);
        hbase_admin.createTable(htable);
        Long i = new Long(1);

              try {
                String[] longlat = new String[3];
                String[] Name = new String[2];
                Table tab = conn.getTable(TableName.valueOf("PokemonDD09"));

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();

                NodeList nl = doc.getElementsByTagName("Folder");
                if( nl.getLength() > 0 )
                {
                for( int j=0; j < nl.getLength(); j++)
                {
                        Node nod1 = nl.item(j);
                        Element et = (Element) nod1;
                        String type = et.getElementsByTagName("name").item(0).getTextContent().toString();
                        NodeList nList = et.getElementsByTagName("Placemark");

                        if( nList.getLength() > 0 )
                        {
                        for (int temp = 0; temp < nList.getLength(); temp++, i++)
                        {
                        Node nNode = nList.item(temp);
                        Element eElement = (Element) nNode;
                        Name = eElement.getElementsByTagName("name").item(0).getTextContent().toString().split("\\r?\\n");
                        longlat = eElement.getElementsByTagName("coordinates").item(0).getTextContent().toString().split(",");
                        Put p = new Put(Bytes.toBytes(i.toString()));
                        p.add(Bytes.toBytes("PokestopData"), Bytes.toBytes("Type"), type.getBytes());
                        p.add(Bytes.toBytes("PokestopData"), Bytes.toBytes("Placemark"), Bytes.toBytes(Name[0]));
                        p.add(Bytes.toBytes("PokestopData"), Bytes.toBytes("Latitude"), Bytes.toBytes(longlat[0]));
                        p.add(Bytes.toBytes("PokestopData"), Bytes.toBytes("Longitude"), Bytes.toBytes(longlat[1]));
                        tab.put(p);
                        }
                        }
                }

                }
}
        catch (Exception e) {
         e.printStackTrace();
        }

     }
}
