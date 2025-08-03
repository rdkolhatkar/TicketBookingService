package com.ratnakar.wiremock.runner;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WiremockRunner {
    public static void main(String[] args) {
        // Define the WireMock configuration
        WireMockConfiguration config = new WireMockConfiguration()
                .port(8092) // Ensure this matches your intended port
                .usingFilesUnderDirectory("src/wiremock/resources/static-mocks"); // Updated path

        // Start the WireMock server
        WireMockServer wireMockServer = new WireMockServer(config);
        wireMockServer.start();

        System.out.println("WireMock standalone server started on port 8091...");
        System.out.println("Serving stubs from: src/wiremock/resources/static-mocks");

        // Add a shutdown hook to stop WireMock gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            wireMockServer.stop();
            System.out.println("WireMock server stopped.");
        }));
    }
}

