package com.example;

import java.util.Properties;
import java.util.List;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalysis {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

        System.out.print("Input text: ");
        String text = System.console().readLine();

        Annotation annotation = new Annotation(text);

        pipeline.annotate(annotation);

        List<CoreMap>sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence : sentences){
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println( "Sentence: " + sentence + " || Sentiment: " + sentiment);

            for(CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){
                // String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                // System.out.println("Lemma: " + lemma);

                String part_of_speech = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                System.out.println("Part Of Speech: " + part_of_speech);

                // String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                // System.out.println("Name: " + ner);
            }

            Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println("Parse: ");
            parseTree.pennPrint();
        }

    }
}
