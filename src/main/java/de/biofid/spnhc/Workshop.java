package de.biofid.spnhc;

import spark.Spark;
import spark.servlet.SparkApplication;

import java.io.IOException;

/**
 * @author Giuseppe Abrami
 * Sample implementation for processing texts with NLP methods
 */
public class Workshop implements SparkApplication {

    public static void main(String[] args) {

        Workshop ws = new Workshop();
        Spark.port(4568);

        ws.init();

    }

    @Override
    public void init() {

        Spark.staticFiles.location("/"); // Static files

        try {
            Methods.init();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("Running");

    }

    @Override
    public void destroy() {
        SparkApplication.super.destroy();
    }
}
