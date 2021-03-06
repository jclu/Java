/* $Header: Utils.java 2017/02/27 03:28:43 jiaclu Exp $ */

/* Copyright (c) 2017, jiaclu. All rights reserved.*/

/*
 DESCRIPTION
 Utilities

 PRIVATE CLASSES
 <list of private classes defined - with one-line descriptions>

 NOTES
 <other useful comments, qualifications, etc.>

 MODIFIED    (MM/DD/YY)
 jiaclu      02/27/17 - Creation
 */
/**
 * @version $Header: Utils.java 2017/02/27 03:28:43 jiaclu Exp $
 * @author jiaclu
 * @since release specific (what release of product did this appear in)
 */
package com.x.utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.Assert;

public class TestUtils {

    public static String uri = null;
    public static String entityNamingUrl = null;
    public static String authorization = null;
    public static String tenantId = null;

  /**
	 * Get method name 
	 */
	public static String getMethodName() {
       	return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
  

    public static String getDBProperty(String property) {

        String propertyValue = null;
        int propertyPosition = 0;

        url = getUrl();
        authorization = Utils.getProperty("SAAS_AUTH_TOKEN");
        tenantId = Utils.getProperty("TENANT_ID");

        RestAssured.baseURI = url;
        uri = "/lookups" + "?domainName=global";

        Response response = RestAssured.
            given().
                headers("Authorization", authorization, "X-USER-IDENTITY-DOMAIN-NAME", tenantId).
                contentType("application/json").
            when().
                get(uri);

        Assert.assertTrue(response.getStatusCode() == 200);

        System.out.println("responseBody: " + response.body().asString());
        String responseBody = response.body().asString();
        JsonPath jsonPath = new JsonPath(responseBody);

        int total = jsonPath.get("total");
        System.out.println("total: " + total);

        for (int i = 0; i < total; i++) {
            ArrayList domainName = jsonPath.get("items.domainName");
            ArrayList value = jsonPath.get("items.keys.value");

            System.out.println("domainName: " + i + ": " + domainName.get(i));
            System.out.println("value: " + i + ": " + value.get(i));
            ArrayList t = (ArrayList) value.get(i);
            String tenant = t.get(0).toString();
            System.out.println("tenant: " + i + ": " + tenant);

            if (domainName.get(i).equals("global") && tenant.equals(tenantId)) {
            	String jsonPathStr = "items.values." + property;
                ArrayList allPropertyList = jsonPath.get(jsonPathStr);
                ArrayList onePropertyContent = (ArrayList) allPropertyList.get(i);
                System.out.println("onePropertyContent: " + i + ": " + onePropertyContent);
                
                for (int j = 0; j < onePropertyContent.size(); j++) {
                	  if (onePropertyContent.get(j) != null) {
                		    propertyValue = onePropertyContent.get(j).toString();
                		    System.out.println("=========================");
                        System.out.println(property + ": " + propertyValue);
                        System.out.println("=========================");
                        break;
                	  }
                }

                break;
            }
        }

        return propertyValue;

    }
    
    public static void writeProperty(String property) {

        String T_WORK = System.getenv("T_WORK");

        FileOutputStream fos = null;
        File file;

        try {

            String fileName = T_WORK + "/env.properties";
            file = new File(fileName);
            fos = new FileOutputStream(file, true);

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // write the content in bytes			
            fos.write(property.getBytes());
            fos.write("\n".getBytes());
            fos.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    public static String getProperty(String property) {

        String T_WORK = System.getenv("T_WORK");

        FileInputStream fis = null;
        File file;
        Properties prop = null;

        prop = new Properties();
        
        try {
            String fileName = T_WORK + "/env.properties";
            file = new File(fileName);
            fis = new FileInputStream(file);

            // Load fis to properties
            prop.load(fis);

            fis.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return prop.getProperty(property);
    }

}
