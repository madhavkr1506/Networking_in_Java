package com.example;

import java.util.Properties;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;


import java.net.URL;
import java.io.*;
import java.net.HttpURLConnection;

public class Sentence_Analysis {

    private static int counter = 0;
    private static String requestBody = "";
    private static String apibaseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";
        private static String apikey = "AIzaSyDhiRc_Rfd2CKOM801d5aoqKnu7q7pbovo";
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        String previousQuestion = "";
        while(true){

            try{
                System.out.print("Input Question: ");
                String userQuestion = reader.nextLine();

                if(userQuestion.equalsIgnoreCase("Exit")){
                    break;
                }

                boolean coreference = analyzeSentence(userQuestion);

                if (coreference && !previousQuestion.isEmpty()) {
                    String combinedInput = previousQuestion + " " + userQuestion;
                    requestBody = "{ \"contents\": [{\"parts\": [{\"text\": \"" + combinedInput + "\"}]}]}";
                } else {
                    requestBody = "{ \"contents\": [{\"parts\": [{\"text\": \"" + userQuestion + "\"}]}]}";
                    previousQuestion = userQuestion;
                }
                
                

                

                URL apiurl = new URL(apibaseUrl + apikey);
                HttpURLConnection connection = (HttpURLConnection) apiurl.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

            
                



                try(OutputStream out = connection.getOutputStream()){
                    byte[] data = requestBody.getBytes("utf-8");
                    out.write(data, 0, data.length);
                    out.flush();
                    out.close();
                }

                int responseCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    System.out.println("Response Code: " + responseCode);
                    System.out.println("Response Message: " + responseMessage);

                    StringBuilder responseString = new StringBuilder();
                    String inputLine;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((inputLine = in.readLine()) != null) {
                        responseString.append(inputLine);
                    }

                    in.close();
                    connection.disconnect();

                    ObjectMapper objectMapper = new ObjectMapper();

                    JsonNode treeNode = objectMapper.readTree(responseString.toString());

                    String BotResponse = treeNode.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
                    System.out.println("Bot Response: " + BotResponse);
                    
                    

                }

            }catch(Exception e){
                reader.close();
                e.printStackTrace();
            }
        }
        

    }


    public static boolean analyzeSentence(String string){

        counter++;
    
        if(counter > 1){
            Properties pipelineProperties = new Properties();
            pipelineProperties.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,coref");
    
            StanfordCoreNLP pipeline = new StanfordCoreNLP(pipelineProperties);
    
            Annotation annotation = new Annotation(string);
            pipeline.annotate(annotation);
    
            Map<Integer, CorefChain> corefChains = annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class);
    
            if (corefChains != null) {
                for (Map.Entry<Integer, CorefChain> entry : corefChains.entrySet()) {
                    CorefChain corefChain = entry.getValue();
    
                    if(corefChain.getMentionsInTextualOrder().size() > 1){
                        return true;
                    }
                }
            }
        }
    
        return false;
    }
}