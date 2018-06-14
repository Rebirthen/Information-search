/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgfinal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TFIDF {

    public double tf(List<String> doc, String term) {

        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word.toLowerCase())) {
                result++;
            }
        }
        return result / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word.toLowerCase())) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
//                
    }

    /**
     * @param doc a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);

    }

    public static void main(String[] args) {
        HashMap<String, Double> map = new HashMap<String, Double>();
        System.out.println("Enter your new: ");
        Scanner scanner = new Scanner(System.in);
        String news = scanner.nextLine().toLowerCase();

        List<List<String>> documents = new ArrayList<List<String>>();
        String[] words = news.split("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})");

        System.out.println(words);
        Set<String> set = new HashSet<String>();
        List<Double> listcosine = new ArrayList<Double>();
        List<String> list = new ArrayList<String>();
        List<String> listok = new ArrayList<String>();
        List<String> docs0 = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {

            set.add(words[i]);

        }
        list.add("");
        list.add(",");
        list.add(":");
        list.add(";");
        list.add("!");
        list.add("?");
        list.add("-");
        

        set.removeAll(list);
        listok.addAll(set);

        System.out.println(set);

        ///test.json
        FileReader reader;
        try {
            reader = new FileReader("test.json");
            JsonParser jsonParser = new JsonParser();
            JsonArray array = (JsonArray) jsonParser.parse(reader);
            String[] values;
            for (JsonElement jsonElement : array) {
                for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                    values = entry.getValue().toString().split("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})");

                    if (values.length > 10) {
                        docs0.add(entry.getValue().toString());
                        documents.add(Arrays.asList(values));
                    }

                }

            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        Test calculator = new Test();

        int N = listok.size();
        int M = documents.size();
        double[] matrix0 = new double[M];
        for (int i = 0; i < N; i++) {
            matrix0[i] = calculator.tfIdf(Arrays.asList(words), documents, listok.get(i));
            System.out.println(matrix0[i] + "");
        }
       
        double[][] matrix = new double[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = calculator.tfIdf(documents.get(i), documents, listok.get(j));

            }
        }
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("Doc: " + i + "---->");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < matrix.length; i++) {
            double dotProduct = 0.0, firstNorm = 0.0, secondNorm = 0.0;
            for (int j = 0; j < matrix[i].length; j++) {

                dotProduct += (matrix[i][j] * matrix0[j]);
                firstNorm += pow(matrix[i][j], 2);
                secondNorm += pow(matrix0[j], 2);
            }
            double cosinSimilarity = (dotProduct / (sqrt(firstNorm) * sqrt(secondNorm)));
            listcosine.add(cosinSimilarity);

        }

        for (int i = 0; i < listcosine.size(); i++) {
            System.out.println("New: "+docs0.get(i)+" "+ "Cosine similarity: "+listcosine.get(i));
            System.out.println(" ");

        }
        int minIndex = listcosine.indexOf(Collections.min(listcosine));
        System.out.println(" MOst similar: "+docs0.get(minIndex));

//        Double[] cosinus = new Double[map.size()];
//        Set fintext = map.keySet();
//        Iterator t = fintext.iterator();
//        int a = 0;
//        while (t.hasNext()) {
//            cosinus[a] = map.get(t.next());
//            a++;
//        }
//        Arrays.sort(cosinus);
//        for (int i = 0; i < map.size(); i++) {
//            t = fintext.iterator();
//            while (t.hasNext()) {
//                String temp = (String) t.next();
//                if (cosinus[i].equals(map.get(temp))) {
//                    System.out.println("Cosine distance=>"+cosinus[i] + " Text = > " + temp);
//                }
//            }
//        }

    }

}
